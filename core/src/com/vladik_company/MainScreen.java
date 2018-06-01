package com.vladik_company;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.ArrayList;

/**
 * Created by vladik on 6/1/18.
 */

public class MainScreen implements Screen {
    OrthographicCamera camera;

    int widthArea = 20;
    int heightArea = 12;

    private SpriteBatch batch;
    ShapeRenderer shapeRenderer;

    Snake firstSnake, secondSnake;

    Main main;

    Stage stage;

    public MainScreen(Main main) {
        this.main = main;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, main.width, main.height);
        shapeRenderer = new ShapeRenderer();
        camera.update();
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch = new SpriteBatch();
//        batch.setProjectionMatrix(camera.combined);

        firstSnake = new Snake(null, 0);
        secondSnake = new Snake(null, 1);

        stage = new Stage();
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.69f, 0.66f, 0.66f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        drawCells(0, 0, main.width, main.height);
        drawSnake(0, 0, main.width, main.height);

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.6f);
        shapeRenderer.rect(0, 0, main.width, main.height);
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        drawButtonStart();
    }

    private void drawButtonStart() {
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(0, 0, 0, 1);
//        drawSuperRect(draw,0, 100, 100);
//        shapeRenderer.end();

//        BitmapFont bitmapFont = new BitmapFont(Gdx.files.internal("data/nameOfFont.fnt"),
//                Gdx.files.internal("data/nameOfFont.png"), false);

        batch.begin();
        Label label = new Label("Start game", new Label.LabelStyle(new BitmapFont(Gdx.files.internal("text_style.fnt")), Color.BLACK));
        label.setX(main.width / 2);
        label.setY(main.height / 2);
        label.draw(batch, 1);
        batch.end();
    }

    private void drawSnake(float x, float y, float X, float Y) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 0, 0, 1);

        float indentX = (X - x) / widthArea / (widthArea + 1) * 3;
        float sizeSquare = (X - x - indentX * (widthArea + 1)) / widthArea;
        float indentY = (Y - y - sizeSquare * heightArea) / (heightArea + 1);

        ArrayList<Snake.Point> listPoint = firstSnake.getArrayPoint();
        for(Snake.Point i : listPoint) {
            float startX = x + i.x * (sizeSquare + indentX) + indentX;
            float startY = y + i.y * (sizeSquare + indentY) + indentY;

            drawSuperRect(startX, startY, startX + sizeSquare, startY + sizeSquare);
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 1, 1);

        listPoint = secondSnake.getArrayPoint();
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

        float indentX = (X - x) / widthArea / (widthArea + 1) * 3;
        float sizeSquare = (X - x - indentX * (widthArea + 1)) / widthArea;
        float indentY = (Y - y - sizeSquare * heightArea) / (heightArea + 1);

        for(int i = 0; i < widthArea; ++i) {
            for(int j = 0; j < heightArea; ++j) {
                float startX = x + i * (sizeSquare + indentX) + indentX;
                float startY = y + j * (sizeSquare + indentY) + indentY;

                drawSuperRect(startX - 1, startY - 1, startX + sizeSquare + 1, startY + sizeSquare + 1);
            }
        }

        shapeRenderer.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(1, 1, 1, 1);

        for(int i = 0; i < widthArea; ++i) {
            for(int j = 0; j < heightArea; ++j) {
                if(i == 0 || i == widthArea - 1 || j == 0 || j == heightArea - 1) {
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
