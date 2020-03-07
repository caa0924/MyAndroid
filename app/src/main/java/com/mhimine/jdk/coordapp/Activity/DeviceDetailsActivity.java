package com.mhimine.jdk.coordapp.Activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.AndroidException;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Utils;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DeviceDetailsActivity extends AppCompatActivity {
    String namespace = "http://tempuri.org/";
    String Url = "http://47.92.68.57:8099/WebServices_Device_Management.asmx?WSDL";
    String methodName = "SelectByDeviceId";
    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    public static final int TAKE_VIDEO = 3;
    public static final int VOICE_RESULT_CODE = 4;
    private ImageView picture;
    private Uri imageUri;
    private Uri mVideoUri;
    Bitmap bitmap;
    private String TAG;
    String cameraPath = Environment.getExternalStorageDirectory() + File.separator
            + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator;//系统相册的路径
    //Environment.getExternalStorageDirectory().getAbsolutePath() + "/CameraDemo/video/";
    String voicePath = Environment.getExternalStorageDirectory() + File.separator
            + "Sounds" + File.separator;//系统录音的路径
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
    Date date = new Date(System.currentTimeMillis());
    String fileName = format.format(date);
    File outputImage = new File(cameraPath, fileName + ".JPEG");
    File outputVideo = new File(cameraPath, fileName + ".mp4");
    private ImageButton main_ib;

    private SeekBar main_sb;

    private MediaPlayer mediaPlayer;
    private Object DeviceDetailsActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_device_details);
        Bundle userList = getIntent().getExtras();
        String message = userList.getString("equip_code");
        Map<String, Object> params = new HashMap<>();
        params.put("equip_number", message);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        SoapObject soapObject = Utils.callWS(namespace, methodName,
                Url, params);
        if (soapObject != null) {

            String detail = soapObject.getProperty("SelectByDeviceIdResult").toString();
            try {
                //将JSON字符串转换为List的结构
                List<Map<String, Object>> list = Utils.convertJSON2List(
                        detail, "Result_List", new String[]{"equip_name",
                                "factory_number"});
                TextView textView_device_name = (TextView) findViewById(R.id.device_name);
                TextView textView_number = (TextView) findViewById(R.id.number);
                textView_device_name.setText(list.get(0).get("equip_name").toString().trim());
                textView_number.setText(list.get(0).get("factory_number").toString().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("This is null...");
        }
        TextView tv_details = (TextView) this.findViewById(R.id.tv_details);
        tv_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(DeviceDetailsActivity.this, DeviceManagerDetailActivity.class);
                startActivity(i);
            }
        });
        final ImageButton btn_cream = (ImageButton) findViewById(R.id.btn_cream);
        picture = (ImageView) findViewById(R.id.picture);
        final ImageButton btn_video = (ImageButton) findViewById(R.id.btn_video);
        btn_cream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // String imageFilePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                showPopupMenu(btn_cream);
            }
        });
        btn_cream.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //如果没有权限则申请权限
                if (ContextCompat.checkSelfPermission(DeviceDetailsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DeviceDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbm();//调用打开相册

                }
                return true;
            }
        });

        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   startVideo();
                showVideoMenu(btn_video);
//                Intent intent = new Intent(DeviceDetailsActivity.this, VideoActivity.class);
//                startActivity(intent);
            }
        });
        final ImageButton btn_voice = (ImageButton) findViewById(R.id.btn_voice);
        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showVoiceMenu(btn_voice);
                //startRecord();
