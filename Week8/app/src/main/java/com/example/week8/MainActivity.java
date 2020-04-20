package com.example.week8;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
//        为TODO的事项添加截止期，如果app运行时待办事项截止期过期，则todo事项用红色字体标注

//        采用sqlite对todo事项进行存储，允许app重新启动后能够重新加载之前编辑的todo事项
//
//        todo事项添加拍照功能，即在编辑todo事项时增加一个图片信息。这个图片信息同样要求采用sqlite进行存储，并允许app重新启动后能够重新加载
//
//        本次作业计入两次作业成绩，其中，1，2为一次作业成绩，3为一次作业成绩，绩点要求：
//
//        1，2：截止日期添加3分，sqlite存储5分，sqlite加载1分，整体完成度1分
//
//        3：拍照功能实现6分，sqlite加载图片4分
public class MainActivity extends AppCompatActivity {

    public static DatabaseHelper dbHelper;
    public static SQLiteDatabase db;//sqlite型数据库 db
    private Button bt_add;
    private RecyclerView recyclerView;
    private Bundle bundle;
    public int position_current;
    final int add_new = 1;
    final int change = 2;
    final int nochange = 3;
    private String thing_name;
    private Date thing_startDate;
    private Date thing_endDate;
    private int thing_rate;
    private String thing_imgURI;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private int number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this, "thing.db");
        db = dbHelper.getWritableDatabase();
        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);//创建新的布局管理
        recyclerView.setLayoutManager(layoutManager);//在recycler中应用该布局管理器
        recyclerView.setAdapter(adapter);//在recycler中应用该适配器
        number=0;
        initView();//初始化视图
        getDatafromDB();//获取数据
    }

    public void getDatafromDB() {
        mthingList.clear();
        Log.v("mthingList(0)", String.valueOf(mthingList.isEmpty()));
        //从数据库中拿数据
        Cursor cursor = db.query("table_Thing", null, null, null, null, null, null);
        //向table_Thing表中查询
        if (cursor.moveToFirst()) {//查询是否有记录
            do {
                try {
                    Date start_date1 = sdf.parse(cursor.getString(cursor.getColumnIndex("start_date")));
                    Date end_date1 = sdf.parse(cursor.getString(cursor.getColumnIndex("end_date")));
                    String name1 = cursor.getString(cursor.getColumnIndex("name"));
                    int rate1 = cursor.getInt(cursor.getColumnIndex("rate"));
                    String image1 = cursor.getString(cursor.getColumnIndex("image"));
                    Uri uri1=Uri.parse(image1);
                    //查询数据
                    Thing thing1 = new Thing(name1, start_date1, end_date1, rate1, uri1,number);
                    mthingList.add(thing1);//添加一列
                }catch (Exception e){
                    Log.v("日期转换失败","1");
                }

            } while (cursor.moveToNext());  //查询是否还有记录，继续添加
        }
        Log.v("mthingList", String.valueOf(mthingList.size()));
        Collections.sort(mthingList, new Comparator<Thing>() {
            //list中按照sort属性的大小升序
            @Override
            public int compare(Thing o1, Thing o2) {
                Log.v("o1.getSort()", String.valueOf(o1.getSort()));
                Log.v("o2.getSort()", String.valueOf(o2.getSort()));
                return o1.getSort()-o2.getSort();
            }
        });
        adapter.notifyDataSetChanged();
        cursor.close();
    }

    List<Thing> mthingList = new ArrayList<>();
    public ThingAdapter adapter = new ThingAdapter(mthingList, MainActivity.this);


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == add_new) {
            //设置结果显示框的显示数值
            try {
                bundle = data.getExtras();
                thing_name = bundle.getString("thing_name");
                String start = bundle.getString("thing_startDate");
                String end = bundle.getString("thing_endDate");
                thing_startDate = sdf.parse(start);
                thing_endDate = sdf.parse(end);
                thing_rate = bundle.getInt("thing_rate");
                thing_imgURI = bundle.getString("imageURI");
                Uri imgURI = Uri.parse(thing_imgURI);
                Log.v("bundle", thing_name);
                Log.v("bundle", start);
                Log.v("bundle", end);
                Log.v("bundle", String.valueOf(thing_rate));
                Log.v("bundle", thing_imgURI);
                try {
                    add_thing(thing_name, thing_startDate, thing_endDate, thing_rate, imgURI,number);
                } catch (Exception e) {
                    Log.v("添加错误", "1");
                }
            }catch (Exception e)
            {
            }
        }
        if (resultCode == change) {
            //如果是修改
//            Log.v( "get","已修改" );
//            Log.v( "接收到的是", String.valueOf( data.getExtras().getInt( "process" ) ) );
            int rate = data.getExtras().getInt("process");
            int position1 = data.getExtras().getInt("position");
            ContentValues values=new ContentValues();
            values.put("rate",rate);
            db.update("table_Thing",values,"number=?",new String[]{mthingList.get(position1).getNumber2Str()});
            Log.v("修改", "第" + position1 + "值是" + rate);
            getDatafromDB();
        }
        if (resultCode == nochange) {
            Log.v("get", "未修改");
        }
    }

    public void add_thing(String thing_name, Date thing_startDate, Date thing_endDate, int thing_rate, Uri thing_imgbyte,int number1) {
        //添加新的事件
        Thing new_thing = new Thing(thing_name, thing_startDate, thing_endDate, thing_rate, thing_imgbyte,number1);
        ContentValues values = new ContentValues();
        values.put("name", thing_name);
        values.put("start_date",sdf.format(thing_startDate));
        values.put("end_date", sdf.format(thing_endDate));
        values.put("rate", thing_rate);
        values.put("image", String.valueOf(thing_imgbyte));
        values.put("number",number1);
        db.insert("table_Thing", null, values);
        number++;
        getDatafromDB();
    }

    private void initView() {
        bt_add = findViewById(R.id.btn_add);//主页的添加按钮
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, thing_content.class);//跳转到添加页面
                startActivityForResult(intent1, add_new);
            }
        });
        adapter.setOnMyItemClickListener(new ThingAdapter.OnMyItemClickListener() {
            @Override
            public void myClick(View v, int position) {

                Intent intent2 = new Intent(MainActivity.this, adjust.class);
                intent2.putExtra("position", position);
                Log.v("点击的项目是", String.valueOf(position));
                startActivityForResult(intent2, 1);
            }
        });
    }
}
