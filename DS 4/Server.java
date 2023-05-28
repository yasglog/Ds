//Server.java


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
public class Server {
 private static final int PORT = 8080;
 public static void main(String[] args) {
 List<Float> clientsLocalClocks = new ArrayList<>();
 float serverLocalClock = (float) (Math.random() * 10); // range from 0 to 9
 System.out.println("Server starts. Server pid is " + ProcessHandle.current().pid());
 System.out.println("Server local clock is " + serverLocalClock + "\n");
 try {
 // Create a server socket
 ServerSocket serverSocket = new ServerSocket(PORT);
 System.out.println("Server: server is listening ...");
 System.out.println("\nYou can open one or multiple new terminal windows now to run ./client");
 int clientsCounter = 0;
 boolean isEnoughClients = false;
 List<Socket> clientSockets = new ArrayList<>();
 List<String> clientIPs = new ArrayList<>();
 List<Integer> clientPorts = new ArrayList<>();
 while (!isEnoughClients) {
 // Accept client connections
 Socket clientSocket = serverSocket.accept();
 clientsCounter++;
 System.out.println("\nYou have connected " + clientsCounter + " client(s) now.");
 // Get client IP and port
 String clientIP = clientSocket.getInetAddress().getHostAddress();
 int clientPort = clientSocket.getPort();
 System.out.println("Server: new client accepted. client ip and port: " + clientIP + ":" + 
clientPort);
 // Store client connection
 clientSockets.add(clientSocket);
 clientIPs.add(clientIP);
 clientPorts.add(clientPort);
 System.out.println("Current connected clients amount is " + clientSockets.size());
 System.out.print("Do you have enough clients? (please input '1' for yes, '0' for no): ");
 BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
 String input = reader.readLine();
 isEnoughClients = input.equals("1");
 if (!isEnoughClients) {
 System.out.println("OK. Please continue opening one or multiple new terminal windows to run ./client");
 } 
 else if (!input.equals("0")) {
 System.out.println("Unrecognized input has been considered as 0. You can create one more client.\n");
 isEnoughClients = false;
 }
 }
 System.out.println("\nClients creation finished! There are a total of " + clientSockets.size() + "connected clients.");
 System.out.println("Asking all clients to report their local clock value ...\n\n\n");
 // Create a BufferedReader to read messages from clients
 BufferedReader[] clientReaders = new BufferedReader[clientSockets.size()];
 for (int i = 0; i < clientSockets.size(); i++) {
 Socket clientSocket = clientSockets.get(i);
 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
 BufferedReader in = new BufferedReader(new 
InputStreamReader(clientSocket.getInputStream()));
 clientReaders[i] = in;
 // Send a message to client
 out.println("Hello from server, please tell me your local clock value.");
 System.out.println("Server: sent to client(" + clientIPs.get(i) + ":" + clientPorts.get(i) + "Hello from server, please tell me your local clock value.'");
 }
 String[] recvBuf = new String[65536];
 boolean[] receivedClockValue = new boolean[clientSockets.size()];
 while (true) {
 boolean allReceived = true;
 for (int i =0; i < clientSockets.size(); i++) {
 Socket clientSocket = clientSockets.get(i);
 BufferedReader in = clientReaders[i];
 if (!receivedClockValue[i]) {
 // Check if there is a message from the client
 if (in.ready()) {
 // Read the message from the client
 recvBuf[i] = in.readLine();
 System.out.println("Server: recv from client(" + clientIPs.get(i) + ":" + 
clientPorts.get(i) + "): '" + recvBuf[i] + "'");
 
 // Extract the clock value from the received message
 if (recvBuf[i].startsWith("Hello from client, my local clock value is")) {
 String[] splitStr = recvBuf[i].split(" ");
 String substrAfterLastSpace = splitStr[splitStr.length - 1];
 System.out.println("Server: received client local clock (string) is " + 
substrAfterLastSpace);
 float substrAfterLastSpaceF = Float.parseFloat(substrAfterLastSpace);
 System.out.println("Server: received client local clock (float) is " + 
substrAfterLastSpaceF);
 clientsLocalClocks.add(substrAfterLastSpaceF);
 receivedClockValue[i] = true;
 }
 } else {
 allReceived = false;
 }
 }
 }
 if (allReceived) {
 break;
 }
 }
 System.out.println("\n\n");
 // Calculate average clock value
 float allClockSum = serverLocalClock;
 for (float clockValue : clientsLocalClocks) {
 allClockSum += clockValue;
 }
 float avgClock = allClockSum / (clientSockets.size() + 1);
 // Send clock adjustment messages to clients
 for (int i = 0; i < clientSockets.size(); i++) {
 Socket clientSocket = clientSockets.get(i);
 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
 // Calculate offset and operation
 float offset = clientsLocalClocks.get(i) - avgClock;
 String operation;
 if (offset >= 0) {
 operation = "minus";
 } else {
 operation = "add";
 offset = 0 - offset;
 }
 // Prepare message
 String msgStr = "From server, your clock adjustment offset is " + operation + " " + offset;
 out.println(msgStr);
 System.out.println("Server: sent to client (" + clientIPs.get(i) + ":" + clientPorts.get(i) + "):" + msgStr + "'");
 }
 // Adjust server's clock
 serverLocalClock += avgClock - serverLocalClock;
 System.out.println("\n\nServer new local clock is " + serverLocalClock + "\n\n");
 System.out.println("Server: server stopped.");
 serverSocket.close();
 } catch (IOException e) {
 e.printStackTrace();
 }
 }
}
