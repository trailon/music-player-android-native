package com.example.mobileproject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder>{
    private ArrayList<Song> songsList;
    private Context context;
    private Intent intent;
    public recyclerAdapter(ArrayList<Song> songsList,Context context,Intent songlistintent){
        this.songsList = songsList;
        this.context = context;
        this.intent = songlistintent;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView songName;
        private TextView ownerName;
        private TextView songTime;
        private ImageView songImage;
        private ConstraintLayout itemLayout;
        private Button deleteButton;
        private Button shareButton;
        ConstraintLayout constraintLayout;
        private recyclerAdapter adapter;

        public MyViewHolder linkAdapter(recyclerAdapter adapter){
            this.adapter = adapter;
            return this;
        }
        public MyViewHolder(final View view) {
            super(view);
            constraintLayout = itemView.findViewById(R.id.song_item);
            songName = view.findViewById(R.id.song_name);
            ownerName = view.findViewById(R.id.owner_name);
            songTime = view.findViewById(R.id.song_time);
            songImage = view.findViewById(R.id.song_image);
            itemLayout = view.findViewById(R.id.song_item);
            deleteButton = view.findViewById(R.id.delete_button);
            itemView.findViewById(R.id.delete_button).setOnClickListener(v -> {
                try{
                    File fileToDelete = new File(songsList.get(getAdapterPosition()).getPath());
                    fileToDelete.setWritable(true);
                    fileToDelete.setReadable(true);
                    if(fileToDelete.exists()){
                        fileToDelete.delete();
                        try {
                            fileToDelete.getCanonicalFile().delete();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else{
                        fileToDelete = new File(songsList.get(getAdapterPosition()).getFilepath());
                        fileToDelete.setWritable(true);
                        fileToDelete.setReadable(true);
                        if(fileToDelete.exists()){
                            fileToDelete.delete();
                            try {
                                fileToDelete.getCanonicalFile().delete();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }else{
                            System.out.println("Başarılı");
                        }
                    }
                    fileToDelete.deleteOnExit();
                }catch(Exception e){
                    e.printStackTrace();
                }
                adapter.songsList.remove(getAdapterPosition());
                adapter.notifyItemRemoved(getAdapterPosition());
            });
            shareButton = view.findViewById(R.id.share_button);
        }
    }

    @NonNull
    @Override
    public recyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_song,parent,false);
        return new MyViewHolder(itemView).linkAdapter(this);
    }



    @Override
    public void onBindViewHolder(@NonNull recyclerAdapter.MyViewHolder holder, int position) {
        String song_uri_path = songsList.get(position).getPath();
        String song_name = songsList.get(position).getSongName();
        String owner_name = songsList.get(position).getOwnerName();
        String song_time = songsList.get(position).getSongTime();
        String song_file_path = songsList.get(position).getFilepath();
        holder.songName.setText(song_name);
        holder.ownerName.setText(owner_name);
        holder.songTime.setText(song_time);
        holder.songImage.setImageResource(R.mipmap.songicon);
        holder.itemLayout.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PlaySongActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("current_song",songsList.get(position));
                bundle.putParcelableArrayList("songs_list",songsList);
                bundle.putInt("current_song_index",position);
                intent.putExtras(bundle);
                context.startActivity(intent,bundle);
            }
        });
        holder.shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                File sharedFile = new File(song_file_path);
                System.out.println(song_file_path);
                String type =  getMimeType(sharedFile.getName());
               //if (type != null){type="*/*";}
                try {
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT,"İşte senin için bir şarkı!");
                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        Uri path = FileProvider.getUriForFile(context,"Serdem",sharedFile);
                        intent.putExtra(Intent.EXTRA_STREAM,path);
                    }else{
                        intent.putExtra(Intent.EXTRA_STREAM,Uri.fromFile(sharedFile));
                    }
                    intent.setType(type);
                    context.startActivity(intent);

                }catch (Exception e){
                    Toast.makeText(context,"Bir Hata Oluştu", Toast.LENGTH_SHORT);
                }
            }
        });
    }
    // url = file path or whatever suitable URL you want.
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

}
