package edu.ufp.inf.sd.rmi._03_pingpong_solved.server;

import edu.ufp.inf.sd.rmi._03_pingpong_solved.client.PongRI;

import java.rmi.Remote;

public interface PingRI extends Remote {
    void ping(Ball ball, PongRI pongRI);
}
