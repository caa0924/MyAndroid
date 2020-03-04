package com.mhimine.jdk.coordapp.Activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.mhimine.jdk.coordapp.ObjectClass.RecordPlayer;
import com.mhimine.jdk.coordapp.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RecordActivity extends Activity implements View.OnClickListener {
    // 开始录音
    private Button start;
    // 停止按钮
    private Button stop;
    // 播放按钮
    private Button paly;
    // 暂停播放
    private Button pause_paly;
    // 停止播放
    private Button stop_paly;
    // 录音类
    private MediaRecorder mediaRecorder;
    // 以文件的形式保存
    private File recordFile;
    private boolean isRecord=false;
    private RecordPlayer player;

    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //在sd根目录创建文件夹
        String bath=Environment.getExternalStorageDirectory().getAbsolutePath() + "/Cvoice/";
        File file = new File(bath);
        if (!file.exists()) {
            boolean flag = file.mkdirs();
        }
       // recordFile = new File("/mnt/sdcard/Cvoice", fileName+"check.amr");
        initView();
        Listener();

        //开始录音
        //语音操作对象


    }

    private void Listener() {
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        paly.setOnClickListener(this);
        pause_paly.setOnClickListener(this);
        stop_paly.setOnClickListener(this);
    }

    private void initView() {
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        paly = findViewById(R.id.paly);
        pause_paly = findViewById(R.id.pause_paly);
        stop_paly = findViewById(R.id.stop_paly);
    }

    @Override
    public void onClick(View v) {
        player = new RecordPlayer(RecordActivity.this);
        int Id = v.getId();

        switch (Id) {
            case R.id.start:
                startRecording();
                break;
            case R.id.stop:
                stopRecording();
                break;
            case R.id.paly:
                playRecording();
                break;
            case R.id.pause_paly:
                pauseplayer();
                break;
            case R.id.stop_paly:
                stopplayer();
                break;
        }
    }

    private void startRecording() {
        mediaRecorder = new MediaRecorder();
        //创建音频文件
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        String fileName = format.format(date);
        recordFile = new File("/mnt/sdcard/Cvoice", fileName+"check.amr");
        // 判断，若当前文件已存在，则删除
        if (recordFile.exists()) {
            recordFile.delete();
        }
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }catch (IllegalStateException e){
            e.printStackTrace();
        }

        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
       mediaRecorder.setOutputFile(recordFile.getAbsolutePath());
     //   mediaRecorder.setOutputFile(path);
        try {
            // 准备好开始录音
            mediaRecorder.prepare();

            mediaRecorder.start();
            isRecord=true;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void stopRecording() {
//        if (recordFile != null) {
        if(isRecord==true){
            mediaRecorder.stop();
            mediaRecorder.release();
        }
    }

    private void playRecording() {
        player.playRecordFile(recordFile);

    }


    private void pauseplayer() {
        player.pausePalyer();
    }

    private void stopplayer() {
        // TODO Auto-generated method stub
        player.stopPalyer();
    }


}
