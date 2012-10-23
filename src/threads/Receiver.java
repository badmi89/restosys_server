package threads;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

import db.MongoMain;
import db.tables.User;

import server.Server;
import sun.org.mozilla.javascript.json.JsonParser;
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

				String hostIp = acceptSocket.getRemoteSocketAddress().toString().split(":")[0].substring(1);

				BufferedReader in = new BufferedReader(new InputStreamReader(acceptSocket.getInputStream()));
				String received = in.readLine();
				
				System.out.println("JSON Received: " + received);
				
				JSONObject json = (JSONObject) JSONValue.parse(received);
				
				for (Object key : json.keySet()) {
					if(key.equals(Key.REQUEST_LOGIN)) {
						handleLoginRequest(json.get(key).toString(), hostIp);
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void handleLoginRequest(String value, String hostIp){
		BasicDBObject user = mongo.getUserByPasscode(value);
		JSONObject response = new JSONObject();
		if (user != null) {
			response.put("ACTION", Key.SERVER_LOGIN_APPROVE);
			response.put("SESSIONID", UUID.randomUUID().toString());
			response.put("USER", user);
			response.put("BILLS", mongo.getUserBills(user.getString("id")));
		} else {
			response.put("ACTION", Key.SERVER_LOGIN_REJECTED);
		}
		sender.send(response.toJSONString(), hostIp);
	}

}
