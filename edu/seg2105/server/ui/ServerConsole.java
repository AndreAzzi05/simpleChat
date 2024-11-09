package edu.seg2105.server.ui;

import java.io.*;
import java.util.Scanner;

import edu.seg2105.client.backend.ChatClient;
import edu.seg2105.client.common.*;
import edu.seg2105.edu.server.backend.EchoServer;

public class ServerConsole {
	
	EchoServer server;
	
	Scanner Entrypoint;
	
	
	final public static int DEFAULT_PORT = 5555;
	
	
	public ServerConsole(int port) {
		try {
			this.server = new EchoServer(port, this);
		} catch (Exception e) {
			System.out.println("Cant make server connection");
			System.exit(1);
		}
		Entrypoint = new Scanner(System.in);
	}
	
	public void display(String message) {
		System.out.println("message from the server" + message);
		
	}
	
	public void accept() {
		try {
			String message;
			
			while (true) {
				message = Entrypoint.nextLine();
				server.handleMessageFromClient(message, null);
			}
		} catch (Exception e) {
			System.out.println("Cant read from the console" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		int port;
		
		try {
			port = Integer.parseInt(args[0]);
		} catch (Exception ex ) {
			port = DEFAULT_PORT;
			System.out.println("You didnt specify a port." + DEFAULT_PORT);
		}
		
		ServerConsole form = new ServerConsole(port);
		
		try {
			form.server.listen();
			form.accept();
		} catch (Exception ex) {
			System.out.println("Cant listen to client");
			ex.printStackTrace();
		}
		
		
	}

}
