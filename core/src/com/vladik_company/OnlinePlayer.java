package com.vladik_company;

import io.socket.emitter.Emitter;

/**
 * Created by vladik on 5/31/18.
 */

public class OnlinePlayer implements Player {
    ServerHandler serverHandler;
    Snake snake;

    OnlinePlayer(ServerHandler serverHandler, Snake snake) {
        this.snake = snake;
        this.serverHandler = serverHandler;
    }

    @Override
    public String getTypePlayer() {
        return "online";
    }

    @Override
    public void startGame(long startTime) {
        serverHandler.socket.on("opp move", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                snake.makeTurn(Integer.parseInt((String) args[0]));
            }

        }).on("black get", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                serverHandler.area.addBlackPoint(Integer.parseInt((String) args[0]), Integer.parseInt((String) args[1]));
            }
        });
    }
}
