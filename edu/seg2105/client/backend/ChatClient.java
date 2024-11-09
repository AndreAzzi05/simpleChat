// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String userID;

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
    this.userID = userID;
    openConnection();
  }
  
  private void handleCommandClientUI(String message) throws IOException{
	  String command = message.substring(1);
	  
	  if (command.equals("quit")) {
		  this.quit();
	  }
	  else if (command.equals("logoff")) {
		  closeConnection();
	  }
	  else if (command.equals("gethost")) {
		  this.clientUI.display("Host: " + getHost());
	  }
	  else if (command.equals("getport")) {
		  this.clientUI.display("port: " + getPort());
			  
		  }
	  else if (message.startsWith("#setport")) {
		  if (!isConnected()) {
			  this.clientUI.display("Cant set port while connected to server");
		  }
		  else {
			  this.setPort(Integer.parseInt(message.substring(8).strip()));
			  this.clientUI.display("port: " + getPort());
		  }
	  }
	  else if (message.startsWith("#sethost")) {
		  if (!isConnected()) {
			  this.clientUI.display("Cant set host while connected to server");
		  }
		  else {
			  this.setPort(Integer.parseInt(message.substring(8).strip()));
			  this.clientUI.display("Host set to: " + getHost());
		  }
		   
	  }
	  
	  else if (message.startsWith("#login")) {
		  if (!isConnected()) {
			  this.clientUI.display("We are already connected to a server");
		  }
		  else {
			  openConnection();
			  this.sendToServer(message);
		  }
	  }
	  else {
		  this.clientUI.display("Command not recognized");
	  }  
  }
  
  @Override
  protected void connectionClosed() {
	  clientUI.display("Connection closed");
  }
  
  @Override
  protected void connectionException(Exception exception) {
	  clientUI.display("Connection lost");
  }
  

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
    
    
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message)
  {
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }
  }
  
  protected void connectionEstablished() {
	  try {
		  this.sendToServer("#login " + userID);
		  
	  } catch (IOException e) {
		  e.printStackTrace();
	  }
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
}
//End of ChatClient class
