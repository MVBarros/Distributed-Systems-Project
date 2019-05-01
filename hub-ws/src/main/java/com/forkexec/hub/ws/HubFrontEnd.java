package com.forkexec.hub.ws;

import java.util.Collection;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import com.forkexec.pts.ws.PointsReadResponse;
import com.forkexec.pts.ws.PointsWriteResponse;
import com.forkexec.pts.ws.cli.PointsClient;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class HubFrontEnd {
	private Collection<PointsClient> clients;
	private int quorumSize;

	private int writeValue;

	public HubFrontEnd(Collection<PointsClient> clients) {
		this.clients = clients;
		this.quorumSize = clients.size() / 2 + 1;
	}


	public synchronized int pointsWriteAsync(String email, int points, long tag) {
		AtomicInteger writeCounter = new AtomicInteger();
		writeCounter.set(0);

		for (PointsClient client : clients) {
			client.pointsWriteAsync(email, points, tag, new AsyncHandler<PointsWriteResponse>() {
				@Override
				public void handleResponse(Response<PointsWriteResponse> response) {
					try {
						writeValue = response.get().getReturn();
						writeCounter.addAndGet(1);
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
			} catch (InterruptedException e) {
				System.out.println("Caught interrupted exception.");
				System.out.print("Cause: ");
				System.out.println(e.getCause());
			}
		}
		return writeValue;
	}
}