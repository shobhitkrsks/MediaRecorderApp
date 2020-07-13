package com.example.mediarecorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button startbtn,stopbtn;
    MediaRecorder mediaRecorder;
    int permreqcode=200;
    RecyclerView audiofilesrv;

    ArrayList<AudioFile> audiolist= new ArrayList<>();
    AudioListViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startbtn=findViewById(R.id.startbtn);
        stopbtn=findViewById(R.id.stopbtn);

        startbtn.setEnabled(true);
        stopbtn.setEnabled(false);

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecording();
            }
        });
        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecording();
            }
        });

        audiofilesrv =findViewById(R.id.filesrv);
        populateFileList();
        for(AudioFile audioFile:audiolist)
        {
            Log.e("File found",audioFile.getName());
        }
        adapter=new AudioListViewAdapter(audiolist);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false);
        audiofilesrv.setLayoutManager(layoutManager);
        audiofilesrv.setItemAnimator(new DefaultItemAnimator());
        audiofilesrv.setAdapter(adapter);

    }

    private void stopRecording() {
        try {
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder=null;
            startbtn.setEnabled(true);
            stopbtn.setEnabled(false);
            adapter.notifyDataSetChanged();

            Toast.makeText(getApplicationContext(),"Recording successfully stopped",Toast.LENGTH_SHORT).show();

        } catch (Exception e)
        {
            Toast.makeText(getApplicationContext(),"Error on stopping recording",Toast.LENGTH_SHORT).show();
        }
    }

    private void startRecording() {
        if(checkPermission())
        {
            mediaRecorder=new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

            String path=getExternalFilesDir(STORAGE_SERVICE).getAbsolutePath()+"/AudioFile_"+System.currentTimeMillis()+".3gp";

            mediaRecorder.setOutputFile(path);
            Log.e("Filepath",path);

            try {
                mediaRecorder.prepare();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(),"Prepare failed",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

            try {
                mediaRecorder.start();
                startbtn.setEnabled(false);
                stopbtn.setEnabled(true);
                Toast.makeText(getApplicationContext(),"Recording started",Toast.LENGTH_SHORT).show();

            } catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Recording not started",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE},permreqcode);
        }
    }

    private void populateFileList() {
        String path=getExternalFilesDir(STORAGE_SERVICE).getAbsolutePath();
        File dir=new File(path);
        File files[]=dir.listFiles();

        for(File file:files)
        {
            MediaMetadataRetriever mediaMetadataRetriever=new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
            String duration=mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            Long length=Long.parseLong(duration);
            audiolist.add(new AudioFile(file.getName(),file.getAbsolutePath(),length/1000));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==permreqcode)
        {
            if(grantResults.length>=0 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED && grantResults[2]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"All permissions granted",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"All permissions not granted",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkPermission() {
        return (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED);
    }


}