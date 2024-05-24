package uppercasereturn.client;

import uppercasereturn.server.UpperServerRI;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient implements Remote {
    private UpperServerRI server;

    public RMIClient(){}

    public void startClient() throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry("localhost",1099);
        server = (UpperServerRI) registry.lookup("UpperServer");
    }

    public String toUpperCase(String string) throws RemoteException{
        String s = server.toUpper(string);
        return s;
    }
}
