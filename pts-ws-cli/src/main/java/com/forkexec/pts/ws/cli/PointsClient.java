package com.forkexec.pts.ws.cli;

import java.util.Collection;
import java.util.regex.Pattern;


import com.forkexec.pts.domain.InvalidEmailException;
import com.forkexec.pts.domain.InvalidPointsException;
import com.forkexec.pts.domain.NotEnoughBalanceException;

import com.forkexec.pts.ws.*;


/**
 * Client port wrapper.
 *
 * Adds easier end point address configuration to the Port generated by
 * wsimport.
 */
public class PointsClient  {

	
	private static final Pattern PATTERN = Pattern
			.compile("^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$");
	
	private PointsFrontEnd frontend;

	/** constructor with provided web service URL */
	public PointsClient(Collection<String> wsURL) throws PointsClientException {
		frontend = new PointsFrontEnd(wsURL);	
	}

	/** constructor with provided UDDI location and name */
	public PointsClient(String uddiURL, String wsName) throws PointsClientException {
		frontend = new PointsFrontEnd(uddiURL, wsName);
	}
	public PointsFrontEnd getFrontEnd() {
		return frontend;
	}
	
	
	public void activateUser(String userEmail) throws InvalidEmailException {
		
		checkEmail(userEmail);
		frontend.cacheRead(userEmail);		
	}

	public int pointsBalance(String userEmail) throws InvalidEmailException {
		checkEmail(userEmail);
		return frontend.cacheRead(userEmail).getPoints();
	}
	
	
	public int addPoints(String userEmail, int pointsToAdd)
			throws InvalidEmailException, InvalidPointsException {
		
		checkEmail(userEmail);
		if (pointsToAdd <= 0) throw new InvalidPointsException("Points must be > 0"); 
		
		try {
			return frontend.pointsIncrement(userEmail, pointsToAdd, "+");	
		}catch (NotEnoughBalanceException e) {
			/*will never happen*/
			throw new RuntimeException();
		}
		
		
	}

	
	public int spendPoints(String userEmail, int pointsToSpend)
			throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		
		if(pointsToSpend <= 0)
			throw new InvalidPointsException("Cannot spend negative points");
		
		checkEmail(userEmail);
		
		return frontend.pointsIncrement(userEmail, pointsToSpend, "-");
	}
	
	
	
	// control operations -----------------------------------------------------

	public String ctrlPing(String inputMessage) {
		return frontend.ctrlPing(inputMessage);
	}


	public void ctrlClear() {
		frontend.ctrlClear();
	}


	public void ctrlInit(int startPoints) throws BadInitFault_Exception {
		frontend.ctrlInit(startPoints);
	}
	

	// auxiliary operations-------------------------------------------------
	public void checkEmail(String email) throws InvalidEmailException {
		if (email == null) throw new InvalidEmailException("Email cant be null");
		if (!PATTERN.matcher(email).matches()) throw new InvalidEmailException();
}

}
