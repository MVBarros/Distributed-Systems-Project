package com.forkexec.pts.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Response;

import com.forkexec.pts.ws.PointsReadResponse;
import com.forkexec.pts.ws.PointsService;
import com.forkexec.pts.ws.PointsWriteResponse;
import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.Balance;
import com.forkexec.pts.ws.PointsPortType;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

public class PointsFrontEnd {

	private int quorumSize;
	/* Thread access */
	static int writeValue;

	static Balance bestReturn = new Balance();

	/* Locks */

	static Map<String, AtomicInteger> locks = new ConcurrentHashMap<>();

	/** WS service */
	PointsService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	Collection<PointsPortType> ports = new ArrayList<PointsPortType>();

	/** UDDI server URL */
	private String uddiURL = null;

	/** WS name */
	private String wsName = null;

	/** WS end point address */
	private Collection<String> wsURL = null;

	public Collection<String> getWsURL() {
		return wsURL;
	}

	/** output option **/
	private boolean verbose = false;

	public boolean isVerbose() {
		return verbose;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	/** constructor with provided web service URL */
	public PointsFrontEnd(Collection<String> wsURL) throws PointsClientException {
		this.wsURL = wsURL;
		createStub();
	}

	/** constructor with provided UDDI location and name */
	public PointsFrontEnd(String uddiURL, String wsName) throws PointsClientException {
		this.uddiURL = uddiURL;
		this.wsName = wsName;
		uddiLookup();
		createStub();
	}

	/** UDDI lookup */
	private void uddiLookup() throws PointsClientException {
		try {
			if (verbose)
				System.out.printf("Contacting UDDI at %s%n", uddiURL);
			UDDINaming uddiNaming = new UDDINaming(uddiURL);

			if (verbose)
				System.out.printf("Looking for '%s'%n", wsName);
			wsURL = uddiNaming.list(wsName);

		} catch (Exception e) {
			String msg = String.format("Client failed lookup on UDDI at %s!", uddiURL);
			throw new PointsClientException(msg, e);
		}

		if (wsURL == null) {
			String msg = String.format("Service with name %s not found on UDDI at %s", wsName, uddiURL);
			throw new PointsClientException(msg);
		}
	}

	/** Stub creation and configuration */
	private void createStub() {
		if (verbose)
			System.out.println("Creating stub ...");

		for (String url : wsURL) {
			service = new PointsService();
			PointsPortType port = service.getPointsPort();
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, url);

			ports.add(port);
		}
		quorumSize = ports.size() / 2 + 1;
	}

	public void ctrlClear() {
		for (PointsPortType port : ports) {
			port.ctrlClear();
		}
	}

	public void ctrlInit(int pts) throws BadInitFault_Exception {
		for (PointsPortType port : ports) {
			port.ctrlInit(pts);
		}
	}

	public String ctrlPing(String str) {
		String result = "";
		for (PointsPortType port : ports) {
			result.concat(port.ctrlPing(str));
		}
		return result;
	}

	public int pointsWrite(String email, int points, long tag) {
		synchronized (locks.computeIfAbsent(email, k -> new AtomicInteger())) {
			locks.get(email).set(0);

			for (PointsPortType port : ports) {
				port.pointsWriteAsync(email, points, tag, new AsyncHandler<PointsWriteResponse>() {

					@Override
					public synchronized void handleResponse(Response<PointsWriteResponse> response) {
						try {
							writeValue = response.get().getReturn();
							locks.get(email).getAndIncrement();
						} catch (InterruptedException e) {
							System.out.println("Caught interrupted exception.");
							System.out.print("Cause: ");
							System.out.println(e.getCause());
						} catch (ExecutionException e) {
							System.out.println("Caught execution exception.");
							System.out.print("Cause: ");
							System.out.println(e.getCause());
						}
					}
				});
			}
			while (locks.get(email).get() < quorumSize) {
				try {
					/* Wait for Quorum */
					Thread.sleep(10);
				} catch (InterruptedException e) {
					System.out.println("Caught interrupted exception.");
					System.out.print("Cause: ");
					System.out.println(e.getCause());
				}
			}
			/* write returns written value */
		}
		return writeValue;

	}

	public Balance pointsRead(String email) {
		synchronized (locks.computeIfAbsent(email, k -> new AtomicInteger())) {
			locks.get(email).set(0);

			bestReturn = new Balance();
			bestReturn.setTag((long) -1);

			for (PointsPortType port : ports) {

				port.pointsReadAsync(email, new AsyncHandler<PointsReadResponse>() {

					@Override
					public synchronized void handleResponse(Response<PointsReadResponse> response) {
						try {
							if (bestReturn.getTag() < response.get().getReturn().getTag()) {
								bestReturn.setPoints(response.get().getReturn().getPoints());
								bestReturn.setTag(response.get().getReturn().getTag());
							}
							locks.get(email).incrementAndGet();

						} catch (InterruptedException e) {
							System.out.println("Caught interrupted exception.");
							System.out.print("Cause: ");
							System.out.println(e.getCause());
						} catch (ExecutionException e) {
							System.out.println("Caught execution exception.");
							System.out.print("Cause: ");
							System.out.println(e.getCause());
						}
					}
				});
			}

			while (locks.get(email).get() < quorumSize) {
				try {
					Thread.sleep(10 /* milliseconds */);
					/* Wait for Quorum */
				} catch (InterruptedException e) {
					System.out.println("Caught interrupted exception.");
					System.out.print("Cause: ");
					System.out.println(e.getCause());
				}
			}
		}
		return bestReturn;
	}
}