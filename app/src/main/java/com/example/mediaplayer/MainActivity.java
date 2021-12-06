package com.example.mediaplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;


public class MainActivity extends AppCompatActivity implements SongInfoFragment.mySong, AddSongFragment.myInfo {

    final String ADD_TAG = "add";
    final String INFO_TAG="info";
    SharedPreferences sharedPreferences;
    boolean isPlay = false;
    RecyclerView recyclerView;
    SongInfoFragment songInfo;
    ArrayList<Song> songs;
    SongAdapter songAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        sharedPreferences = getSharedPreferences("song_first", MODE_PRIVATE);
        Boolean firstTime = sharedPreferences.getBoolean("firstTime", true);
        if (firstTime) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            songs = new ArrayList<>();
            songs.add(new Song("One More Cup Of Coffee", "Bob Dylan", "https://upload.wikimedia.org/wikipedia/commons/thumb/0/02/Bob_Dylan_-_Azkena_Rock_Festival_2010_2.jpg/1200px-Bob_Dylan_-_Azkena_Rock_Festival_2010_2.jpg", "https://www.syntax.org.il/xtra/bob.m4a"));
            songs.add(new Song("Sara", "Bob Dylan","https://www.biography.com/.image/t_share/MTgwMjk3MjI5MjU5NTE1MDMw/gettyimages-3315233.jpg", "https://www.syntax.org.il/xtra/bob1.m4a"));
            songs.add(new Song("The Man In Me", "Bob Dylan", "https://upload.wikimedia.org/wikipedia/commons/2/28/Joan_Baez_Bob_Dylan_crop.jpg","https://www.syntax.org.il/xtra/bob2.mp3"));

            saveData();
            editor.putBoolean("firstTime", false);
            editor.apply();
        } else
            loadData();


        ImageButton songPlay = findViewById(R.id.play_songs);
        songPlay.setOnClickListener(new View.OnClickListener() { //starts playing songs in menu
            @Override
            public void onClick(View v) {
                if (!isPlay) {
                    Intent intent = new Intent(MainActivity.this, MusicPlayerService.class);
                    intent.putExtra("list", songs);
                    intent.putExtra("command", "new_instance");
                    startService(intent);
                    isPlay = true;
                }else {
                    Intent intent3 = new Intent(MainActivity.this, MusicPlayerService.class);
                    intent3.putExtra("command", "play");
                    startService(intent3);

                }
            }
        });

        ImageButton songPause = findViewById(R.id.pause_songs);
        songPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(MainActivity.this, MusicPlayerService.class);
                intent4.putExtra("list", songs);
                intent4.putExtra("command", "pause");
                startService(intent4);
            }
        });

        ImageButton songNext=findViewById(R.id.next_songs);
        songNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, MusicPlayerService.class);
                intent1.putExtra("list", songs);
                intent1.putExtra("command", "next");
                startService(intent1);
            }
        });

        ImageButton songPrev= findViewById(R.id.prev_songs);
        songPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity.this, MusicPlayerService.class);
                intent2.putExtra("list", songs);
                intent2.putExtra("command", "prev");
                startService(intent2);
            }
        });

        ImageButton addSong = findViewById(R.id.add_song);
        addSong.setOnClickListener(new View.OnClickListener() { //adding new song to the list listener
            @Override
            public void onClick(View v) {
//                FragmentManager fragmentManager = getFragmentManager();
//                FragmentTransaction transaction = fragmentManager.beginTransaction();
//                transaction.add(R.id.root_container, new AddSongFragment(MainActivity.this), ADD_TAG);
//                transaction.addToBackStack(null);
                getSupportFragmentManager().beginTransaction().replace(R.id.root_container,new AddSongFragment(MainActivity.this),ADD_TAG).addToBackStack(null).commit();
               // transaction.commit();

            }
        });

        songAdapter = new SongAdapter(songs, MainActivity.this);
        songAdapter.setListener(new SongAdapter.MySongListener() {
            @Override
            public void onSongClicked(int position, View view) {
                Bundle bundle = new Bundle();
                bundle.putString("songName",songs.get(position).getSongName());
                bundle.putString("artistName",songs.get(position).getArtist());
                bundle.putString("songImage",songs.get(position).getImageResId());
                songInfo = new SongInfoFragment();
                songInfo.setArguments(bundle);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.root_container, songInfo, INFO_TAG)
                        .addToBackStack(null).commit();
            }

        });
        ItemTouchHelper.SimpleCallback callback =new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int startPosition = viewHolder.getAdapterPosition();
                int endPosition = target.getAdapterPosition();
                Collections.swap(songs, startPosition, endPosition);
                if(recyclerView.getAdapter()!=null)
                    songAdapter.notifyItemMoved(startPosition,endPosition);
                    saveData();
                return true;
            }
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.delete_song_dialog);
                dialog.setCanceledOnTouchOutside(false);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            Button positive_btn = dialog.findViewById(R.id.yes_delete_btn);
                positive_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    songs.remove(viewHolder.getAdapterPosition());
                    songAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());
                    songAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "The song is deleted! ♫ ", Toast.LENGTH_SHORT).show();
                    saveData();
                    dialog.dismiss();
                }
            });

            Button negative_btn=dialog.findViewById(R.id.cancel_delete_btn);
                negative_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    songAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                    dialog.dismiss();
                    Toast.makeText(MainActivity.this, "Canceled!", Toast.LENGTH_SHORT).show();
                }
            });

                dialog.show();
        }
    };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(songAdapter);
    }

    @Override
    public void addSong(String nameS, String nameA, String link, String imagePath) {
        Fragment fragmentManager = getSupportFragmentManager().findFragmentByTag(ADD_TAG);
        getSupportFragmentManager().beginTransaction().remove(fragmentManager).commit();
        visibility();
        songs.add(new Song(nameS,nameA,link,imagePath));
        saveData();
    }

    private void visibility() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void fun() {
        getFragmentManager().beginTransaction().commit();
    }

    private void loadData() {
        try {
            FileInputStream fis = openFileInput("songList.dat");
            ObjectInputStream ois = new ObjectInputStream(fis);
            songs = (ArrayList<Song>) ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void saveData() {
        try {
            FileOutputStream fos = openFileOutput("songList.dat", MODE_PRIVATE);
            ObjectOutputStream oow = new ObjectOutputStream(fos);
            oow.writeObject(songs);
            oow.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

