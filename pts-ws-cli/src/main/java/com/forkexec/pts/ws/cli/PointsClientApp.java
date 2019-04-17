package com.forkexec.pts.ws.cli;

import java.util.concurrent.ExecutionException;

import javax.xml.ws.AsyncHandler;
import javax.xml.ws.Response;

import com.forkexec.pts.ws.PointsBalanceResponse;

/** 
 * Client application. 
 * 
 * Looks for Points using UDDI and arguments provided
 */
public class PointsClientApp {

	static boolean received = false;

	public static void main(String[] args) throws Exception {
		// Check arguments.
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + PointsClientApp.class.getName() + " wsURL OR uddiURL wsName");
			return;
		}
		String uddiURL = null;
		String wsName = null;
		String wsURL = null;
		if (args.length == 1) {
			wsURL = args[0];
		} else if (args.length >= 2) {
			uddiURL = args[0];
			wsName = args[1];
		}

		// Create client.
		PointsClient client = null;

		if (wsURL != null) {
			System.out.printf("Creating client for server at %s%n", wsURL);
			client = new PointsClient(wsURL);
		} else if (uddiURL != null) {
			System.out.printf("Creating client using UDDI at %s for server with name %s%n", uddiURL, wsName);
			client = new PointsClient(uddiURL, wsName);
		}

		// The following remote invocation is just a basic example.
		// The actual tests are made using JUnit.

		System.out.println("Invoke ping()...");
		String result = client.ctrlPing("client");
		System.out.print("Result: ");
		System.out.println(result);
		
		String userEmail = "user@example.com";
	    client.activateUser(userEmail);
	    // asynchronous call with polling
	    Response<PointsBalanceResponse> response = client.pointsBalanceAsync(userEmail);

	    while (!response.isDone()) {
	        Thread.sleep(10 /* milliseconds */);
	        System.out.print(".");
			System.out.flush();
	    }

	    try {
	        System.out.println("(Polling) asynchronous call result: " + response.get().getReturn());
	    } catch (ExecutionException e) {
	        System.out.println("Caught execution exception.");
	        System.out.print("Cause: ");
	        System.out.println(e.getCause());
	    }
	    
	    client.addPoints(userEmail, 1100);
	    
	    // asynchronous call with callback
        client.pointsBalanceAsync(userEmail, new AsyncHandler<PointsBalanceResponse>() {
            @Override
            public void handleResponse(Response<PointsBalanceResponse> response) {
                try {
                    System.out.println();
                    System.out.print("(Callback) Asynchronous call result arrived: ");
                    System.out.println(response.get().getReturn());
                    received = true;
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

        while (!received) {
            Thread.sleep(10 /* milliseconds */);
            System.out.print(".");
            System.out.flush();
        }
	    
	}

}
