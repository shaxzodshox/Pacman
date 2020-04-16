package com.shlsoft.pacmangame;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import pl.droidsonroids.gif.GifImageView;


public class GameActivity extends AppCompatActivity {

    // Frame
    private FrameLayout gameFrame;
    private int frameHeight, frameWidth, initialFrameWidth;
    private LinearLayout startLayout;

    // Image
    private ImageView pacman, burger, donut, poison;
    private GifImageView bat, bomb;
    private Drawable imagePacmanRight, imagePacmanLeft;

    // Size
    private int pacmanSize;

    // Position
    private float pacmanX, pacmanY;
    private float bombX, bombY;
    private float burgerX, burgerY;
    private float donutX, donutY;
    private float batX, batY;
    private float poisonX, poisonY;

    // Score
    private TextView scoreLabel, highScoreLabel;
    private int score, highScore, timeCount;
    private SharedPreferences settings;

    // Class
    private Timer timer;
    private Handler handler = new Handler();

    // Status
    private boolean start_flg = false;
    private boolean action_flg = false;
    private boolean donut_flg = false;
    private boolean bat_flg = false;
    private boolean poison_flg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gameFrame = findViewById(R.id.gameFrame);
        startLayout = findViewById(R.id.startLayout);
        pacman = findViewById(R.id.pacman);
        bomb = findViewById(R.id.bomb);
        bat = findViewById(R.id.bat);
        burger = findViewById(R.id.burger);
        donut = findViewById(R.id.donut);
        poison = findViewById(R.id.poison);
        scoreLabel = findViewById(R.id.scoreLabel);
        highScoreLabel = findViewById(R.id.highScoreLabel);

        imagePacmanLeft = getResources().getDrawable(R.drawable.pacman_left);
        imagePacmanRight = getResources().getDrawable(R.drawable.pacman_right);

