package com.example.mediaplayer;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder>{

    private List<Song> songs;
    private MySongListener listener;
    private Context context;


    interface MySongListener {
        void onSongClicked(int position, View view);
    }

    public void setListener(MySongListener listener){
        this.listener = listener;
    }

    public SongAdapter(List<Song> songs, Context context) {
        this.songs = songs;
        this.context=context;
    }


    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.song_item,parent,false);
        SongViewHolder songViewHolder = new SongViewHolder(view);
        return songViewHolder;
    }

    @Override
    public void onBindViewHolder(SongAdapter.SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.nameTv.setText(song.getSongName());
        holder.artistTv.setText(song.getArtist());
     //   Picasso.get().load(song.getImageResId()).centerCrop().fit().into(holder.imageIv);
        Glide.with(context).load(song.getImageResId()).centerCrop().into(holder.imageIv);
       //holder.link.setText(song.getLink());
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder{
        TextView nameTv;
        TextView artistTv;
        ImageView imageIv;
        TextView link;

        public SongViewHolder(View itemView) {
            super(itemView);
            nameTv=itemView.findViewById(R.id.song_name);
            artistTv=itemView.findViewById(R.id.song_artist);
            imageIv=itemView.findViewById(R.id.song_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener!=null) {
                        listener.onSongClicked(getAdapterPosition(),view);
                    }
                }
            });
        }
    }
}
