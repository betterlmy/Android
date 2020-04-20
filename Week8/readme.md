# 第八-十周作业 


## 要求
#### 在前上次TODO app程序代码基础上，增加以下内容<br><br>
#### 为TODO的事项添加截止期，如果app运行时待办事项截止期过期，则todo事项用红色字体标注<br><br>
#### 采用sqlite对todo事项进行存储，允许app重新启动后能够重新加载之前编辑的todo事项<br><br>
#### todo事项添加拍照功能，即在编辑todo事项时增加一个图片信息。这个图片信息同样要求采用sqlite进行存储，并允许app重新启动后能够重新加载
<br>
## 已知BUG<br>

#### $\color{red}{1.拍照为黑白色 2.超时红色删除掉后，红色会保留至下一个事件}$


## 知识点
### 1.sqlite增删查改
##### a.databaseHelper<br>
```java
dbHelper = new DatabaseHelper(this, "thing.db");
db = dbHelper.getWritableDatabase();
```
         
##### b.增加
```java
Thing new_thing = new Thing(thing_name, thing_startDate, thing_endDate, thing_rate, thing_imgbyte,number1);
        ContentValues values = new ContentValues();
        values.put("name", thing_name);
        values.put("start_date",sdf.format(thing_startDate));
        values.put("end_date", sdf.format(thing_endDate));
        values.put("rate", thing_rate);
        values.put("image", String.valueOf(thing_imgbyte));
        values.put("number",number1);
        db.insert("table_Thing", null, values);
```        
##### c.删除<br>
```java
            db.delete("table_Thing","name=?",new String[]{mThingList.get(holder.getAdapterPosition()).getthing_name()});
```  
##### d.修改<br>
```java
db.update("table_Thing",values,"number=?",new String[]{mthingList.get(position1).getNumber2Str()});
```           

### 2.拍照功能的实现（安卓10 Q）
```java
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
```   
