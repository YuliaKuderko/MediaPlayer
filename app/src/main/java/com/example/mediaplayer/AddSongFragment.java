package com.example.mediaplayer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Objects;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class AddSongFragment extends Fragment {

    private Context context;
    File file;
    SharedPreferences photoNumber;
    Button submit, cancel;
    ImageButton openGallery,openCamera;
    EditText songName,songLink,artistName;
    String nameS,nameA,link;
    final int CAMERA_REQUEST = 1;
    final int WRITE_PERMISSION_REQUEST=1;
    final  int IMAGE_PICK_CODE=1000;
    final  int PERMISSION_CODE=1001;
    String imagePath;
    Uri imageUri;
    ImageView smallImageSong, bigImageSong;
    myInfo callBack;

    public AddSongFragment(Context context){
        this.context=context;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (myInfo) context;
        }catch (ClassCastException ex) {
            throw new ClassCastException("The activity must implement the myInfo intrerface");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    interface  myInfo{
        void addSong(String nameS, String nameA, String link, String imagePath);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            //imagePath = file.getPath();
            Glide
                    .with(AddSongFragment.this)
                    .load(imageUri)
                    .centerCrop()
                    .into(smallImageSong);

        }
        else if ( requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK){
            imageUri = data.getData();
            Glide
                    .with(AddSongFragment.this)
                    .load(imageUri)
                    .centerCrop()
                    .into(smallImageSong );

        }
    }

    public void pickImageFromGallery(){
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate((R.layout.add_song), container, false);
        submit=rootView.findViewById(R.id.submit_btn);
        cancel=rootView.findViewById(R.id.cancel_btn);
        openCamera =rootView.findViewById(R.id.add_camera_pic);
        openGallery =rootView.findViewById(R.id.add_gallery_pic);
        songName=rootView.findViewById(R.id.add_song_name);
        songLink=rootView.findViewById(R.id.add_song_link);
        artistName = rootView.findViewById(R.id.add_artist_name);
        smallImageSong = rootView.findViewById(R.id.show_image);
        bigImageSong=rootView.findViewById((R.id.song_image));


        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randNumber = new Random().nextInt();
                String photoName =String.valueOf(randNumber);
                String finalName = "pic"+photoName+".jpg";
                file=new File(getActivity().getExternalFilesDir(null),finalName);
                imageUri = FileProvider.getUriForFile(context, "com.example.mediaplayer.provider", file);
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent,CAMERA_REQUEST);

            }
        });
        if (Build.VERSION.SDK_INT >= 23) {
            int hasWritePermission = PermissionChecker.checkSelfPermission(context,Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (hasWritePermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION_REQUEST);
            }
        }
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
                {
                    if(PermissionChecker.checkSelfPermission(context,Manifest.permission.READ_EXTERNAL_STORAGE)==PermissionChecker.PERMISSION_DENIED){
                        String [] permission={Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permission, PERMISSION_CODE);
                    }
                } pickImageFromGallery();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameS=songName.getText().toString();
                nameA= artistName.getText().toString();
                link =songLink.getText().toString();
                if(nameS!=null&&nameA!=null&&link!=null&&imageUri!=null){
                    callBack.addSong(nameS,nameA,link,imageUri.toString());
                    rootView.setVisibility(View.VISIBLE);
                }
                else{
                    Toast.makeText(context, "You have to take a photo or pick one from your gallery ↑ ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
