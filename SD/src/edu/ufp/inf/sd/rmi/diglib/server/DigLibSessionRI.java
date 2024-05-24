package edu.ufp.inf.sd.rmi.diglib.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * <p>Title: Projecto SD</p>
 * <p>Description: Projecto apoio aulas SD</p>
 * <p>Copyright: Copyright (c) 2009</p>
 * <p>Company: UFP </p>
 * @author Rui Moreira
 * @version 1.0
 */
public interface DigLibSessionRI extends Remote {
    /**
     *
     * @param title, author
     * @return result of specific book with respect to title and author
     */
    public Book[] search(String title, String author);

    /**
     * @return void
     */
    void logout();
}
