package com.example.spaceescape.model;

import android.graphics.Point;

public class Meteor extends BoardElement {

    interface MovementListener {
        void meteorHasMoved(Point oldPosition, Point newPosition);
        void meteorUpdate(Meteor met, Point pos);
    }

    private MovementListener listener;

    public Meteor(int limit, int x, int y) {
        super(limit, x, y);
        this.limit = limit;
    }

    public void move() {
        Point pos = new Point(location.x, location.y);
        if(++location.y < limit && isAlive) {
            if(location.y == 0){
                listener.meteorUpdate(this, location);
                return;
            }
            listener.meteorHasMoved(pos, location);
        } else  {
            --location.y;
            isAlive = false;
            listener.meteorUpdate(this, location);
        }
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public void kill() {
        super.kill();
        listener.meteorUpdate(this, location);
    }

    public void setMovementListener(MovementListener listener) {
        this.listener = listener;
    }

}
