package codesquad;

import codesquad.server.SocketServer;

public class Main {

    public static void main(String[] args) {
        SocketServer socketServer = new SocketServer();
        socketServer.start();
    }

}
