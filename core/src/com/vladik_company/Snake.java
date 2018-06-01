package com.vladik_company;

import java.util.ArrayList;

/**
 * Created by vladik on 5/29/18.
 */

public class Snake {
    public class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private final int dx[] = {1, 0, -1, 0};
    private final int dy[] = {0, 1, 0, -1};

    private ArrayList<Point> listPoint;
    private int direct;
    private Integer deltaDirect = 0;
    public int type;

    public Snake(PlayingArea area, int typeSnake) {
        this.type = typeSnake;

        listPoint = new ArrayList<Point>();

        switch (typeSnake) {
            case 0:
                for(int i = 5; i >= 1; --i) {
                    listPoint.add(new Point(i, 6));
                }
                direct = 0;

                break;
            case 1:
                for(int i = 19 - 5; i < 19; ++i) {
                    listPoint.add(new Point(i, 6));
                }
                direct = 2;

                break;
            case 2:
                for(int i = 11; i >= 12 - 5; --i) {
                    listPoint.add(new Point(0, i));
                }
                direct = 1;

                break;
            case 3:
                for(int i = 0; i < 5; ++i) {
                    listPoint.add(new Point(19, i));
                }
                direct = 3;

                break;
        }
    }

    ArrayList<Point> getArrayPoint() {
        synchronized (listPoint) {
            return (ArrayList<Point>) listPoint.clone();
        }
    }

    public void setDeltaDirect(int newDeltaDirect) {
        synchronized (deltaDirect) {
            deltaDirect = newDeltaDirect;
        }
    }

    public int makeTurn() {
        synchronized (deltaDirect) {
            direct = (direct + deltaDirect + 4) % 4;
            deltaDirect = 0;
        }

        synchronized (listPoint) {
            for(int i = listPoint.size() - 2; i >= 0; --i) {
                listPoint.set(i + 1, listPoint.get(i));
            }
            listPoint.set(0, new Point(listPoint.get(0).x + dx[direct], listPoint.get(0).y + dy[direct]));
        }

        return direct;
    }

    public void makeTurn(int direct) {
        synchronized (listPoint) {
            for(int i = listPoint.size() - 2; i >= 0; --i) {
                listPoint.set(i + 1, listPoint.get(i));
            }
            listPoint.set(0, new Point(listPoint.get(0).x + dx[direct], listPoint.get(0).y + dy[direct]));
        }
    }
}
