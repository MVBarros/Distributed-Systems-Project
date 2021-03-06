package com.forkexec.cc.ws.cli;

import static javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY;

import java.util.Map;

import javax.xml.ws.BindingProvider;

import  pt.ulisboa.tecnico.sdis.ws.*;


//import pt.ulisboa.tecnico.sdis.ws.uddi.UDDINaming;



public class CCClient implements CreditCard {
	
	/** WS service */
	CreditCardImplService service = null;

	/** WS port (port type is the interface, port is the implementation) */
	CreditCard port = null;



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
	public CCClient(String wsURL) {
		this.wsURL = wsURL;
		createStub();
	}
	
	/** constructor with provided web service URL */
	public CCClient() {
		this.wsURL = "http://ws.sd.rnl.tecnico.ulisboa.pt:8080/cc";
		createStub();
	}

	
	/** Stub creation and configuration */
	private void createStub() {
		if (verbose)
			System.out.println("Creating stub ...");
		
		service = new CreditCardImplService();
		port = service.getCreditCardImplPort();


		if (wsURL != null) {
			if (verbose)
				System.out.println("Setting endpoint address ...");
			BindingProvider bindingProvider = (BindingProvider) port;
			Map<String, Object> requestContext = bindingProvider.getRequestContext();
			requestContext.put(ENDPOINT_ADDRESS_PROPERTY, wsURL);
		}
	}

	@Override
	public String ping(String name) {
		return port.ping(name);
	}

	@Override
	public boolean validateNumber(String numberAsString) {
		return port.validateNumber(numberAsString);
	}
	
}