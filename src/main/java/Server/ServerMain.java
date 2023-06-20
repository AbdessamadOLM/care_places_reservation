package Server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class ServerMain {
    public static void main(String[] args) {
        try {
            BusResServiceImp busresrve = new BusResServiceImp();
            LocateRegistry.createRegistry(52000);
            Naming.rebind("rmi://localhost:52000/echo",busresrve);
            System.out.println("server est en écoute");
        } catch (RemoteException | MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
