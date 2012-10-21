package server;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import db.MongoMain;

import threads.Receiver;
import threads.Sender;

public class Server {
	
	public static Set<String> hosts = new HashSet<String>();
	public static Scanner scan = new Scanner(System.in);
	
	public MongoMain mongo;
	public Sender sender;
	public Receiver receiver;
	
	public Server() {
		mongo = new MongoMain("restosys");
		sender = new Sender(mongo, receiver);
		receiver = new Receiver(mongo, sender, 3333);
	}

	public static void main(String[] args) {
		Server server = new Server();
		try {
			server.sender.start();
		} catch (Exception e) {
			System.err.println("Exception " + e.getMessage());
			System.err.println("Restarting sender service...");
			server.sender.start();
		}
		server.receiver.start();
	}

}
