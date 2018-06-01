package com.vladik_company;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

/**
 * Created by vladik on 5/28/18.
 */

public class AreaScreen implements Screen {
    private Main main;
    OrthographicCamera camera;

    private SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    PlayingArea area;

    public AreaScreen(Main mc, ServerHandler serverHandler) {
        main = mc;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, main.width, main.height);
        shapeRenderer = new ShapeRenderer();
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch = new SpriteBatch();

        area = serverHandler.area;
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.90f, 0.89f, 0.89f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawGameBox(100, 0, main.width - 100, main.height - 50);
        drawFPS();
    }

    private void drawFPS() {
        batch.begin();
        BitmapFont fps = new BitmapFont();
        fps.setColor(Color.BLACK);
        fps.getData().setScale(2);
        fps.draw(batch, Float.toString(Gdx.graphics.getFramesPerSecond()), 20, 20);
        batch.end();
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

    private void drawGameBox(float x, float y, float X, float Y) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.69f, 0.66f, 0.66f, 1);
        drawSuperRect(x, y, X, Y);
        shapeRenderer.end();

        drawCells(x, y, X, Y);
        drawSnake(x, y, X, Y);
    }

    private void drawSnake(float x, float y, float X, float Y) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1);

        float indentX = (X - x) / area.widthArea / (area.widthArea + 1) * 2;
        float sizeSquare = (X - x - indentX * (area.widthArea + 1)) / area.widthArea;
        float indentY = (Y - y - sizeSquare * area.heightArea) / (area.heightArea + 1);

        ArrayList<Snake.Point> listPoint = area.firstSnake.getArrayPoint();
        for(Snake.Point i : listPoint) {
            float startX = x + i.x * (sizeSquare + indentX) + indentX;
            float startY = y + i.y * (sizeSquare + indentY) + indentY;

            drawSuperRect(startX, startY, startX + sizeSquare, startY + sizeSquare);
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 1, 1);

        listPoint = area.secondSnake.getArrayPoint();
        for(Snake.Point i : listPoint) {
            float startX = x + i.x * (sizeSquare + indentX) + indentX;
            float startY = y + i.y * (sizeSquare + indentY) + indentY;

            drawSuperRect(startX, startY, startX + sizeSquare, startY + sizeSquare);
        }

        shapeRenderer.end();
    }

    private void drawCells(float x, float y, float X, float Y) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 1);

        float indentX = (X - x) / area.widthArea / (area.widthArea + 1) * 2;
        float sizeSquare = (X - x - indentX * (area.widthArea + 1)) / area.widthArea;
        float indentY = (Y - y - sizeSquare * area.heightArea) / (area.heightArea + 1);

        for(int i = 0; i < area.widthArea; ++i) {
            for(int j = 0; j < area.heightArea; ++j) {
                float startX = x + i * (sizeSquare + indentX) + indentX;
                float startY = y + j * (sizeSquare + indentY) + indentY;

                drawSuperRect(startX - 1, startY - 1, startX + sizeSquare + 1, startY + sizeSquare + 1);
            }
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);

        for(int i = 0; i < area.widthArea; ++i) {
            for(int j = 0; j < area.heightArea; ++j) {
                if(area.area[i][j] || i == 0 || i == area.widthArea - 1 || j == 0 || j == area.heightArea - 1) {
                    continue;
                }

                float startX = x + i * (sizeSquare + indentX) + indentX;
                float startY = y + j * (sizeSquare + indentY) + indentY;

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
