package com.example.myapplication11;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ImageView ivAlbumCover;

    private MediaPlayer mediaPlayer;
    private ImageButton btnPrevious;
    private ImageButton btnPlayPause;
    private ImageButton btnNext;
    private TextView tvSongTitle;
    private SeekBar seekBar;
    private List<Song> songList;
    private int currentSongIndex = 0;
    // 歌曲数据结构
    private static class Song {
        private String songTitle;
        private int audioResourceId;
        private int coverResourceId;

        public Song(String songTitle, int audioResourceId, int coverResourceId) {
            this.songTitle = songTitle;
            this.audioResourceId = audioResourceId;
            this.coverResourceId = coverResourceId;
        }

        public int getAudioResourceId() {
            return audioResourceId;
        }
        public String getSongTitle() {
            return songTitle;
        }
        public int getCoverResourceId() {
            return coverResourceId;
        }
    }

    private void playPreviousSong() {
        currentSongIndex--;
        if (currentSongIndex < 0) {
            currentSongIndex = songList.size() - 1;
        }
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(this, songList.get(currentSongIndex).getAudioResourceId());
        mediaPlayer.start();
        tvSongTitle.setText(songList.get(currentSongIndex).getSongTitle());
        ivAlbumCover.setImageResource(songList.get(currentSongIndex).getCoverResourceId());
    }
    private void playNextSong() {
        currentSongIndex++;
        if (currentSongIndex >= songList.size()) {
            currentSongIndex = 0;
        }
        mediaPlayer.release();
        mediaPlayer = MediaPlayer.create(this, songList.get(currentSongIndex).getAudioResourceId());
        mediaPlayer.start();
        tvSongTitle.setText(songList.get(currentSongIndex).getSongTitle());
        ivAlbumCover.setImageResource(songList.get(currentSongIndex).getCoverResourceId());
    }
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        songList = new ArrayList<>();
        songList.add(new Song("黑夜问白天", R.raw.ljj, R.drawable.ljj));
        songList.add(new Song("传奇", R.raw.wjw, R.drawable.wjw));
        songList.add(new Song("青春修炼手册", R.raw.wyt, R.drawable.wyt));
        songList.add(new Song("他说", R.raw.dzq, R.drawable.dzq));
// 添加更多歌曲...

        ivAlbumCover = findViewById(R.id.ivAlbumCover);

        btnPrevious = findViewById(R.id.btnPrevious);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnNext = findViewById(R.id.btnNext);
        tvSongTitle = findViewById(R.id.tvSongTitle);
        seekBar = findViewById(R.id.seekBar);

        // 初始化MediaPlayer
        mediaPlayer = MediaPlayer.create(this, songList.get(currentSongIndex).getAudioResourceId());
        // 设置当前歌曲标题
        tvSongTitle.setText(songList.get(currentSongIndex).getSongTitle());
        // 初始化视图
        ivAlbumCover.setImageAlpha(0);




        // 设置SeekBar的最大值
        seekBar.setMax(mediaPlayer.getDuration());
// 初始化歌单列表


        // 设置按钮点击事件监听器
        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 实现上一曲功能
                playPreviousSong();
            }
        });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 实现播放/暂停功能
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    btnPlayPause.setImageResource(R.drawable.ic_play);
                } else {
                    mediaPlayer.start();
                    btnPlayPause.setImageResource(R.drawable.ic_pause);
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 实现下一曲功能
                playNextSong();
            }
        });

        // 设置SeekBar的进度变化监听器
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 按下SeekBar时的操作（可选）
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 松开SeekBar时的操作（可选）
            }
        });

        // 创建定时任务以更新SeekBar的进度
        updateSeekBar();
    }

    private void updateSeekBar() {
        // 使用Handler定时更新SeekBar的进度
        final android.os.Handler handler = new android.os.Handler();
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    int currentPosition = mediaPlayer.getCurrentPosition();
                    seekBar.setProgress(currentPosition);
                }
                handler.postDelayed(this, 1000); // 1000毫秒即1秒更新一次
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 释放MediaPlayer资源
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}