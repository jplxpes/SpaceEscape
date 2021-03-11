package com.example.spaceescape.model;

import android.graphics.Point;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Board {


    int limit = 8;

    public void increaseMeteor() {
        ++limit;
    }

    public boolean deleteMeteor() {
        for (int i = arenaHeight - 1; i > 0; i--) {
            BoardElement temp =  board[ship.location.x][i];
            if(temp instanceof Meteor) {
                temp.kill();
                enemies.remove(temp);
                return true;
            }
        }
        return false;
    }

    public interface ChangeListener {
        void onChanged(Point oldLocation, Point newLocation);
        void newCell(Point pos);
    }

    private final List<Meteor> enemies;
    private final BoardElement[][] board;
    public final int arenaWidth;
    public final int arenaHeight;
    private Rocket ship;
    ChangeListener listener;
    Iterator<Meteor> it;

    public void setChangeListener(ChangeListener listener) {
        this.listener = listener;
    }

    public Board(int width, int height){
        arenaHeight = height;
        arenaWidth = width;
        board = new BoardElement[arenaWidth][arenaHeight];
        enemies = new LinkedList<>();
        it =  enemies.iterator();
        createRocket();
    }

    private void createRocket(){
        ship = new Rocket(arenaWidth, arenaWidth/2,arenaHeight - 2);
        board[arenaWidth/2][arenaHeight - 2] = ship;
        ship.setListener(new Rocket.MovementListener() {
            @Override
            public void rocketHasMoved(Point oldPosition, Point newPosition) {
                board[newPosition.x][newPosition.y] = ship;
                board[oldPosition.x][oldPosition.y] = null;
                listener.onChanged(oldPosition,newPosition);
            }

            @Override
            public void rocketUpdate(Rocket ship, Point pos) {
                if(!ship.isAlive()) {
                    board[pos.x][pos.y] = null;
                }
                listener.newCell(pos);
            }
        });
    }

    public Rocket getRocket(){
        return ship;
    }

    public void fall() {

        if(enemies.size() < limit){
            int x = (int) (Math.random()*arenaWidth);
            Meteor temp = new Meteor(arenaHeight, x, -1);
            board[x][0] = temp;
            temp.setMovementListener(new Meteor.MovementListener() {
                @Override
                public void meteorHasMoved(Point oldPosition, Point newPosition) {
                    board[newPosition.x][newPosition.y] = board[oldPosition.x][oldPosition.y];
                    board[oldPosition.x][oldPosition.y] = null;
                    listener.onChanged(oldPosition,newPosition);
                }

                @Override
                public void meteorUpdate(Meteor met, Point pos) {
                    if(!met.isAlive()) {
                        board[pos.x][pos.y] = null;
                    }
                    listener.newCell(pos);
                }
            });
            enemies.add(temp);
        }

        for (Iterator<Meteor> it = enemies.iterator(); it.hasNext() && ship.isAlive;){
            Meteor met = it.next();
            met.move();
            if(met.location.x == ship.location.x && met.location.y == ship.location.y){
                board[ship.location.x][ship.location.y] = ship;
                ship.kill();
                listener.newCell(ship.location);
                break;
            }

            if(!met.isAlive()) {
                it.remove();
            }
        }
    }

    public void move(int side, int up) {
        ship.move(side, up);
    }

    public BoardElement getElementAt(int x, int y) {
        return board[x][y];
    }


}
