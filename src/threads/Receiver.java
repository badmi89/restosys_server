package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import db.MongoMain;
import db.tables.User;

import server.Server;
import utils.Key;

public class Receiver extends Thread {

	private int serverPort;
	private MongoMain mongo;
	private Sender sender;

	public Receiver(MongoMain mongo, Sender sender, int port) {
		this.mongo = mongo;
		this.sender = sender;
		serverPort = port;
	}

	@Override
	public void run() {

		try {
			ServerSocket socket = new ServerSocket(serverPort);
			System.out.println("Server started...");
			while (true) {
				Socket acceptSocket = socket.accept();

				String hostIp = acceptSocket.getRemoteSocketAddress()
						.toString().split(":")[0].substring(1);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						acceptSocket.getInputStream()));
				String received = in.readLine();

				try {
					String command = received.split(":@:")[0];
					String value = received.split(":@:")[1];

					if (command.equals(Key.HOST_CHECKIN)) {
						System.out.println("Host " + hostIp
								+ " connected, adding to known hosts...");
						Server.hosts.add(hostIp);
					} else if (command.equals(Key.HOST_CHECKOUT)) {
						System.out.println("Host " + hostIp + " is out");
						Server.hosts.remove(hostIp);
					} else if (command.equals(Key.REQUEST_LOGIN)) {
						User user = mongo.getUserByPasscode(value);
						if (user != null) {
							String response = Key.SERVER_LOGIN_APPROVE;
							String name = user.getFirstname() + " "
									+ user.getLastname();
							sender.send(response + ":@:" + name, hostIp);
						} else {
							sender.send(Key.SERVER_LOGIN_REJECTED
									+ ":@:User doesn't exist", hostIp);
						}
					}

					else {
						if (!Server.hosts.contains(hostIp)) {
							Server.hosts.add(hostIp);
							System.out.println("Host " + hostIp
									+ " connected, adding to known hosts...");
						}
						System.out.println("Server Received from " + hostIp
								+ ": " + command);
					}
				} catch (IndexOutOfBoundsException e) {
					System.out.println("Ignoring bad format...");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
