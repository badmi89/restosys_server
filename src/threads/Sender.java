package threads;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import db.MongoMain;
import db.tables.User;

import server.Server;

public class Sender extends Thread {
	
	public MongoMain mongo = new MongoMain("restosys");
	public Receiver receiver;
	int clientPort = 4343;
	
	public Sender(MongoMain mongo, Receiver receiver) {
		this.mongo = mongo;
		this.receiver = receiver;
	}
	
	public void send(String message, String clientAddress) {
		Socket client;
		try {
			client = new Socket(clientAddress, clientPort);
			DataOutputStream out = new DataOutputStream(client.getOutputStream());
			out.writeBytes(message + "\n");
			out.close();
			System.out.println("Sent to " + clientAddress + ":" + clientPort + ": " + message);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {

		Scanner scan = new Scanner(System.in);
		while (true) {
			// Broadcasting to all known hosts
			for (String adr : Server.hosts) {
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
					} else if (mess.contains("--gun ")) {
						String passcode = mess.split(" ")[1];
						User user = mongo.getUserByPasscode(passcode);
						if(user!=null)
							System.out.println(user.getFirstname() + " " + user.getLastname());
						else 
							System.out.println("User " + passcode + " don't exist");
					
					} else {
						out.writeBytes(mess + "\n");
						out.close();
					}
				} catch (ConnectException e) {
					Server.hosts.remove(adr);
					System.err.println("Connection error, host " + adr + " is out of known hosts");
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public static void displayHelp() {
		System.out.println("--------------");
		System.out.println("Available commands:");
		System.out.println("--quit : Shut down the server");
		System.out.println("--lkh : List known hosts");
		System.out.println("--help : Display this message");
		System.out.println("--gun <passcode> : Show users full name for given passcode");
		System.out.println("---------------");
	}
	
	public static void displayKnowHosts() {
		System.out.println("--------------");
		System.out.println("Known hosts:");
		for (String host : Server.hosts) {
			System.out.println("\t" + host);
		}
		System.out.println("--------------");
	}

}
