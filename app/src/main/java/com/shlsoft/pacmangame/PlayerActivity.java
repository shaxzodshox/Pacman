package com.shlsoft.pacmangame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import maes.tech.intentanim.CustomIntent;

public class PlayerActivity extends AppCompatActivity {

    private String player = "";

    private EditText et_player;
    private Button btn_start_game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getUser();

        et_player = findViewById(R.id.et_player);
        btn_start_game = findViewById(R.id.btn_start_game);

        if(!player.equals("")){
            et_player.setText(player);
            et_player.requestFocus();
        }

        btn_start_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_player.getText().toString().isEmpty()){
                    et_player.setError("Iltimos ism kiriting");
                    et_player.requestFocus();
                }
                else{
                    //save player name
                    SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFS,MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString(Constants.SHARED_PREFS_PLAYER,et_player.getText().toString());
                    edit.apply();

                    startActivity(new Intent(getApplicationContext(),GameActivity.class));
                    finish();
                }
            }
        });


    }

    private void getUser() {
        SharedPreferences preferences = getSharedPreferences(Constants.SHARED_PREFS,MODE_PRIVATE);
        player = preferences.getString(Constants.SHARED_PREFS_PLAYER,"");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        CustomIntent.customType(PlayerActivity.this, "right-to-left");
        finish();
    }
}
