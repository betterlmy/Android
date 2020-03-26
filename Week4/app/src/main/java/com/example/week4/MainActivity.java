package com.example.week4;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.btn_add)  Button add;
    private Bundle bundle;
    final int add_new=1;
    final int change=2;
    final int nochange=3;
    private int process;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //初始化操作
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        ButterKnife.bind(this);
        add.setOnClickListener( click1 );
        initThings();
    }

    //创建跳转监听者事件
        public View.OnClickListener click1=new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,thing_content.class);
            startActivityForResult(intent,add_new);
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == add_new) {
            //设置结果显示框的显示数值
            bundle = data.getExtras();
            add_thing(bundle.getString( "thing_name" ),bundle.getString( "thing_date" ),bundle.getInt( "thing_rate" ));
        }
        if (resultCode == change) {
            //如果是修改
//            Log.v( "get","已修改" );
//            Log.v( "接收到的是", String.valueOf( data.getExtras().getInt( "process" ) ) );
            process=data.getExtras().getInt( "process" );
            int position =data.getExtras().getInt( "position" );
            mthingList.get( position ).thing_rate=process;
            Log.v( "修改", "第"+position+"值是"+process );
            adapter.notifyDataSetChanged();
        }
        if (resultCode == nochange) {
            Log.v( "get","未修改" );
        }
    }


    List<Thing> mthingList = new ArrayList<>();
    public ThingAdapter adapter = new ThingAdapter(mthingList,MainActivity.this);//设置新的时间适配器，用于处理事件列表
    public class ThingAdapter extends RecyclerView.Adapter<ThingAdapter.MyViewHolder> {
        //创建recycler的适配器
        private Context mContext;
        private List<Thing> mThingList;
        //数据源
        class MyViewHolder extends RecyclerView.ViewHolder {
            TextView thing_date;
            TextView thing_name;
            ProgressBar process;
            Button submit;

            public MyViewHolder(View view1) {
                super(view1);
                thing_date= view1.findViewById( R.id.thing_date ) ;
                thing_name= view1.findViewById( R.id.thing_name ) ;
                process=view1.findViewById( R.id.process ) ;
                submit=view1.findViewById( R.id.btn_finish );
                submit.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position=(int)v.getTag();
                        String name=mthingList.get( position ).getName();
                        mthingList.remove(position);//删除该按钮
                        adapter.notifyDataSetChanged();
                        Toast.makeText( MainActivity.this,"您删除掉了事件："+name,Toast.LENGTH_SHORT).show();
                    }
                } );
            }

            public void setdata(Thing thing,int position) {
                this.thing_name.setText( thing.getName() );
                this.thing_date.setText(thing.getDate());
               this.process.setProgress(thing.getRate());

            }
        }

        public ThingAdapter(List<Thing> mthingList,Context context) {
            mThingList = mthingList;//传入数据源
            mContext=context;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //传入thing界面的内容
            //1.拿到thing见面的view
            //2.创建ViewHolder类型的view
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.thing,null);
            return new MyViewHolder(view1);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            //这个方法用于绑定内部holder，用来设置数据
            Thing thing = mThingList.get(position);
           holder.setdata( thing,position );
           holder.itemView.setOnClickListener( new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Intent intent1=new Intent(MainActivity.this,adjust.class);
                   intent1.putExtra( "process",thing.getRate());
                   intent1.putExtra( "position",position);
                   startActivityForResult(intent1,1);
               }
           } );
        }

        @Override
        public int getItemCount() {
            return mThingList.size();
        }
    }

    public class Thing {
        //创建Thing类内含有thingname,thingdate以及rate属性
        private String thing_name;
        private String thing_date;
        private int thing_rate;
        public Thing(String thing_name,String thing_date,int rate){ //新建Thing类的方法
            this.thing_name=thing_name;
            this.thing_date=thing_date;
            this.thing_rate=rate;
        }
        public String getName(){
            return thing_name;
        }
        public String getDate(){
            return thing_date;
        }
        public int getRate(){
            return thing_rate;
        }
    }

    public void add_thing(String thing_name,String thing_date,int thing_rate){
        //添加新的事件
        Thing new_thing=new Thing(thing_name,thing_date,thing_rate);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date1 = null;
        Date date2 = null;
        if(mthingList.size()!=0)
            for(int i=0;i<mthingList.size();) {
                try {
                    date1 = format.parse( thing_date );
                    date2 = format.parse( mthingList.get( i ).thing_date );
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (date1.before( date2 )) {
                    mthingList.add( i, new_thing );
                    break;
                }
                i++;
                if(i==mthingList.size()) {
                    mthingList.add(new_thing);
                    Log.v( "a","添加成功" );
                    break;
                }
            }else mthingList.add(new_thing);//把新事件添加到list中去
        adapter.notifyDataSetChanged(); //更新
    }

    private void initThings(){
        //初始化事件
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);//创建新的布局管理
        recyclerView.setLayoutManager(layoutManager);//在recycler中应用该布局管理器
        recyclerView.setAdapter(adapter);//在recycler中应用该适配器
    }
}