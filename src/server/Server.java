package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.Character.Subset;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Server {

	private static Set<String> hosts = new HashSet<String>();
	
	public static void displayHelp() {
		System.out.println("--------------");
		System.out.println("Available commands:");
		System.out.println("--quit : Shut down the server");
		System.out.println("--lkh : List known hosts");
		System.out.println("--help : Display this message");
		System.out.println("---------------");
	}
	
	public static void displayKnowHosts() {
		System.out.println("--------------");
		System.out.println("Known hosts:");
		for (String host : hosts) {
			System.out.println("\t" + host);
		}
		System.out.println("--------------");
	}

	public Thread sender = new Thread(new Runnable() {

		public void run() {
			int clientPort = 4343;

			Scanner scan = new Scanner(System.in);
			while (true) {
				// Broadcasting to all known hosts
				for (String adr : hosts) {
					try {
						Socket client = new Socket(adr, clientPort);
						DataOutputStream out = new DataOutputStream(client.getOutputStream());
						String mess = scan.nextLine();
						if (mess.equals("--quit")) {
							client.close();
							System.out.println("Disconnected");
							return;
						} else if (mess.equals("--lkh")) {
							displayKnowHosts();
						} else if (mess.equals("--help")) {
							displayHelp();
						}
						out.writeBytes(mess + "\n");
						out.close();
					} catch (ConnectException e) {
						hosts.remove(adr);
						System.err.println("Connection error, host " + adr + " is out of known hosts");
					} catch (UnknownHostException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}
	});

	public Thread reveiver = new Thread(new Runnable() {

		public void run() {
			try {
				ServerSocket socket = new ServerSocket(3434);
				System.out.println("Server started...");
				while (true) {
					Socket acceptSocket = socket.accept();

					String hostIp = acceptSocket.getRemoteSocketAddress().toString().split(":")[0].substring(1);


					BufferedReader in = new BufferedReader(new InputStreamReader(acceptSocket.getInputStream()));
					String command = in.readLine();
					if(command.equals("device-checking-in")) {
						System.out.println("Host " + hostIp	+ " connected, adding to known hosts...");
						hosts.add(hostIp);
					} else if (command.equals("device-checking-out")) {
						System.out.println("Host " + hostIp + " is out");
						hosts.remove(hostIp);
					} else {
						System.out.println("Server Received from " + hostIp + ": " + command);
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	});

	public static void main(String[] args) {
		Server server = new Server();
		try {
			server.sender.start();
		} catch (Exception e) {
			System.err.println("Exception " + e.getMessage());
			System.err.println("Restarting sender service...");
			server.sender.start();
		}
		server.reveiver.start();
	}

}
