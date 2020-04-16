package com.shlsoft.pacmangame;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

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
    private Timer timer;
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

    public void changePosition(){
        //Move Pacman
        if(action_flg){
            //Touching
            pacmanX += 20;
            pacman.setImageDrawable(pacmanImgRight);
        }
        else{
            //Releasing
            pacmanX -= 20;
            pacman.setImageDrawable(pacmanImgLeft);
        }

        //Check box position
        if(pacmanX < 0){
            pacmanX = 0;
            pacman.setImageDrawable(pacmanImgRight);
        }

        if(pacmanX > game_frame.getWidth() - pacman.getWidth()){
            pacmanX = game_frame.getWidth() - pacman.getWidth();
            pacman.setImageDrawable(pacmanImgLeft);
        }

        pacman.setX(pacmanX);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(start_flg){
            if(event.getAction() == MotionEvent.ACTION_DOWN){ //pressed gesture has started
                action_flg = true;
            }
            else if(event.getAction() == MotionEvent.ACTION_UP){ //pressed gesture has finished
                action_flg = false;
            }
        }
        return true;
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

        pacman.setX(0.0f); //initial position of the pacman

        //initial positions of the objects
        burger.setY(3000.0f); //outside the screen
        ice_cream.setY(3000.0f);
        donut.setY(3000.0f);
        bomb.setY(3000.0f);
        bat.setY(3000.0f);

        burgerY = burger.getY();
        iceCreamY = ice_cream.getY();
        donutY = donut.getY();
        bombY = bomb.getY();
        batY = bat.getY();

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        changePosition();
                    }
                });
            }
        },0,20);
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
