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

	static AtomicInteger respostasRecebidas = new AtomicInteger();
	static Balance bestReturn = new Balance();

	public HubFrontEnd(Collection<PointsClient> clients) {
		super();
		this.clients = clients;
		this.quorumSize = clients.size() / 2 + 1;
		System.out.print("quorumSize-->");

		System.out.println(this.quorumSize);
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
				public void handleResponse(Response<PointsWriteResponse> response) {
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
				Thread.sleep(10);
				// System.out.println(writeCounter.get());
			} catch (InterruptedException e) {
				System.out.println("Caught interrupted exception.");
				System.out.print("Cause: ");
				System.out.println(e.getCause());
			}
		}
		return writeValue;

	}

	@Override
	public Balance pointsRead(String email) {
		respostasRecebidas.set(0);

		bestReturn = new Balance();
		bestReturn.setTag((long) -1);

		for (PointsClient client : clients) {

			client.pointsReadAsync(email, new AsyncHandler<PointsReadResponse>() {
				@Override
				public void handleResponse(Response<PointsReadResponse> response) {
					System.out.println("Entrei aqui");
					try {
						if (bestReturn.getTag() < response.get().getReturn().getTag()) {
							bestReturn.setPoints(response.get().getReturn().getPoints());
							bestReturn.setTag(response.get().getReturn().getTag());
						}
						respostasRecebidas.incrementAndGet();

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

		while (respostasRecebidas.get() < quorumSize) {
			try {
				Thread.sleep(10 /* milliseconds */);
				// System.out.print("TAG-->");
				// System.out.println(bestReturn.getTag());
				/*
				 * System.out.print("Respostas recebidas -->");
				 * System.out.println(respostasRecebidas.get()); System.out.println(bestReturn);
				 */
				// System.out.print("POINTS-->");
				// System.out.println(bestReturn.getPoints());
			} catch (InterruptedException e) {
				System.out.println("Caught interrupted exception.");
				System.out.print("Cause: ");
				System.out.println(e.getCause());
			}
		}

		return bestReturn;
	}
}