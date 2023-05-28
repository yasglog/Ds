import java.net.*; import java.rmi.*; public class AddServer {
public static void main(String args[]) { try {
AddServerImpl addServerImpl = new AddServerImpl(); Naming.rebind("AddServer", addServerImpl); System.out.println("in server side");
}
catch(Exception e) { System.out.println("Exception: " + e);
}
}
}
