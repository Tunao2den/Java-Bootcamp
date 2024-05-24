package edu.ufp.inf.sd.rmi.observer.server;

import java.rmi.RemoteException;

public interface SubjectRI {
    void attach();

    void setState(State s) throws RemoteException;
}
