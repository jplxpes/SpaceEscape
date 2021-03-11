package com.example.spaceescape.view;

import android.content.Context;
import android.graphics.Canvas;

import com.example.spaceescape.R;

import pt.isel.poo.tile.Img;
import pt.isel.poo.tile.Tile;

public class MeteorTile implements Tile {

    private final Context ctx;
    private Img img;

    public MeteorTile(Context ctx) {
        this.ctx = ctx;
        img = new Img(ctx, R.drawable.rock);

    }

    @Override
    public void draw(Canvas canvas, int side) {
        img.draw(canvas, side, side, (float) (Math.random()*360), null);
    }

    @Override
    public boolean setSelect(boolean selected) {
        return false;
    }
}

