package com.example.mediarecorderapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button startbtn,stopbtn;
    MediaRecorder mediaRecorder;
    int permreqcode=200;

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
                if(checkPermission())
                {
                    mediaRecorder=new MediaRecorder();
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mediaRecorder.setOutputFile(Environment.getExternalStorageDirectory().getAbsolutePath()+"/AudioFile.3gp");

                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        Toast.makeText(getApplicationContext(),"Prepare failed",Toast.LENGTH_SHORT).show();
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
                    requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO},permreqcode);
                }
            }
        });

        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mediaRecorder.stop();
                    mediaRecorder.release();
                    mediaRecorder=null;
                    startbtn.setEnabled(true);
                    stopbtn.setEnabled(false);
                    Toast.makeText(getApplicationContext(),"Recording successfully stopped",Toast.LENGTH_SHORT).show();

                } catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),"Error on stopping recording",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==permreqcode)
        {
            if(grantResults.length>=2 && grantResults[0]==PackageManager.PERMISSION_GRANTED && grantResults[1]==PackageManager.PERMISSION_GRANTED)
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
        return (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.RECORD_AUDIO)==PackageManager.PERMISSION_GRANTED);
    }


}