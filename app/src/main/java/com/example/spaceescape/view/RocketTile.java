package com.example.spaceescape.view;

import android.content.Context;
import android.graphics.Canvas;

import com.example.spaceescape.R;
import com.example.spaceescape.model.Rocket;

import pt.isel.poo.tile.Img;
import pt.isel.poo.tile.Tile;

public class RocketTile implements Tile {

    private Img img;
    private Img dead;
    private Rocket ship;

    public RocketTile(Context context, Rocket ship) {
        this.ship = ship;
        img = new Img(context, R.drawable.ship);
        dead = new Img(context, R.drawable.explosion);
    }

    @Override
    public void draw(Canvas canvas, int side) {
        img.draw(canvas, side, side, null);
        if(!ship.isAlive())
            dead.draw(canvas,side,side,null);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}
