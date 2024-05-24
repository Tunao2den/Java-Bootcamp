package edu.ufp.inf.sd.rmi.observer.server;

import edu.ufp.inf.sd.rmi.observer.client.ObserverGuiClient;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ObserverImpl extends UnicastRemoteObject implements ObserverRI {

    private State observerState;

    public SubjectRI subjectRI;


    public ObserverImpl() throws RemoteException {
        super();
    }

    public ObserverImpl(String username, ObserverGuiClient observerGuiClient, SubjectRI subjectRI) throws RemoteException {
        super();
    }

    public State getLastObserverState(){
        return observerState;
    }



    public void update(){

    }

}
