package com.mhimine.jdk.coordapp.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mhimine.jdk.coordapp.R;
import com.mhimine.jdk.coordapp.Utils.Utils;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
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
    private ImageView picture;
    private Uri imageUri;
    Bitmap bitmap;

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
        ImageButton  btn_record = (ImageButton) findViewById(R.id.btn_voice);
        ImageButton btn_video = (ImageButton) findViewById(R.id.btn_video);
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
                if (ContextCompat.checkSelfPermission(DeviceDetailsActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(DeviceDetailsActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                } else {
                    openAlbm();

                }
                return true;
            }
        });

        btn_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceDetailsActivity.this, RecordActivity.class);
                startActivity(intent);
            }
        });
        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DeviceDetailsActivity.this, VideoActivity.class);
                startActivity(intent);
            }
        });
        ImageButton btn_voice= (ImageButton) findViewById(R.id.btn_voice);
        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(DeviceDetailsActivity.this,RecordActivity.class);
                startActivity(intent);
            }
        });
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
            default:
                break;
        }
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            //如果是document类型的Uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.document".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        selection);
                //  Log.i(imagePath, "相册选择1");
            } else if ("com.android.providers.downloads.documents".equals
                    (uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse(
                        "content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
                //  Log.i(imagePath, "相册选择2");
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方法处理
            imagePath = getImagePath(uri, null);
            // Log.i(imagePath, "相册选择3");
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
            //  Log.i(imagePath, "相册选择4");
        }
        displayImage(imagePath);
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

    //弹出按钮框
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
                    case R.id.choose:
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
        //关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                Toast.makeText(view.getContext(), "close", Toast.LENGTH_SHORT).show();
            }
        });        //显示菜单，不要少了这一步
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

    private void takePhoto() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA);
        Date date = new Date(System.currentTimeMillis());
        String fileName = format.format(date);
        File outputImage = new File(getExternalCacheDir(), fileName + ".jpg");
        //File outputImage = new File(imageFilePath, fileName + ".jpg");
        int currentapiVersion = Build.VERSION.SDK_INT;
        if (currentapiVersion > 24) {
            imageUri = FileProvider.getUriForFile(DeviceDetailsActivity.this, "czz.provider", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        try {
            Intent intent = new Intent((MediaStore.ACTION_IMAGE_CAPTURE));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权
            startActivityForResult(intent, TAKE_PHOTO);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
