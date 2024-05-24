package edu.ufp.inf.sd.rmi.diglib.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: UFP </p>
 * @author Rui Moreira
 * @version 1.0
 */
public interface DigLibFactoryRI extends Remote {
    /**
     *
     * @param username and pwd
     * @return result of adding all list elements
     * @throws RemoteException
     */
    boolean register(String username, String pwd) throws RemoteException;

    /**
     * @param user, pwd
     */
    DigLibSessionRI login(String user, String pwd) throws RemoteException;
}