        // High Score
        settings = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        highScore = settings.getInt("HIGH_SCORE", 0);
        highScoreLabel.setText("High Score : " + highScore);

    }

    public void changePos() {

        // Add timeCount
        timeCount += 20;

        // Burger
        burgerY += 42;

        float burgerCenterX = burgerX + burger.getWidth() / 2;
        float burgerCenterY = burgerY + burger.getHeight() / 2;

        if (hitCheck(burgerCenterX, burgerCenterY)) {
            burgerY = frameHeight + 100;
            score += 10;
        }

        if (burgerY > frameHeight) {
            burgerY = -100;
            burgerX = (float) Math.floor(Math.random() * (frameWidth - burger.getWidth()));
        }
        burger.setX(burgerX);
        burger.setY(burgerY);

        // Donut
        if (!donut_flg && timeCount % 20000 == 0) {
            donut_flg = true;
            donutY = -24;
            donutX = (float) Math.floor(Math.random() * (frameWidth - donut.getWidth()));
        }

        if (donut_flg) {
            donutY += 24;

            float donutCenterX = donutX + donut.getWidth() / 2;
            float donutCenterY = donutY + donut.getWidth() / 2;

            if (hitCheck(donutCenterX, donutCenterY)) {
                donutY = frameHeight + 30;
                score += 30;
                // Change FrameWidth
                if (initialFrameWidth > frameWidth * 110 / 100) {
                    frameWidth = frameWidth * 110 / 100;
                    changeFrameWidth(frameWidth);
                }
            }

            if (donutY > frameHeight) donut_flg = false;
            donut.setX(donutX);
            donut.setY(donutY);
        }

        //Bat
        if (!bat_flg && timeCount % 13000 == 0) {
            bat_flg = true;
            batY = -20;
            batX = (float) Math.floor(Math.random() * (frameWidth - bat.getWidth()));
        }

        if (bat_flg) {
            batY += 28;

            float batCenterX = batX + bat.getWidth() / 2;
            float batCenterY = batY + bat.getWidth() / 2;

            if (hitCheck(batCenterX, batCenterY)) {
                //Do some action if the touch to the bat
                gameOver();
            }

            if (batY > frameHeight) bat_flg = false;
            bat.setX(batX);
            bat.setY(batY);
        }

        //Poison
        if (!poison_flg && timeCount % 14000 == 0) {
            poison_flg = true;
            poisonY = -34;
            poisonX = (float) Math.floor(Math.random() * (frameWidth - poison.getWidth()));
        }

        if (poison_flg) {
            poisonY += 34;

            float poisonCenterX = poisonX + poison.getWidth() / 2;
            float poisonCenterY = poisonY + poison.getWidth() / 2;

            if (hitCheck(poisonCenterX, poisonCenterY)) {
                poisonY += frameHeight + 100;
                //Do some action if the touch to the poison
                score = 0;
            }

            if (poisonY > frameHeight) poison_flg = false;
            poison.setX(poisonX);
            poison.setY(poisonY);
        }

        // Bomb
        bombY += 38;

        float bombCenterX = bombX + bomb.getWidth() / 2;
        float bombCenterY = bombY + bomb.getHeight() / 2;

        if (hitCheck(bombCenterX, bombCenterY)) {
            bombY = frameHeight + 100;

            // Change FrameWidth
            frameWidth = frameWidth * 80 / 100;
            changeFrameWidth(frameWidth);
            if (frameWidth <= pacmanSize) {
                gameOver();
            }

        }

        if (bombY > frameHeight) {
            bombY = -100;
            bombX = (float) Math.floor(Math.random() * (frameWidth - bomb.getWidth()));
        }

        bomb.setX(bombX);
        bomb.setY(bombY);

        // Move Pacman
        if (action_flg) {
            // Touching
            pacmanX += 26;
            pacman.setImageDrawable(imagePacmanRight);
        } else {
            // Releasing
            pacmanX -= 26;
            pacman.setImageDrawable(imagePacmanLeft);
        }

        // Check pacman position.
        if (pacmanX < 0) {
            pacmanX = 0;
            pacman.setImageDrawable(imagePacmanRight);
        }
        if (frameWidth - pacmanSize < pacmanX) {
            pacmanX = frameWidth - pacmanSize;
            pacman.setImageDrawable(imagePacmanLeft);
        }

        pacman.setX(pacmanX);

        scoreLabel.setText("Score : " + score);

    }

    public boolean hitCheck(float x, float y) {
        if (pacmanX <= x && x <= pacmanX + pacmanSize &&
                pacmanY <= y && y <= frameHeight) {
            return true;
        }
        return false;
    }

    public void changeFrameWidth(int frameWidth) {
        ViewGroup.LayoutParams params = gameFrame.getLayoutParams();
        params.width = frameWidth;
        gameFrame.setLayoutParams(params);
    }

    public void gameOver() {
        // Stop timer.
        timer.cancel();
        timer = null;
        start_flg = false;

        // Before showing startLayout, sleep 1 second.
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        changeFrameWidth(initialFrameWidth);

        startLayout.setVisibility(View.VISIBLE);
        pacman.setVisibility(View.INVISIBLE);
        bomb.setVisibility(View.INVISIBLE);
        burger.setVisibility(View.INVISIBLE);
        donut.setVisibility(View.INVISIBLE);
        bat.setVisibility(View.INVISIBLE);
        poison.setVisibility(View.INVISIBLE);

        // Update High Score
        if (score > highScore) {
            highScore = score;
            highScoreLabel.setText("High Score : " + highScore);

            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", highScore);
            editor.commit();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (start_flg) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                action_flg = true;

            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                action_flg = false;

            }
        }
        return true;
    }

    public void startGame(View view) {
        start_flg = true;
        startLayout.setVisibility(View.INVISIBLE);

        if (frameHeight == 0) {
            frameHeight = gameFrame.getHeight();
            frameWidth = gameFrame.getWidth();
            initialFrameWidth = frameWidth;

            pacmanSize = pacman.getHeight();
            pacmanX = pacman.getX();
            pacmanY = pacman.getY();
        }

        frameWidth = initialFrameWidth;

        pacman.setX(0.0f);
        bomb.setY(3000.0f);
        burger.setY(3000.0f);
        donut.setY(3000.0f);
        bat.setY(3000.0f);
        poison.setY(3000.0f);

        bombY = bomb.getY();
        burgerY = burger.getY();
        donutY = donut.getY();
        batY = bat.getY();
        poisonY = poison.getY();

        pacman.setVisibility(View.VISIBLE);
        bomb.setVisibility(View.VISIBLE);
        burger.setVisibility(View.VISIBLE);
        donut.setVisibility(View.VISIBLE);
        bat.setVisibility(View.VISIBLE);
        poison.setVisibility(View.VISIBLE);

        timeCount = 0;
        score = 0;
        scoreLabel.setText("Score : 0");


        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (start_flg) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            changePos();
                        }
                    });
                }
            }
        }, 0, 20);
    }

    public void quitGame(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }


}
