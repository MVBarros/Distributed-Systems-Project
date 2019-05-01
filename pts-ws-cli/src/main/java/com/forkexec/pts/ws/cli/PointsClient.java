package com.forkexec.pts.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;
import java.util.regex.Pattern;

import javax.xml.ws.BindingProvider;

import com.forkexec.pts.domain.EmailAlreadyExistsException;
import com.forkexec.pts.domain.InvalidEmailException;
import com.forkexec.pts.domain.InvalidPointsException;
import com.forkexec.pts.domain.NotEnoughBalanceException;

import com.forkexec.pts.ws.*;

import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;

/**
 * Client port wrapper.
 *
 * Adds easier end point address configuration to the Port generated by
 * wsimport.
 */
public class PointsClient  {

	
	private static final Pattern PATTERN = Pattern
			.compile("^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z" + "A-Z]{2,7}$");
	
	
	/** WS service */
	PointsService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	PointsPortType port = null;

	/** UDDI server URL */
	private String uddiURL = null;

	/** WS name */
	private String wsName = null;

	/** WS end point address */
	private String wsURL = null; // default value is defined inside WSDL

	public String getWsURL() {
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
	public PointsClient(String wsURL) throws PointsClientException {
		this.wsURL = wsURL;
		createStub();
	}

	/** constructor with provided UDDI location and name */
	public PointsClient(String uddiURL, String wsName) throws PointsClientException {
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
			wsURL = uddiNaming.lookup(wsName);

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
		service = new PointsService();
		port = service.getPointsPort();

		if (wsURL != null) {
			if (verbose)
				System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, wsURL);
		}
	}

	// remote invocation methods ----------------------------------------------
	public Balance pointsRead(String userEmail) {
		return port.pointsRead(userEmail);
	}

	public int pointsWrite(String userEmail, int pointsVal, long seq) {
		return port.pointsWrite(userEmail, pointsVal, seq);
	}
	
	
	public void activateUser(String userEmail) throws EmailAlreadyExistsException, InvalidEmailException {
		//port.activateUser(userEmail);
		
	}

	public int pointsBalance(String userEmail) throws InvalidEmailException {
		checkEmail(userEmail);
		return pointsRead(userEmail).getPoints();
	}
	
	
	public int addPoints(String userEmail, int pointsToAdd)
			throws InvalidEmailException, InvalidPointsException {
		checkEmail(userEmail);
		if (pointsToAdd <= 0) throw new InvalidPointsException("Points must be > 0"); 
		
		Balance b = pointsRead(userEmail);
		
		return pointsWrite(userEmail, pointsToAdd, b.getTag()+1);
		
	}

	
	public int spendPoints(String userEmail, int pointsToSpend)
			throws InvalidEmailException, InvalidPointsException, NotEnoughBalanceException {
		//return port.spendPoints(userEmail, pointsToSpend);
		return 0;
	}
	
	
	
	// control operations -----------------------------------------------------

	public String ctrlPing(String inputMessage) {
		return port.ctrlPing(inputMessage);
	}


	public void ctrlClear() {
		port.ctrlClear();
	}


	public void ctrlInit(int startPoints) throws BadInitFault_Exception {
		port.ctrlInit(startPoints);
	}
	

	// auxiliary operations-------------------------------------------------
	public void checkEmail(String email) throws InvalidEmailException {
		if (email == null) throw new InvalidEmailException("Email cant be null");
		if (!PATTERN.matcher(email).matches()) throw new InvalidEmailException();
}

}
