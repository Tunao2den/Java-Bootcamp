package edu.ufp.inf.sd.rmi.diglib.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class DigLibSessionImpl extends UnicastRemoteObject implements DigLibSessionRI {
    DigLibFactoryImpl digLibFactory;
    String user;
    protected DigLibSessionImpl(DigLibFactoryImpl digLibFactoryImpl, String user) throws RemoteException {
        super();
        this.digLibFactory=digLibFactoryImpl;
        this.user=user;
    }


    @Override
    public Book[] search(String title, String author) {
        Book[] books = digLibFactory.getDbMockup().select(title, author);

        return books;
    }

    @Override
    public void logout() {
        digLibFactory.hashMap.remove(user);
    }
}
