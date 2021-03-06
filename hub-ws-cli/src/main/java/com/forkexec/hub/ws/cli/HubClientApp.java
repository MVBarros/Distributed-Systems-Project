package com.forkexec.hub.ws.cli;

import com.forkexec.pts.ws.cli.PointsFrontEndCache;

/** 
 * Client application. 
 * 
 * Looks for Hub using UDDI and arguments provided
 */
public class HubClientApp {

	public static void main(String[] args) throws Exception {
		// Check arguments.
		if (args.length == 0) {
			System.err.println("Argument(s) missing!");
			System.err.println("Usage: java " + HubClientApp.class.getName() + " wsURL OR uddiURL wsName");
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
		HubClient client = null;

		if (wsURL != null) {
			System.out.printf("Creating client for server at %s%n", wsURL);
			client = new HubClient(wsURL);
		} else if (uddiURL != null) {
			System.out.printf("Creating client using UDDI at %s for server with name %s%n", uddiURL, wsName);
			client = new HubClient(uddiURL, wsName);
		}

		// The following remote invocation is just a basic example.
		// The actual tests are made using JUnit.

		System.out.println("Invoke ping()...");
		String result = client.ctrlPing("client");
		System.out.print("Result: ");
		System.out.println(result);
		
		// Demonstracao
		
		PointsFrontEndCache.hasCache = false;
		
		if(!PointsFrontEndCache.hasCache) {
			System.out.println("A cache foi desligada para este exemplo.");
		}
		System.out.println("Comecamos com os 3 servidores de pontos ativos.");
		System.out.println("Iremos ativar uma conta com o email test@test.test");
		
		client.activateAccount("test@test.test");
		
		System.out.println("Ao ler agora os pontos vemos que tem " + client.accountBalance("test@test.test") + " pontos, demonstramos portanto o funcionamento "
				+ "normal do sistema");
		System.out.println("Iremos agora desligar o primeiro servidor de pontos.");
		
		System.in.read();
		
		System.out.println("Iremos agora adicionar 1000 pontos a conta ativada, note-se que so estão duas replicas ativas,"
				+ " no entanto o sistema consegue funcionar, e portanto resistente a (1) falha.");

		client.loadAccount("test@test.test", 10, "4024007102923926");

		
		System.out.println("Apos adicionar 1000 pontos o saldo da conta e " + client.accountBalance("test@test.test") + 
				" pontos, como esperado.");
		
		System.out.println("As duas replicas ligadas atualmente tem o saldo a 1100 e a tag a 1, por oposicao a outra replica que"
				+ " vai agora ser ligada nao tera essa conta associada.");
		
		System.out.println("Ligue agora o primeiro servidor de pontos.");
		
		System.in.read();
		
		System.out.println("Desligue agora o segundo servidor de pontos.");
		
		System.in.read();
		
		System.out.println("Iremos agora adicionar mais 1000 pontos a conta de Pontos. Observe-se"
				+ " neste momento\n um dos servidores tem a conta ativa e a outra nao no entanto"
				+ " a replica atrasada ira atualizar-se (como esperado pelo protocolo).");
		
		client.loadAccount("test@test.test", 10, "4024007102923926");
		
		System.out.println("Apos adicionar 1000 pontos o saldo da conta e " + client.accountBalance("test@test.test") + 
				" pontos, como esperado");
		
		System.out.println("Ligue agora o 2o servidor de pontos.");

		System.in.read();
		
		System.out.println("Temos agora os 3 servidores ativos, o 1º e o 3º tem a conta atualizada.");
		
		System.out.println("Ao ativar a conta temos que o 2º servidor que tem a conta desatualizada ira cria-la com a tag a zero. ");
		
		System.out.println("Como esta tag e a menor das 3 replicas ira estar desatualizada.");
		
		client.activateAccount("test@test.test");
		
		System.out.println("Desligue agora o 1o servidor.");
		
		System.in.read();
		
		System.out.println("Iremos agora adicionar mais 1000 pontos a conta de Pontos. Observe-se"
				+ " neste momento\n uma das replicas tem a conta ativa enquanto que a outra tem a conta desatualizada (tem a tag a 0) no entanto"
				+ " a replica atrasada ira atualizar-se (como esperado pelo protocolo)");
		
		client.loadAccount("test@test.test", 10, "4024007102923926");
		
		System.out.println("Ligue agora  1o servidor.");
		
		System.in.read();
		
		System.out.println("Desligue agora o 3o servidor.");
		
		System.in.read();
		
		System.out.println("Neste momento temos 2 servidores ativos, o primeiro esta desatualizado, ainda assim uma leitura ira dar o valor esperado.");
		
		System.out.println("O saldo da conta e " + client.accountBalance("test@test.test") + 
				" pontos, como esperado.");
		
		System.out.println("Ligue agora o 3o servidor.");

		System.in.read();
		
		System.out.println("Isto so e possivel acontecer porque a 2a replica atualizou-se.");
		
		PointsFrontEndCache.hasCache = true;
		
		if(PointsFrontEndCache.hasCache) {
			System.out.println("A cache foi ligada para mostrar o funcionamento normal do sistema com cache.");
		}	
		
		client.loadAccount("test@test.test", 10, "4024007102923926");
		client.loadAccount("test@test.test", 10, "4024007102923926");
		client.loadAccount("test@test.test", 10, "4024007102923926");
		
		System.out.println("A pos adicionar 3 vezes 1000 pontos o saldo e " + client.accountBalance("test@test.test") + 
				" pontos, sendo que apenas ouve uma chamada "
				+ "assincrona no 1o read efetuado pois o saldo encontra-se em cache");
		
		System.out.println("Acabou assim a demonstracao, para ver mais exemplos do sistema a funcionar com cache"
				+ " correr todos os testes (mvn verify na pasta source do projeto) com 3 servidores de pontos, 2 de restaurantes e 1 hub ligados. ");
		
		client.ctrlClear();
	}

}
