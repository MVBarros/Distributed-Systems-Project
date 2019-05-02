package com.forkexec.hub.ws;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import com.forkexec.pts.ws.PointsReadResponse;
import com.forkexec.pts.ws.PointsWriteResponse;
import com.forkexec.pts.ws.BadInitFault_Exception;
import com.forkexec.pts.ws.Balance;
import com.forkexec.pts.ws.cli.PointsClient;

public class HubFrontEnd extends PointsClient {
	private Collection<PointsClient> clients;
	private int quorumSize;

	static AtomicInteger writeCounter = new AtomicInteger();
	static int writeValue;

	static AtomicInteger readCounter = new AtomicInteger();
	static Balance bestReturn = new Balance();

	public HubFrontEnd(Collection<PointsClient> clients) {
		super();
		this.clients = clients;
		this.quorumSize = clients.size() / 2 + 1;
	}

	public void ctrlClear() {
		for (PointsClient client : clients) {
			client.ctrlClear();
		}
	}

	public void ctrlInit(int pts) throws BadInitFault_Exception {
		for (PointsClient client : clients) {
			client.ctrlInit(pts);
		}
	}

	public String ctrlPing(String str) {
		String result = "";
		for (PointsClient client : clients) {
			result.concat(client.ctrlPing(str));
		}
		return result;
	}

	@Override
	public synchronized int pointsWrite(String email, int points, long tag) {
		writeCounter.set(0);

		for (PointsClient client : clients) {
			client.pointsWriteAsync(email, points, tag, new AsyncHandler<PointsWriteResponse>() {

				@Override
				public synchronized void handleResponse(Response<PointsWriteResponse> response) {
					try {
						writeValue = response.get().getReturn();
						writeCounter.getAndIncrement();
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
		while (writeCounter.get() < quorumSize) {
			try {
				/*Wait for Quorum*/
				Thread.sleep(10);
			} catch (InterruptedException e) {
				System.out.println("Caught interrupted exception.");
				System.out.print("Cause: ");
				System.out.println(e.getCause());
			}
		}
		/* write returns written value */
		return writeValue;

	}

	@Override
	public Balance pointsRead(String email) {
		readCounter.set(0);

		bestReturn = new Balance();
		bestReturn.setTag((long) -1);

		for (PointsClient client : clients) {

			client.pointsReadAsync(email, new AsyncHandler<PointsReadResponse>() {

				@Override
				public synchronized void handleResponse(Response<PointsReadResponse> response) {
					try {
						if (bestReturn.getTag() < response.get().getReturn().getTag()) {
							bestReturn.setPoints(response.get().getReturn().getPoints());
							bestReturn.setTag(response.get().getReturn().getTag());
						}
						readCounter.incrementAndGet();

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

		while (readCounter.get() < quorumSize) {
			try {
				Thread.sleep(10 /* milliseconds */);
				/*Wait for Quorum*/
			} catch (InterruptedException e) {
				System.out.println("Caught interrupted exception.");
				System.out.print("Cause: ");
				System.out.println(e.getCause());
			}
		}

		return bestReturn;
	}
}