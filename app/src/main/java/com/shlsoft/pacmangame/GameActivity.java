package com.shlsoft.pacmangame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Timer;

import pl.droidsonroids.gif.GifImageView;

public class GameActivity extends AppCompatActivity {

    //Frame
    private FrameLayout game_frame;
    private int frameHeight, frameWidth, initialFrameWidth;

    //Image
    private ImageView pacman, burger, ice_cream, donut;
    private GifImageView bat, bomb;
    private Drawable pacmanImgRight, pacmanImgLeft;

    //Size
    private int pacmanSize;

    //Position
    private float pacmanX, pacmanY;
    private float burgerX, burgerY;
    private float iceCreamX, iceCreamY;
    private float donutX, donutY;
    private float batX, batY;
    private float bombX, bombY;

    //Class
    private Timer timer = new Timer();
    private Handler handler = new Handler();

    //Status
    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean bat_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        initViews();

        startGame();
    }

    private void startGame() {
        start_flg = true;
        if (frameHeight == 0) {
            frameHeight = game_frame.getHeight();
            frameWidth = game_frame.getWidth();
            initialFrameWidth = frameWidth;

            pacmanSize = pacman.getHeight();
            pacmanX = pacman.getX();
            pacmanY = pacman.getY();
        }


    }

    private void initViews() {
        game_frame = findViewById(R.id.game_frame);
        pacman = findViewById(R.id.pacman);
        burger = findViewById(R.id.burger);
        ice_cream = findViewById(R.id.ice_cream);
        donut = findViewById(R.id.donut);
        bat = findViewById(R.id.bat);
        bomb = findViewById(R.id.bomb);

        pacmanImgLeft = getResources().getDrawable(R.drawable.pacman_left);
        pacmanImgRight = getResources().getDrawable(R.drawable.pacman_right);
    }
}
