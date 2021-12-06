package com.example.mediaplayer;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class SongInfoFragment extends Fragment {

    interface mySong{
        void fun();
    }

    mySong callBack;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            callBack =(mySong)context;
        }catch (ClassCastException ex){
            throw new ClassCastException("The activity must implement the myInfo interface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.song_info,container,false);
        TextView songName =rootView.findViewById(R.id.song_name_info);
        TextView artistName = rootView.findViewById(R.id.artist_name_info);
        ImageView imageView =rootView.findViewById(R.id.song_image_info);
        String song_name = getArguments().getString("songName");
        String artist_name=getArguments().getString("artistName");
        String image=getArguments().getString("songImage");;
        songName.setText(song_name);
        artistName.setText(artist_name);
        //Picasso.get().load(image).centerCrop().fit().into(imageView);
        Glide.with(this).load(image).centerCrop().into(imageView);
        return rootView;
    }
}
