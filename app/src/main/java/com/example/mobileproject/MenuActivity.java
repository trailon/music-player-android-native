package com.example.mobileproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MenuActivity extends Activity {
    private Button songListButton;
    private Button playListButton;
    private Button exitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_screen);
        songListButton = findViewById(R.id.songs);
        playListButton = findViewById(R.id.playlists);
        exitButton = findViewById(R.id.exit);

        songListButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ToSongs();
            }
        });
        playListButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Üzgünüz Özellik Mevcut Değil", Toast.LENGTH_SHORT).show();
            }
        });
        exitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void ToSongs(){
        Intent intent = new Intent(MenuActivity.this,SongListActivity.class);
        MenuActivity.this.startActivity(intent);
    }

    private void ToPlaylists(){
        Intent intent = new Intent(MenuActivity.this,SongListActivity.class);
        MenuActivity.this.startActivity(intent);
    }

    private void Exit(){
        Intent intent = new Intent(MenuActivity.this,SongListActivity.class);
        MenuActivity.this.startActivity(intent);
    }
}
