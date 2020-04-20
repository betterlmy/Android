package com.example.week8;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class thing_content extends AppCompatActivity {

    //绑定控件
    private EditText text;
    private EditText start_date;
    private EditText end_date;
    private DatePicker datePicker;
    private EditText rate;
    private Button submit_btn;
    private ImageView image;
    private byte[] imageData;
    private EditText date;//用于判断开始或结束


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thing_content);
        bind();
        initListener();
    }

    private void bind() {
        //绑定控件
        text = findViewById(R.id.context);//内容
        start_date = findViewById(R.id.text_startDate);//开始日期
        end_date = findViewById(R.id.text_endDate);//结束日期
        datePicker = findViewById(R.id.datepicker);//日期选择
        rate = findViewById(R.id.text_process);//进度
        submit_btn = findViewById(R.id.btn_submit);//确定按钮
        image = findViewById(R.id.text_image);//照片
        start_date.setFocusable(false); //失去焦点，点击日期文本框不在弹出输入法
        end_date.setFocusable(false);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initListener() {
        //初始化监听器
        submit_btn.setOnClickListener(complete);//给按钮添加完成监听
        start_date.setOnClickListener(setstartDate);
        end_date.setOnClickListener(setendDate);
        image.setOnClickListener(setImg);
        datePicker.setOnDateChangedListener(dateChanged);
    }

    View.OnClickListener setImg = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            checkPermissionAndCamera();
        }
    };

    View.OnClickListener complete = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //获取变量值
            String thing_name = String.valueOf(text.getText());//获取事件名
            String thing_startDate = String.valueOf(start_date.getText());//获取开始时间
            String thing_endDate = String.valueOf(end_date.getText());//获取结束时间
            int thing_rate = Integer.parseInt(String.valueOf(rate.getText()));//获取完成度的值
            if (thing_rate > 100 || thing_rate < 0) {
                //判断格式
                Toast.makeText(thing_content.this, "格式错误", Toast.LENGTH_SHORT).show();
            } else {
                Bundle bundle = new Bundle();
                bundle.putString("thing_name", thing_name);
                bundle.putString("thing_startDate", thing_startDate);
                bundle.putString("thing_endDate", thing_endDate);
                bundle.putInt("thing_rate", thing_rate);
                try {
                    bundle.putString("imageURI", String.valueOf(mCameraUri));
                }catch (Exception e){
                    bundle.putByteArray("imageURI", null);
                    Log.v("拍照","传参错误");
                }
                Message msg = new Message();
                msg.setData(bundle);
                //传参
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(1, intent);
                //传回1 代码1表示添加且添加成功
                finish();
            }
        }
    };

    View.OnClickListener setstartDate = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            datePicker.setVisibility(View.VISIBLE);//日历设置不可见
            datePicker.bringToFront();//把日历显示到最前方，防止显示冲突
            date = start_date;
        }
    };
    View.OnClickListener setendDate = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datePicker.setVisibility(View.VISIBLE);//日历设置不可见
            datePicker.bringToFront();//把日历显示到最前方，防止显示冲突
            date = end_date;
        }
    };
    public DatePicker.OnDateChangedListener dateChanged = new DatePicker.OnDateChangedListener() {
        //点击日历后把选定的日期转换成字符串写入到日期文本中去
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            datePicker.setVisibility(View.INVISIBLE);//设置完后不可见
        }
    };

    /**
     * 检查权限并拍照。
     * 调用相机前先检查权限。
     */
    // 申请相机权限的requestCode
    private static final int PERMISSION_CAMERA_REQUEST_CODE = 0x00000012;
    private static final int CAMERA_REQUEST_CODE = 10;

    private void checkPermissionAndCamera() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        if (hasCameraPermission == PackageManager.PERMISSION_GRANTED) {
            //有调起相机拍照。
            openCamera();
        } else {
            //没有权限，申请权限。
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},
                    PERMISSION_CAMERA_REQUEST_CODE);
        }
    }

    /**
     * 处理权限申请的回调。
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSION_CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //允许权限，有调起相机拍照。
                openCamera();
            } else {
                //拒绝权限，弹出提示框。
                Toast.makeText(this, "拍照权限被拒绝", Toast.LENGTH_LONG).show();
            }
        }
    }
    //用于保存拍照图片的uri
    private Uri mCameraUri;
    private void openCamera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = createImageUri();
            if (photoFile != null) photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
            mCameraUri = photoUri;
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    private Uri createImageUri() {
        //创建图片地址uri,用于保存拍照后的照片 Android 10以后使用这种方法
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                image.setImageURI(mCameraUri);
                Log.v("拍照", String.valueOf(mCameraUri));
            } else {
                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
            }
        }
    }
}
