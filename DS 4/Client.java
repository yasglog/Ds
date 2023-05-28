/*Implement Berkeley algorithm for clock 
synchronization.
Client.java
*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.List;
import java.util.Random;
import java.util.Vector;
public class Client {
 private static final int PORT = 8080;
 public static void main(String[] args) {
 Random random = new Random();
 float client_local_clock = random.nextInt(10);
 System.out.println("Client starts. Client pid is " + ProcessHandle.current().pid());
 System.out.println("Client local clock is " + client_local_clock + "\n");
 try {
 InetAddress serverAddress = InetAddress.getByName("127.0.0.1");
 Socket clientSocket = new Socket(serverAddress, PORT);
 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
 BufferedReader in = new BufferedReader(new 
InputStreamReader(clientSocket.getInputStream()));
 System.out.println("Client: connected server(" + serverAddress.getHostAddress() + ":" + 
PORT + ").\n");
 // First round communication
 // receiving from server
 String serverMsg = in.readLine();
 System.out.println("Client: read: '" + serverMsg + "'");
 // reply according to what client receives
 if (serverMsg.equals("Hello from server, please tell me your local clock value.")) {
 // prepare message
 String msg = "Hello from client, my local clock value is " + client_local_clock;
 // sending a message to server
 out.println(msg);
 System.out.println("Client: sent message: '" + msg + "'");
 }
 // Second round communication
 // receiving from server
 serverMsg = in.readLine();
 System.out.println("Client: read: '" + serverMsg + "'");
 if (serverMsg.contains("From server, your clock adjustment offset is")) {
 List<String> splitStr = split(serverMsg, " ");
 String substr_after_lastbutone_space = splitStr.get(splitStr.size() - 2);
 String substr_after_last_space = splitStr.get(splitStr.size() - 1);
 System.out.println("Client: received local clock adjustment offset (string) is " + 
substr_after_lastbutone_space + " " + substr_after_last_space);
 float substr_after_last_space_f = Float.parseFloat(substr_after_last_space);
 System.out.println("Client: received local clock adjustment offset (float) is " + 
substr_after_lastbutone_space + " " + substr_after_last_space_f);
 if (substr_after_lastbutone_space.equals("add")) {
 client_local_clock += substr_after_last_space_f;
 } else if (substr_after_lastbutone_space.equals("minus")) {
 client_local_clock -= substr_after_last_space_f;
 }
 System.out.println("Client local clock is " + client_local_clock + "\n");
 }
 clientSocket.close();
 } catch (Exception e) {
 e.printStackTrace();
 }
 }
 private static List<String> split(String s, String delimiter) {
 String[] tokens = s.split(delimiter);
 List<String> res = new Vector<>();
 for (String token : tokens) {
 res.add(token.trim());
 }
 return res;
 }
}
