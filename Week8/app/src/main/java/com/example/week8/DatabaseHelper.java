package com.example.week8;
//SQLiteOpenHelper的派生类
//SQLiteOpenHelper是SQLiteDatabse的一个帮助类
// 用来管理数据的创建和版本更新。
// 该类是一个抽象类，要使用SQLiteOpenHelper类时必须创建该类的派生类。
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String create_table="create table table_Thing(" +
            //创建表create_table 包含int型 id和progress text型的data，data_ex,content，blob型的image
            "id integer primary key autoincrement," +//id自动增加 主键
            "start_date text," +
            "end_date text," +
            "name text," +
            "image blob," +
            "rate integer,"+
            "number integer)";

    public DatabaseHelper(Context context,String name) {
        super(context, name, null, 1);  //默认factory为null  版本为1
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(create_table);  //数据库执行sql语句，创建表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
