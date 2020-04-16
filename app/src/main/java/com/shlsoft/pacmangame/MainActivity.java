package com.shlsoft.pacmangame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;

import maes.tech.intentanim.CustomIntent;
import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btn_start, btn_player, btn_highscores, btn_instructions, btn_exit;
    TextView tv_player_view, tv_player_name;

    GifImageView menu_bat;

    String username = "";

    private boolean bat_down_position = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        initListeners();

        //check the last played user
        getLastUser();

        if(username.equals("")){
            tv_player_name.setVisibility(View.INVISIBLE);
            tv_player_view.setVisibility(View.INVISIBLE);
        }
        else{
            tv_player_view.setVisibility(View.VISIBLE);
            tv_player_name.setText(username);
        }
    }

    private void getLastUser() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFS,MODE_PRIVATE);
        username = preferences.getString(Constants.SHARED_PREFS_PLAYER,"");
    }

    private void initListeners() {
        btn_start.setOnClickListener(this);
        btn_player.setOnClickListener(this);
        btn_highscores.setOnClickListener(this);
        btn_instructions.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        menu_bat.setOnClickListener(this);
    }

    private void initViews() {
        btn_start = findViewById(R.id.btn_start);
        btn_player = findViewById(R.id.btn_player);
        btn_highscores = findViewById(R.id.btn_highscores);
        btn_instructions = findViewById(R.id.btn_instructions);
        btn_exit = findViewById(R.id.btn_exit);

        menu_bat = findViewById(R.id.menu_bat);

        tv_player_view = findViewById(R.id.tv_player_view);
        tv_player_name = findViewById(R.id.tv_player_name);
    }

    public void startGame(View view) {
        startActivity(new Intent(getApplicationContext(),GameActivity.class));
        overridePendingTransition(0,0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start:
                if(username.equals("")){
                    startActivity(new Intent(getApplicationContext(),PlayerActivity.class));
                    CustomIntent.customType(MainActivity.this, "left-to-right");
                    finish();
                }else{
                    startActivity(new Intent(getApplicationContext(),GameActivity.class));
                    CustomIntent.customType(MainActivity.this, "left-to-right");
                    finish();
                }
                break;
            case R.id.btn_player:
                startActivity(new Intent(getApplicationContext(),PlayerActivity.class));
                CustomIntent.customType(MainActivity.this, "left-to-right");
                finish();
                break;

                //bat is clicked on menu
            case R.id.menu_bat:
                if(!bat_down_position) {
                    menu_bat.animate().translationYBy(120f).setDuration(1000); //moves from -1000 to 1000
                    bat_down_position = true;
                }else{
                    menu_bat.animate().translationYBy(-120f).setDuration(1000); //moves from -1000 to 1000
                    bat_down_position = false;
                }

                break;
        }
    }


}
