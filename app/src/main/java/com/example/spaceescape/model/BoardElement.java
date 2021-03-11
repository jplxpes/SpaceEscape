package com.example.spaceescape.model;

import android.graphics.Point;

public abstract class BoardElement {

    Point location;
    boolean isAlive = true;

    public Point getLocation() {
        return location;
    }

    public int getLimit() {
        return limit;
    }

    int limit;

    public BoardElement(int limit, int x, int y){
        location = new Point(x, y);
        this.limit = limit;
    }

    public void kill() {
        isAlive = false;
    }
}
