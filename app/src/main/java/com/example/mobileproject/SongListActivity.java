package com.example.mobileproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class SongListActivity extends Activity {
        private ArrayList<Song> songsList;
        private RecyclerView songRecyclerView;

        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.songlist);
            songRecyclerView = findViewById(R.id.song_list);
            songsList = new ArrayList<Song>();
            File musicFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
            File downloadFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            getSongs(musicFolder.getAbsolutePath()+"/");
            getSongs(downloadFolder.getAbsolutePath()+"/");
            setAdapter();
        }

    @RequiresApi(api = Build.VERSION_CODES.N)
    ArrayList<HashMap<String,String>> getSongs(String rootPath) {
        ArrayList<HashMap<String,String>> fileList = new ArrayList<>();
        try {
            File rootFolder = new File(rootPath);
            File[] files = rootFolder.listFiles();
            for (File file : files) {
                if (file.isDirectory()) {
                    if (getSongs(file.getAbsolutePath()) != null) {
                        fileList.addAll(getSongs(file.getAbsolutePath()));
                    } else {
                        break;
                    }
                } else if (file.getName().endsWith(".mp3")) {
                    Uri uri = Uri.fromFile(file);
                    MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
                    mediaMetadataRetriever.setDataSource(this, uri);
                    String songName = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                    String artist = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                    String album = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                    String genre = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE);
                    String track = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS);
                    String time = mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                    int durationOfCurrentSong = Integer.parseInt(time) / 1000;
                    int minute = durationOfCurrentSong / 60;
                    int seconds = durationOfCurrentSong % 60;
                    String secondsString = Integer.toString(seconds);
                    if(secondsString.length() < 2){
                        secondsString = "0"+secondsString;
                    }
                    time = Integer.toString(minute) + ":"+secondsString;
                    songsList.add(new Song(songName,artist,time,uri.toString(),file.getPath()));
                }
            }
            songsList.sort(Comparator.comparing(Song::getSongName));
            return fileList;
        } catch (Exception e) {
            System.out.println("Get Song Exception "+e);
            return null;
        }
    }

    private void setAdapter() {
            Intent intent = getIntent();
            recyclerAdapter adapter = new recyclerAdapter(songsList,SongListActivity.this,intent);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
            songRecyclerView.setLayoutManager(layoutManager);
            songRecyclerView.setItemAnimator(new DefaultItemAnimator());
            songRecyclerView.setAdapter(adapter);
    }
}
