package com.forkexec.hub.ws;

import java.util.Collection;

import com.forkexec.pts.ws.cli.PointsClient;

public class HubFrontEnd {
	private Collection<PointsClient> clients;
	private int quorumSize;
	
	public HubFrontEnd(Collection<PointsClient> clients) {
		this.clients = clients;
		this.quorumSize = clients.size() / 2 + 1;
	}
	
	
}