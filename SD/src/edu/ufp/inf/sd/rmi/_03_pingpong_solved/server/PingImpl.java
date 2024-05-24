package edu.ufp.inf.sd.rmi._03_pingpong_solved.server;

import edu.ufp.inf.sd.rmi._03_pingpong_solved.client.PongRI;

public class PingImpl implements PingRI {

    @Override
    public void ping(Ball ball, PongRI pongRI){
        int player = ball.getPlayerID();

    }
}