//                Intent intent = new Intent(DeviceDetailsActivity.this, RecordActivity.class);
//                startActivity(intent);
            }
        });

    }

    //创建一个线程
    class myThread extends Thread {
        @Override
        public void run() {
            //判断当前的位置是不是小于播放总长
            while (main_sb.getProgress() <= main_sb.getMax()) {
                //得到当前音频播放位置
                // 设置滚动条当前位置
                main_sb.setProgress(mediaPlayer.getCurrentPosition());
            }
        }
    }


    private void startRecord() {
        Intent intent = new Intent();
        intent.setAction(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        createVoiceFile();
        Log.d(TAG, "创建录音文件");
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Log.d(TAG, "启动系统录音机，开始录音...");
        startActivityForResult(intent, VOICE_RESULT_CODE);


    }

    /**
     * 创建音频目录
     */
    private void createVoiceFile() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        String fileName = format.format(date);
        File recordFile = new File(Environment.getExternalStorageDirectory() + File.separator
                + "Sounds" + File.separator, fileName + "check.amr");
        recordFile.setWritable(true);
        // 判断，若当前文件已存在，则删除
        if (recordFile.exists()) {
            recordFile.delete();
        }
    }


    private void openAlbm() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbm();
                } else {
                    Toast.makeText(this, "你拒绝了权限许可", Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        switch (requestCode) {
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        //4.4及以上系统使用该方法处理图片

                        handleImageOnKitKat(data);
                    } else {
                        //4.4以下系统使用该方法处理图片
                        handleImageBeforeKitKat(data);
                    }
                }
                break;

            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {

                    Uri uri = Uri.fromFile(outputImage);
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(uri);
                    this.sendBroadcast(intent);  // 这里我们发送广播让MediaScanner 扫描我们制定的文件

                }
                break;
            case TAKE_VIDEO:
                if (resultCode == RESULT_OK) {

                    Uri uri = Uri.fromFile(outputVideo);
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(uri);
                    this.sendBroadcast(intent);  // 这里我们发送广播让MediaScanner 扫描我们制定的文件
                }
                break;
            case 6:
                if (resultCode == RESULT_OK) {
                    VideoView videoView = (VideoView) findViewById(R.id.videoView);
                    Uri uri = data.getData();
                    videoView.setVideoURI(uri);//将选择的文件路径给播放器
                    videoView.setMediaController(new MediaController(this));
                    videoView.requestFocus();
                    videoView.start();
                }
                break;
            case 7:
                if (resultCode == RESULT_OK) {
                   // VideoView videoView = (VideoView) findViewById(R.id.videoView);
                    final Uri uri = data.getData();
//                    videoView.setVideoURI(uri);//将选择的文件路径给播放器
//                    videoView.setMediaController(new MediaController(this));
//                    videoView.requestFocus();
//                    videoView.start();
                    //得到按钮对象
                    main_ib = (ImageButton) findViewById(R.id.main_ib);
                    //设置播放图标
                    main_ib.setImageResource(android.R.drawable.ic_media_play);
                    //实现播放
                    main_ib.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //判断是否为第一次
                            if (mediaPlayer == null) {
                                //开始创建MediaPlayer //pp为音频文件名
//                                try {
                                 //  mediaPlayer.setDataSource(DeviceDetailsActivity.this,uri);
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
                                mediaPlayer = MediaPlayer.create(v.getContext(), uri);
                                //如果为第一次则播放
                                mediaPlayer.start();
                                main_ib.setImageResource(android.R.drawable.ic_media_pause);
                                //获取当前音频时长   //把最大音频时长赋给滚动条
                                main_sb.setMax(mediaPlayer.getDuration());
                                //调用线程（start启动线程）
                                new myThread().start();
                            } else if (mediaPlayer.isPlaying()) {
                                //如果处于正在播放状态中，则暂停
                                mediaPlayer.pause();
                                main_ib.setImageResource(android.R.drawable.ic_media_play);

                            } else {
                                //暂停后点击为播放
                                mediaPlayer.start();
                                main_ib.setImageResource(android.R.drawable.ic_media_pause);
                            }
                        }
                    });
                    //得到滚动条对象
                    main_sb = (SeekBar) findViewById(R.id.main_sb);
                    //滚动条实现快进效果
                    main_sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override            //值改变
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                        }

                        @Override
                        //开始点击
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        //结束点击
                        public void onStopTrackingTouch(SeekBar seekBar) {
                            //得到当前播放时长，赋值给mediaPlayer
                            mediaPlayer.seekTo(main_sb.getProgress());
                        }
                    });

                }
                break;
            default:
                break;
        }
    }


    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagesPath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagesPath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);

            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagesPath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagesPath = uri.getPath();
        }
        displayImage(imagesPath);
    }


    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        // Log.i(imagePath, "相册选择5");
        displayImage(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过uri和selection来获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.
                        Media.DATA));
            }
            cursor.close();
        }
        // Log.i(path, "相册选择");
        return path;
    }

    private void displayImage(String imagePath) {
        if (imagePath != null) {
            //Bitmap
            bitmap = BitmapFactory.decodeFile(imagePath);
            picture.setImageBitmap(bitmap);
        } else {
            Toast.makeText(this, "failed to get image", Toast.LENGTH_LONG).show();
        }
    }

    //点击相机弹出按钮框
    private void showPopupMenu(final View view) {
        final PopupMenu popupMenu = new PopupMenu(this, view);
        //menu 布局
        popupMenu.getMenuInflater().inflate(R.menu.cream_menu, popupMenu.getMenu());
        //点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.take:
                        takePhoto();
                        break;
                    case R.id.choosePhoto:
                        choosePicture();
                        break;
//                    case R.id.exit:
//                        popupMenu.dismiss();
//                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        //显示菜单，不要少了这一步
        popupMenu.show();
    }

    private void choosePicture() {
        if (ContextCompat.checkSelfPermission(DeviceDetailsActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(DeviceDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            openAlbm();
        }
    }

    //点击录像弹出按钮框
    private void showVideoMenu(final View view) {
        final PopupMenu popupMenu = new PopupMenu(this, view);
        //menu 布局
        popupMenu.getMenuInflater().inflate(R.menu.video_menu, popupMenu.getMenu());
        //点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.takeVideo:
                        startVideo();
                        break;
                    case R.id.chooseVideo:
                        chooseVideo();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        //显示菜单，不要少了这一步
        popupMenu.show();
    }
    //点击录音弹出按钮框
    private void showVoiceMenu(final View view) {
        final PopupMenu popupMenu = new PopupMenu(this, view);
        //menu 布局
        popupMenu.getMenuInflater().inflate(R.menu.voice_menu, popupMenu.getMenu());
        //点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.takeVoice:
                        startRecord();
                        break;
                    case R.id.chooseVoice:
                        chooseVoice();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        //显示菜单，不要少了这一步
        popupMenu.show();
    }

    private void chooseVoice() {
        Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        //intent.setType("image/*");
         intent.setType("audio/*"); //选择音频
        //intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）

       // intent.setType("video/*;image/*");//同时选择视频和图片

        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);

        /* 取得相片后返回本画面 */
        startActivityForResult(intent, 7);
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        //intent.setType("image/*");
        // intent.setType("audio/*"); //选择音频
        //intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）

        intent.setType("video/*;image/*");//同时选择视频和图片

        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);

        /* 取得相片后返回本画面 */
        startActivityForResult(intent, 6);

    }

    private void takePhoto() {
//        String cameraPath = Environment.getExternalStorageDirectory() + File.separator
//                + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator;//系统相册的路径
//        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
//        Date date = new Date(System.currentTimeMillis());
//        String fileName = format.format(date);
        // File outputImage = new File(cameraPath, fileName + ".JPEG");//getExternalCacheDir()
        //File outputImage = new File(imageFilePath, fileName + ".jpg");
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion > 24) {
            imageUri = FileProvider.getUriForFile(DeviceDetailsActivity.this, "czz.provider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        try {
            MediaStore.Images.Media.insertImage(getContentResolver(), cameraPath, fileName, null);
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权
            Uri uri = Uri.fromFile(outputImage);
            intent.setData(uri);
            this.sendBroadcast(intent);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//打开系统相机
        try {
            Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权
            startActivityForResult(intent, TAKE_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void startVideo() {

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
}
