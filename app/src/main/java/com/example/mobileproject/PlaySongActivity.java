package com.example.mobileproject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;

public class PlaySongActivity extends Activity {
    TextView title;
    TextView artist;
    ImageButton previous;
    ImageButton stop;
    ImageButton resume;
    ImageButton next;
    ImageView songImage;
    SeekBar bar;
    Bundle extras;
    Song currentSong;
    ArrayList<Song> songsList;
    Drawable defaultIcon;
    int currentSongIndex;
    int length = 0;
    MediaPlayer mediaplayer;
    private Handler mHandler = new Handler();
    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaplayer.release();
        mediaplayer = null;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_song);
        extras = getIntent().getExtras();
        currentSong = extras.getParcelable("current_song");
        songsList  = extras.getParcelableArrayList("songs_list");
        currentSongIndex = extras.getInt("current_song_index");
        System.out.println(currentSong.getSongTime());
        title = findViewById(R.id.title);
        artist = findViewById(R.id.artist);
        previous = findViewById(R.id.previous);
        stop = findViewById(R.id.stop);
        resume = findViewById(R.id.resume);
        next = findViewById(R.id.next);
        bar = findViewById(R.id.seek_bar);
        songImage = findViewById(R.id.song_icon);
        defaultIcon = songImage.getBackground();
        SetSongInfos();
        mediaplayer = MediaPlayer.create(PlaySongActivity.this, Uri.parse(currentSong.getPath()));
        mediaplayer.start();
        bar.setProgress(0);
        bar.setMax(mediaplayer.getDuration()/1000);
        mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                NextSong();
            }
        });
        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaplayer != null && fromUser){
                    length = mediaplayer.getCurrentPosition();
                    mediaplayer.seekTo(progress * 1000);
                }
                System.out.println("Current progress = " +Integer.toString(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        previous.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                PreviousSong();

            }
        });
        stop.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                StopSong();

            }
        });
        resume.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ResumeSong();

            }
        });
        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                NextSong();

            }
        });
        PlaySongActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaplayer != null && mediaplayer.isPlaying()){
                    //int mCurrentPosition = mediaplayer.getCurrentPosition() / 1000;
                    //bar.setProgress(mCurrentPosition);
                    int mCurrentPosition = mediaplayer.getCurrentPosition() / 1000;
                    bar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);
            }
        });

    }


    private void PreviousSong() {
        length = 0;
        mediaplayer.release();
        mediaplayer = null;
        if(currentSongIndex - 1 < 0){
            currentSongIndex = songsList.size()-1;
        }else{
            currentSongIndex-=1;
        }
        currentSong = songsList.get(currentSongIndex);
        SetSongInfos();
        mediaplayer = MediaPlayer.create(PlaySongActivity.this, Uri.parse(currentSong.getPath()));
        bar.setMax(mediaplayer.getDuration()/1000);
        bar.setProgress(0);
        mediaplayer.start();
    }

    private void StopSong() {
        mediaplayer.pause();
        length = mediaplayer.getCurrentPosition();
    }

    private void ResumeSong() {
        mediaplayer.seekTo(length);
        mediaplayer.start();
    }

    private void NextSong() {
        length = 0;
        mediaplayer.release();
        mediaplayer = null;
        if(currentSongIndex + 1 == songsList.size()){
            currentSongIndex = 0;
        }else{
            currentSongIndex+=1;
        }
        currentSong = songsList.get(currentSongIndex);
        SetSongInfos();
        mediaplayer = MediaPlayer.create(PlaySongActivity.this, Uri.parse(currentSong.getPath()));
        bar.setMax(mediaplayer.getDuration()/1000);
        bar.setProgress(0);
        mediaplayer.start();
    }

    private void SetSongInfos(){
        title.setText(currentSong.getSongName());
        artist.setText(currentSong.getOwnerName());
        Bitmap songCover = null;
        try {
            songCover = ThumbnailUtils.createVideoThumbnail(currentSong.getFilepath(), MediaStore.Images.Thumbnails.MINI_KIND);
        }catch(Exception e){
            e.printStackTrace();
        }
        if(songCover != null){
            songImage.setBackground(null);
            songImage.setImageBitmap(songCover);
        }else{
            songImage.setBackground(defaultIcon);
        }

    }


}
