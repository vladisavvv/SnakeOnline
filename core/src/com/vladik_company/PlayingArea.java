package com.vladik_company;

/**
 * Created by vladik on 5/30/18.
 */

public class PlayingArea {
    public Snake firstSnake;
    public Snake secondSnake;

    public final int widthArea = 20;
    public final int heightArea = 13;

    public boolean[][] area;

    public PlayingArea() {
        firstSnake = new Snake(this, 0);
        secondSnake = new Snake(this, 1);

        area = new boolean[widthArea][heightArea];
    }

    void addBlackPoint(int x, int y) {
        synchronized (area) {
            area[x][y] = true;
        }
    }
}
