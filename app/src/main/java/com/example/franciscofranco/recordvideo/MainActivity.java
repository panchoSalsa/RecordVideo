package com.example.franciscofranco.recordvideo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_TAKE_VIDEO = 1;
    public static final String AUTHORITY = "com.example.franciscofranco.recordvideo";
    private VideoView videoView;
    private Uri videoUri;
    private MediaController mediaController; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoView = (VideoView) findViewById(R.id.videoView);
        mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
    }

    public void recordVideo(View v) {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            File videoFile = createVideoFile();
            if (videoFile != null) {
                videoUri = FileProvider.getUriForFile(this, AUTHORITY,videoFile);

                if (videoUri == null)
                    return;

                takeVideoIntent.putExtra(MediaStore.EXTRA_OUTPUT, videoUri);
//                Log.d("FRANCO_DEBUG", videoFile.toString());
//                Log.d("FRANCO_DEBUG", videoUri.toString());
                startActivityForResult(takeVideoIntent, REQUEST_TAKE_VIDEO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_VIDEO && resultCode == RESULT_OK) {
            videoView.setVideoURI(videoUri);
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                }
            });
        }
    }

    public File createVideoFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String videoFileName = "MP4_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
        File videoFile = null;
        try {
            videoFile = File.createTempFile(videoFileName, ".mp4", storageDir);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return videoFile;
    }

}
