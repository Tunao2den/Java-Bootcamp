package edu.ufp.inf.sd.rmi.diglib.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DigLibFactoryImpl extends UnicastRemoteObject implements DigLibFactoryRI {

    HashMap<String, DigLibSessionRI> hashMap;

    DBMockup dbMockup = new DBMockup();

    protected DigLibFactoryImpl() throws RemoteException {
        super();
        hashMap=new HashMap<>();
    }

    @Override
    public boolean register(String username, String pwd) throws RemoteException {
        dbMockup.register(username, pwd);

        return dbMockup.exists(username, pwd);
    }

    @Override
    public DigLibSessionRI login(String user, String pwd) throws RemoteException {

        if (dbMockup.exists(user, pwd)){
            DigLibSessionRI digLibSessionRI;

            if (hashMap.containsKey(user)){
                return hashMap.get(user);
            }

            digLibSessionRI = new DigLibSessionImpl(this, user);
            hashMap.put(user,digLibSessionRI);
            return digLibSessionRI;

        } else { return  null; }
    }

    public DBMockup getDbMockup() {
        return dbMockup;
    }


}
