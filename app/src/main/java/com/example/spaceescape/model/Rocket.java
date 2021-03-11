package com.example.spaceescape.model;

import android.graphics.Point;

public class Rocket extends BoardElement {

    private MovementListener listener;
    int lastVal = 0;

    interface MovementListener {
        void rocketHasMoved(Point oldPosition, Point newPosition);
        void rocketUpdate(Rocket ship, Point pos);
    }

    public void setListener(MovementListener listener) {
        this.listener = listener;
    }

    public Rocket(int limit, int x, int y) {
        super(limit, x, y);
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void move(int side, int vertical) {
        lastVal = side;
        Point old = new Point(location.x, location.y);
        location.x += side*(-1);
        location.y += vertical;
        if(location.x  < 0) {
            location.x = 0;
        }
        if(location.x >= limit) {
            location.x = limit - 1;
        }
        if(location.y >= limit - 2) {
            location.y = limit - 2;
        }
        if(location.y  <= limit - limit/2 + 1) {
            location.y =  limit - limit/2 + 1;
        }
        if(old.x != location.x || old.y != location.y){
            listener.rocketHasMoved(old, location);
        }
    }
}
