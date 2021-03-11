package com.example.spaceescape;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.TimeAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.spaceescape.model.Board;
import com.example.spaceescape.view.BoardView;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import pt.isel.poo.tile.TilePanel;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    public static final int LEVEL_UP = 75;
    public static final int SPEED = 25;
    public static final int CAP = 3;
    public static int COOLDOWN = 10000;
    Sensor sensor;
    ImageButton reset;
    TextView lvl, point;
    Board board;
    int vertical = 0;
    int horizontal = 0;
    int points = 0;
    int level = 0;
    int highscore;
    Button fire;
    Context ctx;
    TextView max;

    MediaPlayer laser;
    MediaPlayer explosion;
    MediaPlayer levelup;
    boolean sLaser = true;
    boolean sExplosion = true;
    boolean sLevel = true;

    private static final String  MY_PREFENCE_NAME = "com.example.spaceescape";
    private static final String PREF_TOTAL_KEY = "pref_total_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(MainActivity.this, sensor, SensorManager.SENSOR_DELAY_GAME);
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(Color.LTGRAY);

        reset = findViewById(R.id.reset);
        fire = findViewById(R.id.button);
        lvl = findViewById(R.id.level);
        point = findViewById(R.id.points);

        max = findViewById(R.id.highscore);
        fire.setTextColor(Color.WHITE);
        fire.setBackgroundColor(Color.RED);

        reset.setVisibility(View.GONE);
        final TilePanel panel = findViewById(R.id.tilePanel);
        panel.setBackgroundColor(Color.parseColor("#455578"));
        board = new Board(panel.getWidthInTiles(), panel.getHeightInTiles());
        ctx = this;
        new BoardView(panel, board,this);
        load();
        max.setText(String.valueOf(highscore));

        laser = MediaPlayer.create(ctx, R.raw.laser);
        explosion = MediaPlayer.create(ctx, R.raw.explosion);
        levelup = MediaPlayer.create(ctx, R.raw.level);

        fire.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                laser.start();
                fire.setEnabled(false);
                fire.setBackgroundColor(Color.GRAY);
                Timer buttonTimer = new Timer();
                buttonTimer.schedule(new TimerTask() {

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fire.setEnabled(true);
                                fire.setBackgroundColor(Color.RED);
                            }
                        });
                    }
                }, COOLDOWN);

                if(board.deleteMeteor())
                    points += 25;
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                points = 0;
                level = 0;
                sExplosion = true;
                board = new Board(panel.getWidthInTiles(), panel.getHeightInTiles());
                new BoardView(panel, board, ctx);
                fire.setEnabled(true);
                fire.setBackgroundColor(Color.RED);
                reset.setVisibility(View.GONE);
            }
        });

        final TimeAnimator animator = new TimeAnimator();
        animator.setTimeListener(new TimeAnimator.TimeListener() {
            int elapsedTime = 0;
            int interval = 75;
            int elapsedTime2 = 0;
            int spawntime = 300;
            int count = 0;
            @SuppressLint("SetTextI18n")
            @Override
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                if(board.getRocket().isAlive()){

                    if (elapsedTime >= interval) {
                        sLevel = true;
                        elapsedTime = 0;
                        board.move(horizontal, vertical);
                        if(elapsedTime2 >= spawntime) {
                            elapsedTime2 = 0;
                            point.setText(Integer.toString(points));
                            points++;
                            if(points % LEVEL_UP == 0){

                                if(sLevel) {
                                    levelup.start();
                                    sLevel = false;
                                }

                                level++;
                                lvl.setText(Integer.toString(level));
                                spawntime -= SPEED;
                                if(count++ == CAP){
                                    count = 0;
                                    board.increaseMeteor();
                                    COOLDOWN += 2500;
                                }
                            }
                            board.fall();
                        }

                    }
                    else {
                        elapsedTime += deltaTime;
                        elapsedTime2 += deltaTime;
                    }
                    if(points > highscore){
                        save(points);
                         highscore = points;
                         max.setText(String.valueOf(highscore));
                    }

                } else {

                    if(sExplosion) {
                        explosion.start();
                        sExplosion = false;
                    }

                     reset.setVisibility(View.VISIBLE);
                }

            }
        });
        animator.start();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        horizontal = (int)event.values[0];
        vertical = (int)event.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void save(int total){
        SharedPreferences pref = ctx.getSharedPreferences(MY_PREFENCE_NAME, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_TOTAL_KEY, total);
        editor.apply();
    }

    public void load(){
        SharedPreferences pref = ctx.getSharedPreferences(MY_PREFENCE_NAME, ctx.MODE_PRIVATE);
        highscore = pref.getInt( PREF_TOTAL_KEY, 0);
    }
}