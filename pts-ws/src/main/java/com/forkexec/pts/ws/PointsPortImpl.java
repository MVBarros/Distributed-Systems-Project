package com.forkexec.pts.ws;

import javax.jws.WebService;

import com.forkexec.pts.domain.Points;

/**
 * This class implements the Web Service port type (interface). The annotations
 * below "map" the Java class to the WSDL definitions.
 */
@WebService(endpointInterface = "com.forkexec.pts.ws.PointsPortType", wsdlLocation = "PointsService.wsdl", name = "PointsWebService", portName = "PointsPort", targetNamespace = "http://ws.pts.forkexec.com/", serviceName = "PointsService")
public class PointsPortImpl implements PointsPortType {

	/**
	 * The Endpoint manager controls the Web Service instance during its whole
	 * lifecycle.
	 */
	private final PointsEndpointManager endpointManager;

	/** Constructor receives a reference to the endpoint manager. */
	public PointsPortImpl(final PointsEndpointManager endpointManager) {
		this.endpointManager = endpointManager;
	}

	// Main operations -------------------------------------------------------

	@Override
	public void activateUser(final String userEmail)
			throws EmailAlreadyExistsFault_Exception, InvalidEmailFault_Exception {
		if (!Points.getInstance().checkEmail(userEmail)) {
			throwInvalidEmailFault("Mail is not valid");
		}
		if (!Points.getInstance().checkEmailExists(userEmail)) {
			throwEmailAlreadyExistsFault("That email is already being used");
		}

		Points.getInstance().addAccount(userEmail);
	}

	@Override
	public int pointsBalance(final String userEmail) throws InvalidEmailFault_Exception {
		if (!Points.getInstance().checkEmail(userEmail)) {
			throwInvalidEmailFault("Mail is not valid");
		}
		if (!Points.getInstance().checkEmailExists(userEmail))
			throwInvalidEmailFault("Mail does not pertain to any user");

		return Points.getInstance().getUserPoints(userEmail);
	}

	@Override
	public int addPoints(final String userEmail, final int pointsToAdd)
			throws InvalidEmailFault_Exception, InvalidPointsFault_Exception {
		if (pointsToAdd <= 0) {
			throwInvalidPointsFault("Cannot add negative points");
		}

		if (!Points.getInstance().checkEmail(userEmail)) {
			throwInvalidEmailFault("Mail is not valid");
		}
		if (!Points.getInstance().checkEmailExists(userEmail))
			throwInvalidEmailFault("Mail does not pertain to any user");

		return Points.getInstance().addUserPoints(userEmail, pointsToAdd);
	}

	@Override
	public int spendPoints(final String userEmail, final int pointsToSpend)
			throws InvalidEmailFault_Exception, InvalidPointsFault_Exception, NotEnoughBalanceFault_Exception {
		// TODO
		return -1;
	}

	// Control operations ----------------------------------------------------
	// TODO
	/** Diagnostic operation to check if service is running. */
	@Override
	public String ctrlPing(String inputMessage) {
		// If no input is received, return a default name.
		if (inputMessage == null || inputMessage.trim().length() == 0)
			inputMessage = "friend";

		// If the park does not have a name, return a default.
		String wsName = endpointManager.getWsName();
		if (wsName == null || wsName.trim().length() == 0)
			wsName = "Park";

		// Build a string with a message to return.
		final StringBuilder builder = new StringBuilder();
		builder.append("Hello ").append(inputMessage);
		builder.append(" from ").append(wsName);
		return builder.toString();
	}

	/** Return all variables to default values. */
	@Override
	public void ctrlClear() {
		Points.getInstance().resetPoints();
	}

	/** Set variables with specific values. */
	@Override
	public void ctrlInit(final int startPoints) throws BadInitFault_Exception {
		if (startPoints < 0)
			throwBadInit("Initial points must be >= 0");
		Points.getInstance().initPoints(startPoints);
	}

	// Exception helpers -----------------------------------------------------

	/** Helper to throw a new BadInit exception. */
	private void throwBadInit(final String message) throws BadInitFault_Exception {
		final BadInitFault faultInfo = new BadInitFault();
		faultInfo.message = message;
		throw new BadInitFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new EmailAlreadyExistsFault exception. */
	private void throwEmailAlreadyExistsFault(final String message) throws EmailAlreadyExistsFault_Exception {
		final EmailAlreadyExistsFault faultInfo = new EmailAlreadyExistsFault();
		faultInfo.message = message;
		throw new EmailAlreadyExistsFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new InvalidEmailFault exception. */
	private void throwInvalidEmailFault(final String message) throws InvalidEmailFault_Exception {
		final InvalidEmailFault faultInfo = new InvalidEmailFault();
		faultInfo.message = message;
		throw new InvalidEmailFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new NotEnoughBalanceFault exception. */
	private void throwNotEnoughBalanceFault(final String message) throws NotEnoughBalanceFault_Exception {
		final NotEnoughBalanceFault faultInfo = new NotEnoughBalanceFault();
		faultInfo.message = message;
		throw new NotEnoughBalanceFault_Exception(message, faultInfo);
	}

	/** Helper to throw a new NotEnoughBalanceFault exception. */
	private void throwInvalidPointsFault(final String message) throws InvalidPointsFault_Exception {
		final InvalidPointsFault faultInfo = new InvalidPointsFault();
		faultInfo.message = message;
		throw new InvalidPointsFault_Exception(message, faultInfo);
	}
}
