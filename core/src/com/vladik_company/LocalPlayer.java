package com.vladik_company;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

/**
 * Created by vladik on 5/29/18.
 */

public class LocalPlayer implements Player {
    private static Snake snake;
    ServerHandler serverHandler;
    String otherPlayerToken;

    public LocalPlayer(ServerHandler serverHandler, final Snake snake, String otherPlayerToken) {
        this.serverHandler = serverHandler;
        this.otherPlayerToken = otherPlayerToken;
        this.snake = snake;

        Gdx.input.setInputProcessor(new InputAdapter() {
            public boolean touchUp(int x,int y,int pointer,int button){
                OrthographicCamera camera = new OrthographicCamera();
                camera.setToOrtho(false, Main.width, Main.height);

                Vector3 pos = camera.unproject(new Vector3(x, y, 0));
                synchronized (snake) {
                    if(pos.x <= Main.width / 2) {
                        snake.setDeltaDirect(1);
                    } else {
                        snake.setDeltaDirect(-1);
                    }
                }

                return true;
            }
        });
    }

    @Override
    public void startGame(final long startTime) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                long lastTime = 0;
                long lastLastTime = 0;

                while(true) {
                    long curTime = System.currentTimeMillis();
                    if((curTime - startTime) / 750 > lastTime) {
                        lastTime = (curTime - startTime) / 750;
                        serverHandler.socket.emit("move", otherPlayerToken, (Integer.valueOf(snake.makeTurn())).toString());
                    }

                    if(snake.type == 0 && lastTime % 2 == 0 && lastLastTime != lastTime) {
                        lastLastTime = lastTime;
                        int x = (new Random()).nextInt(serverHandler.area.widthArea);
                        int y = (new Random()).nextInt(serverHandler.area.heightArea);

                        serverHandler.area.addBlackPoint(x, y);
                        serverHandler.socket.emit("black set", otherPlayerToken, (Integer.valueOf(x)).toString(), (Integer.valueOf(y)).toString());
                    }
                }
            }
        })).start();
    }

    @Override
    public String getTypePlayer() {
        return "local";
    }
}
