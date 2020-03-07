package com.mhimine.jdk.coordapp.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class VideoActivity extends AppCompatActivity {
    public View start;
    public View stop;
    private SurfaceView surfaceView;
    public static final int TAKE_VIDEO= 1;
    MediaRecorder mediaRecorder;
    private android.hardware.Camera camera;
    private boolean isRecord = false;//记录录像状态
    //  private MediaPlayer mediaPlayer;
    private BufferedOutputStream outputStream;
    //  private SurfaceHolder cameraSurfaceHolder;
    private SurfaceHolder.Callback callback;
    private String path;
    Chronometer ch;
    private int flag = 0;
    private Uri mVideoUri;
    private String TAG;
    String cameraPath = Environment.getExternalStorageDirectory() + File.separator
            + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator;//系统相册的路径
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
    Date date = new Date(System.currentTimeMillis());
    String fileName = format.format(date);
    File outputVideo = new File(cameraPath, fileName + ".mp4");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        init();
        initSurfaceView();

    }

    private void initSurfaceView() {
        surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        surfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.setKeepScreenOn(true);
        callback = new SurfaceHolder.Callback() {

            //在控件创建的时候，进行相应的初始化工作
            public void surfaceCreated(SurfaceHolder holder) {
                //打开相机，同时进行各种控件的初始化mediaRecord等
                camera = Camera.open();
                mediaRecorder = new MediaRecorder();
            }

            //当控件发生变化的时候调用，进行surfaceView和camera进行绑定，可以进行画面的显示
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                doChange(holder);
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }

        };
        //为SurfaceView设置回调函数
        surfaceView.getHolder().addCallback(callback);
    }

    private void doChange(SurfaceHolder holder) {
        try {
            camera.setPreviewDisplay(holder);
            //设置surfaceView旋转的角度，系统默认的录制是横向的画面，把这句话注释掉运行你就会发现这行代码的作用
            camera.setDisplayOrientation(getDegree());
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getDegree() {
        //获取当前屏幕旋转的角度
        int rotating = this.getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        //根据手机旋转的角度，来设置surfaceView的显示的角度
        switch (rotating) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    private void init() {
        start = findViewById(R.id.bt_start);
        stop = findViewById(R.id.bt_stop);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecord == false) {
                    stop.setVisibility(View.VISIBLE);
                    start.setVisibility(View.INVISIBLE);
                    startRecord();

                }
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRecord();
                start.setVisibility(View.VISIBLE);
                stop.setVisibility(View.INVISIBLE);
                isRecord = false;
            }
        });
    }

    private void stopRecord() {
        mediaRecorder.stop();//停止视频采集
        mediaRecorder.reset();//删除记录器的配置设置
        //当结束录制之后，就将当前的资源都释放
        mediaRecorder.release();
        camera.stopPreview();
        camera.release();

        mediaRecorder = null;
        //  然后再重新初始化所有的必须的控件和对象
        camera = Camera.open();
        mediaRecorder = new MediaRecorder();
        doChange(surfaceView.getHolder());
        ch.stop();
        ch.setBase(SystemClock.elapsedRealtime());
    }

    private void startRecord() {
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
//        Date date = new Date(System.currentTimeMillis());
//        String fileName = format.format(date);
//        // path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hello.3gp";
//        String bath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/CameraDemo/video/";
//        File file = new File(bath);
//        if (!file.exists()) {
//            boolean flag = file.mkdirs();
//        }
//        path = Environment.getExternalStorageDirectory().getAbsolutePath() +
//                "/CameraDemo/video/" + fileName + ".3gp";
//        //先释放被占用的camera，在将其设置为mediaRecorder所用的camera
//
//        camera.unlock();
//        CamcorderProfile mProfile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
//        mediaRecorder.setCamera(camera);
//
//        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
//        //设置音频的来源  麦克风
//        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        //设置视频的来源
//        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//        mediaRecorder.setOutputFormat(mProfile.fileFormat);
//        mediaRecorder.setAudioEncoder(mProfile.audioCodec);
//        mediaRecorder.setVideoEncoder(mProfile.videoCodec);
//        //  mediaRecorder.setOutputFile("/sdcard/FBVideo.3gp");
//        mediaRecorder.setVideoSize(mProfile.videoFrameWidth, mProfile.videoFrameHeight);
//        mediaRecorder.setVideoFrameRate(mProfile.videoFrameRate);
//        mediaRecorder.setVideoEncodingBitRate(mProfile.videoBitRate);
//        mediaRecorder.setAudioEncodingBitRate(mProfile.audioBitRate);
//        mediaRecorder.setAudioChannels(mProfile.audioChannels);
//        mediaRecorder.setAudioSamplingRate(mProfile.audioSampleRate);
//
//        //设置保存的路径
//        mediaRecorder.setOutputFile(path);
//        //开始录制
//        try {
//            mediaRecorder.prepare();
//            mediaRecorder.start();
//            ch = (Chronometer) findViewById(R.id.test);
//            //设置开始计时时间
//            ch.setBase(SystemClock.elapsedRealtime());
//            //启动计时器
//            ch.start();
////            pause.setEnabled(true);
////            restart.setEnabled(false);
//            start.setEnabled(false);
//            Toast.makeText(VideoActivity.this, "开始录制", Toast.LENGTH_SHORT).show();
//            isRecord = true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent();
        //指定动作，启动相机
        intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Log.d(TAG, "指定启动相机动作，完成。");

        Log.d(TAG, "创建视频文件结束。");
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.d(TAG, "添加权限。");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //做一些处理            //获取uri
            mVideoUri = FileProvider.getUriForFile(this, "czz.provider", outputVideo);
        } else {
            //在版本低于此的时候，做一些处理
            mVideoUri = Uri.fromFile(outputVideo);
        }
        Log.d(TAG, "根据视频文件路径获取uri。");
        //将uri加入到额外数据
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mVideoUri);
        Log.d(TAG, "将uri加入启动相机的额外数据。");
        Log.d(TAG, "启动相机...");
        //启动相机并要求返回结果
        startActivityForResult(intent, TAKE_VIDEO);
        Log.d(TAG, "拍摄中...");


    }


    private void Start() {

        File path = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/CameraDemo/video/ErrorVideo/");
        // File path = new File(getExternalCacheDir() + "/Myvideo/");//视频保存的文件夹
        if (!path.exists()) {
            path.mkdir();
        }
        //  videoFile = path.toString();
//        String filename = "video.mp4";
//        videoFile = new File(path, filename);//视频文件
        mediaRecorder = new MediaRecorder();// 创建mediarecorder对象
        camera.setDisplayOrientation(90);//调整摄像头角度
        camera.unlock();//解锁摄像头
        mediaRecorder.setCamera(camera);//使用摄像头
        //mediaRecorder.reset();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//使用摄像头获取图像
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//设置输出格式
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);//设置编码格式
        mediaRecorder.setVideoEncodingBitRate(1920 * 1080);
        mediaRecorder.setVideoSize(1920, 1080);//视频尺寸
        mediaRecorder.setVideoFrameRate(10);//每秒

        // videoFile = newFileName();
        // mediaRecorder.setOutputFile(videoFile);
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
            Date date = new Date(System.currentTimeMillis());
            String fileName = format.format(date);
            File saveFile = new File(Environment.getExternalStorageDirectory().getCanonicalFile() + "/" + System.currentTimeMillis() + ".mp4");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mediaRecorder.setOutputFile(saveFile.getAbsoluteFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        mediaRecorder.setPreviewDisplay(surfaceView.getHolder().getSurface());
        mediaRecorder.setOrientationHint(90);//调整播放的角度
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Toast.makeText(VideoActivity.this, "开始录制", Toast.LENGTH_SHORT).show();
        isRecord = true;//表示正在录制

    }

    @Override
    protected void onResume() {//当开起焦点的时候开机相机
        super.onResume();
        camera = Camera.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.stopPreview();//停止预览
        camera.release();
        camera = null;
    }

    private void createfile() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        String fileName = format.format(date);
        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fileName + ".mp4";
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
