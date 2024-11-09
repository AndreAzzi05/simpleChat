package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 


import java.io.IOException;

import edu.seg2105.client.common.ChatIF;
import edu.seg2105.server.ui.ServerConsole;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer{
	
  //Class variables *************************************************
  static ServerConsole serverUI;
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port, ServerConsole serverUI) {
    super(port);
    EchoServer.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client){
	  String message = msg.toString();
	  String userID = (String) client.getInfo("UserID");
	  
    System.out.println("Message received: " + msg + " from " + client);
    this.sendToAllClients(msg);
    
    if (message.startsWith("login")) {
    	if (userID == null) {
    		String loginID = message.substring(6).strip();
    		client.setInfo("UserID", loginID);
    		serverUI.display(loginID + " has logged in.");
    		
    		try {
    			client.sendToClient("Login succesful: " + loginID);
    		} catch (IOException e) {
    			serverUI.display("Error sending login info to client");
    			e.printStackTrace();
    		}
    	}else {
    			try {
    				client.sendToClient("You are logged in");
    				client.close();
    				
    			} catch (IOException e) {
    				serverUI.display("Error sending login info to client");
    				e.printStackTrace();
    			}
    		}
    	} else {
    		String formattedMessage = (userID != null ? userID : "Unknown") + message;
    		this.sendToAllClients(formattedMessage);
    	}
    }
  
  
  
    
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  protected void serverStarted()
  {
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  @Override
  protected void clientConnected(ConnectionToClient client) {
	  this.serverUI.display("A new client has connected to server");
  }
  @Override
  protected void clientDisconnected(ConnectionToClient client) {
	  this.serverUI.display("Client disconnected" + client.getInfo("UserID") + client.getId());
  }
  
private void CommandFromServerUI(String message) throws IOException {
	String command = message.substring(1);
	
	if (message.startsWith("setport")) {
		if(!this.isListening()) {
			this.setPort(Integer.parseInt(message.substring(8).strip()));
			this.serverUI.display("port set to: " + getPort());
			this.serverUI.display("Server is listening to clients on port" + getPort());
		} else {
			this.serverUI.display("Cant set port when server is on");
		}
		
	} else if (command.equals("quit")) {
		if (!this.isClosed()) {
			this.close();
			this.serverUI.display("Server has ended");
			
		} else {
			this.serverUI.display("Server has already ended");
		}
	} else if (command.equals("stop")){
		if (this.isListening()) {
			this.stopListening();
			this.serverUI.display("Server has quit");
					
		} else {
			this.serverUI.display("Server has stopped listening for clients");
		}
		
	}  else if (command.equals("close")) {
		if (!this.isClosed()) {
			this.stopListening();
			this.close();
			this.serverUI.display("server has been closed");
			
		} else {
			this.serverUI.display("Server is not listening");
		}
	} else if (command.equals("getport")) {
		this.serverUI.display("port: " + getPort());
	} else if (command.equals("start")) {
		if (!this.isListening()) {
			this.listen();
			this.serverUI.display("Server is listening for clients");
		} else {
			this.serverUI.display("server has been listening");
		}
	} else {
		this.serverUI.display("command not recognized");
	}
}
  
  
  //Class methods ***************************************************
  
  private boolean isClosed() {
	// TODO Auto-generated method stub
	return false;
}


/**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port, serverUI);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }
}
//End of EchoServer class
