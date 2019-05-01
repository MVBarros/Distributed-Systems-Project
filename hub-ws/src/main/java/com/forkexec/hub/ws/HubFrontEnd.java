package com.forkexec.hub.ws;

import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import com.forkexec.pts.ws.Balance;
import com.forkexec.pts.ws.PointsReadResponse;
import com.forkexec.pts.ws.cli.PointsClient;

public class HubFrontEnd {
	private Collection<PointsClient> clients;
	private int quorumSize;
	
	public HubFrontEnd(Collection<PointsClient> clients) {
		this.clients = clients;
		this.quorumSize = clients.size() / 2 + 1;
	}
	
	public Balance pointsReadAsync(String email) {
		AtomicInteger respostasRecebidas = new AtomicInteger();
		respostasRecebidas.set(0);
		
		Balance bestReturn = new Balance();
		bestReturn.setTag((long) -1);
		
		for (PointsClient client: clients) {
			
			client.pointsReadAsync(email, new AsyncHandler<PointsReadResponse>() {
				@Override
		        public void handleResponse(Response<PointsReadResponse> response) {
					try {
						respostasRecebidas.incrementAndGet();
						if (bestReturn.getTag() < response.get().getReturn().getTag()) {
							bestReturn.setPoints(response.get().getReturn().getPoints());
							bestReturn.setTag(response.get().getReturn().getTag());
							}
						
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
			
			 while (respostasRecebidas.get() < quorumSize) {
	                try {
						Thread.sleep(10 /* milliseconds */);
					} catch (InterruptedException e) {
						System.out.println("Caught interrupted exception.");
                        System.out.print("Cause: ");
                        System.out.println(e.getCause());
					}
			 }}
		return bestReturn;
	}
	
	
}