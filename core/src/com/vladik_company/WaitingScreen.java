package com.vladik_company;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by vladik on 5/31/18.
 */

class WaitingScreen implements Screen  {
    private Main main;
    OrthographicCamera camera;

    private SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    private static Socket socket;
    boolean startGame = false;
    ServerHandler serverHandler;

    Snake firstSnake, secondSnake;

    int width = 20;
    int height = 12;

    public WaitingScreen(final Main main) throws URISyntaxException {
        this.main = main;
        camera = new OrthographicCamera();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, main.width, main.height);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        socket = IO.socket("https://thawing-ridge-70423.herokuapp.com/");

        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }

        }).on("game found", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                serverHandler = new ServerHandler((String) args[0], System.currentTimeMillis(), (String) args[2], socket);
                startGame = true;
            }
        });

        socket.connect();

        socket.emit("find game");

        firstSnake = new Snake(null, 2);
        secondSnake = new Snake(null, 3);
    }

    @Override
    public void show() {
    }

    private void drawSuperRect(float x, float y, float X, float Y) {
        float r = 5;

        shapeRenderer.circle(x + r, y + r, r);
        shapeRenderer.circle(x + r, Y - r, r);
        shapeRenderer.circle(X - r, y + r, r);
        shapeRenderer.circle(X - r, Y - r, r);
        shapeRenderer.rect(x + r, y, X - x - r * 2, Y - y);
        shapeRenderer.rect(x, y + r, X - x, Y - y - r * 2);
    }

    float timeWaiting = 0;

    @Override
    public void render(float delta) {
        timeWaiting += delta;

        if(timeWaiting >= 0.5) {
            timeWaiting -= 0.5;

            makeTurn(firstSnake);
            makeTurn(secondSnake);
        }

        if(startGame) {
            main.startGame(serverHandler);
        }

        Gdx.gl.glClearColor(0.69f, 0.66f, 0.66f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();

        float indentX = main.width / width / (width + 1) * 3;
        float sizeSquare = (main.width - indentX * (width + 1)) / width;
        float indentY = (main.height - sizeSquare * height) / (height + 1);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.90f, 0.89f, 0.89f, 1);
        drawSuperRect(indentX * 2 + sizeSquare, indentY * 2 + sizeSquare, main.width - (indentX * 2 + sizeSquare), main.height - (indentY * 2 + sizeSquare));
        shapeRenderer.end();

        drawCells();
        drawSnake();

//        BitmapFont font;
//        String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";
//        font = TrueTypeFont.createBitmapFont(Gdx.files.internal("font.ttf"), FONT_CHARACTERS, 12.5f, 7.5f, 1.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    private void makeTurn(Snake snake) {
        int x = snake.getArrayPoint().get(0).x;
        int y = snake.getArrayPoint().get(0).y;

        if(x == 0 && y == 0) {
            snake.setDeltaDirect(-1);
        }
        if(x == 0 && y == height - 1) {
            snake.setDeltaDirect(-1);
        }
        if(x == width - 1 && y == 0) {
            snake.setDeltaDirect(-1);
        }
        if(x == width - 1 && y == height - 1) {
            snake.setDeltaDirect(-1);
        }

        snake.makeTurn();
    }

    private void drawSnake() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1);

        float indentX = main.width / width / (width + 1) * 3;
        float sizeSquare = (main.width - indentX * (width + 1)) / width;
        float indentY = (main.height - sizeSquare * height) / (height + 1);

        ArrayList<Snake.Point> listPoint = firstSnake.getArrayPoint();
        for(Snake.Point i : listPoint) {
            float startX = i.x * (sizeSquare + indentX) + indentX;
            float startY = i.y * (sizeSquare + indentY) + indentY;

            drawSuperRect(startX, startY, startX + sizeSquare, startY + sizeSquare);
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 1, 1);

        listPoint = secondSnake.getArrayPoint();
        for(Snake.Point i : listPoint) {
            float startX = i.x * (sizeSquare + indentX) + indentX;
            float startY = i.y * (sizeSquare + indentY) + indentY;

            drawSuperRect(startX, startY, startX + sizeSquare, startY + sizeSquare);
        }

        shapeRenderer.end();
    }

    private void drawCells() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);

        float indentX = main.width / width / (width + 1) * 3;
        float sizeSquare = (main.width - indentX * (width + 1)) / width;
        float indentY = (main.height - sizeSquare * height) / (height + 1);

        for(int i = 0; i < width; ++i) {
            for(int j = 0; j < height; ++j) {
                if(!(i == 0 || i == width - 1 || j == 0 || j == height - 1)) {
                    continue;
                }

                float startX = i * (sizeSquare + indentX) + indentX;
                float startY = j * (sizeSquare + indentY) + indentY;

                drawSuperRect(startX - 1, startY - 1, startX + sizeSquare + 1, startY + sizeSquare + 1);
            }
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);

        for(int i = 0; i < width; ++i) {
            for(int j = 0; j < height; ++j) {
                if(!(i == 0 || i == width - 1 || j == 0 || j == height - 1)) {
                    continue;
                }

                float startX = i * (sizeSquare + indentX) + indentX;
                float startY = j * (sizeSquare + indentY) + indentY;

                drawSuperRect(startX, startY, startX + sizeSquare, startY + sizeSquare);
            }
        }

        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
