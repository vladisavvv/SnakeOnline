package com.vladik_company;

import io.socket.client.Socket;

/**
 * Created by vladik on 5/29/18.
 */

public class ServerHandler {
    private Player firstPlayer;
    private Player secondPlayer;
    public Socket socket;

    PlayingArea area;

    ServerHandler(String tokenOnlinePlayr, final long startTime, String colorLocalPlayer, Socket socket) {
        this.socket = socket;
        area = new PlayingArea();

        if(colorLocalPlayer.equals("red")) {
            firstPlayer = new LocalPlayer(this, area.firstSnake, tokenOnlinePlayr);
            secondPlayer = new OnlinePlayer(this, area.secondSnake);

            firstPlayer.startGame(startTime);
            secondPlayer.startGame(startTime);
        } else {
            secondPlayer = new LocalPlayer(this, area.secondSnake, tokenOnlinePlayr);
            firstPlayer = new OnlinePlayer(this, area.firstSnake);

            firstPlayer.startGame(startTime);
            secondPlayer.startGame(startTime);
        }

    }
}
