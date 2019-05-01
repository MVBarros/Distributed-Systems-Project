package com.forkexec.hub.ws;

import java.util.Collection;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import com.forkexec.pts.ws.cli.PointsClient;

public class HubFrontEnd {
	private Collection<PointsClient> clients;
	private int quorumSize;
	
	public HubFrontEnd(Collection<PointsClient> clients) {
		this.clients = clients;
		this.quorumSize = clients.size() / 2 + 1;
	}
	
	public int pointsReadAsync(String email) {
		for (PointsClient client: clients) {
			client.pointsReadAsync(email, new AsyncHandler<PointsReadResponse>() {
				@Override
		        public void handleResponse(Response<PointsReadResponse> response) {
					
				}
				
		});
			
		
		
		}
	}
	
	
}