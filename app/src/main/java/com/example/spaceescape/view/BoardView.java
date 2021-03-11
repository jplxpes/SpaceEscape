package com.example.spaceescape.view;

import android.content.Context;
import android.graphics.Point;
import android.location.Location;

import com.example.spaceescape.model.Board;
import com.example.spaceescape.model.BoardElement;
import com.example.spaceescape.model.Meteor;
import com.example.spaceescape.model.Rocket;

import java.util.HashMap;
import java.util.List;

import pt.isel.poo.tile.Tile;
import pt.isel.poo.tile.TilePanel;

public class BoardView {

    private final TilePanel panel;
    private final Board board;
    private final Context ctx;
    private HashMap<Class, Tile> map;

    private void updatePosition(Point position) {
        BoardElement elem = board.getElementAt(position.x, position.y);
        if (elem != null) {
            Tile tile = map.get(board.getElementAt(position.x, position.y).getClass());
            panel.setTile(position.x, position.y, tile);
        } else {
            panel.setTile(position.x, position.y, null);
        }
    }

    public BoardView(TilePanel panel, Board board, Context ctx) {
        this.panel = panel;
        this.board = board;
        this.ctx = ctx;
        initialize();
        board.setChangeListener(new Board.ChangeListener() {
            @Override
            public void onChanged(Point oldLocation, Point newLocation) {
                updatePosition(oldLocation);
                updatePosition(newLocation);
            }

            @Override
            public void newCell(Point pos) {
                updatePosition(pos);
            }
        });
    }

    private void initialize() {
        map = new HashMap<>();
        map.put(Rocket.class, new RocketTile(ctx, board.getRocket()));
        map.put(Meteor.class, new MeteorTile(ctx));
        for(int x = 0; x < board.arenaWidth; ++x) {
            for(int y = 0; y < board.arenaHeight; ++y) {
                updatePosition(new Point(x, y));
            }
        }
    }
}

