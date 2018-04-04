package com.example.administrator.bishe;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import butterknife.BindView;

public class VideoPage extends AppCompatActivity {
    @BindView(R.id.videoView) public VideoView videoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Uri uri = Uri.parse(getString(R.string.appPath)+"/material/环保小视频.mp4");
        //设置视频控制器
        videoView.setMediaController(new MediaController(this));
        //播放完成回调
        videoView.setOnCompletionListener(new MyPlayerOnCompletionListener());
        //设置视频路径
        videoView.setVideoURI(uri);

        //开始播放视频
        videoView.start();
    }
    class MyPlayerOnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {
            Toast.makeText( VideoPage.this, "播放完成了", Toast.LENGTH_SHORT).show();
        }
    }
}
