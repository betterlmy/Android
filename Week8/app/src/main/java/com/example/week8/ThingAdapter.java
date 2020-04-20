package com.example.week8;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ThingAdapter extends RecyclerView.Adapter<ThingAdapter.MyViewHolder> {
    //创建recycler的适配器
    private Context mContext;
    private List<Thing> mThingList;
    private OnMyItemClickListener listener;

    //数据源
    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView thing_startDate;
        TextView thing_endDate;
        TextView thing_name;
        TextView sort;
        ProgressBar process;
        Button submit;
        ImageView img;

        public MyViewHolder(View view1) {
            super(view1);
            thing_startDate = view1.findViewById(R.id.thing_startDate);
            thing_endDate = view1.findViewById(R.id.thing_endDate);
            thing_name = view1.findViewById(R.id.thing_name);
            process = view1.findViewById(R.id.process);
            submit = view1.findViewById(R.id.btn_finish);
            sort=view1.findViewById(R.id.sort);
            img = view1.findViewById(R.id.thing_img);
//            submit.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int position = (int) v.getTag();
//                    String name = mThingList.get(position).getthing_name();
//                    mThingList.remove(position);//删除该按钮
//                    adapter.notifyDataSetChanged();
//                    Toast.makeText(MainActivity.this, "您删除掉了事件：" + name, Toast.LENGTH_SHORT).show();
//                }
//            });
        }

        public void setdata(Thing thing, int position) {
            this.thing_name.setText(thing.getthing_name());
            this.thing_startDate.setText(thing.getStartDate());
            this.thing_endDate.setText(thing.getEndDate());
            this.process.setProgress(thing.getthing_rate());
//            this.sort.setText(position);
            Log.v("number", String.valueOf(position));
            Uri imgData = thing.getthing_imgbyte();
            if (imgData != null) {
                //将字节数组转化为位图
                //将位图显示为图片
                this.img.setImageURI(imgData);
            } else {
                this.img.setBackgroundResource(R.mipmap.ic_launcher);
            }

            //将日期转为毫秒数
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            try {
                date = simpleDateFormat.parse(thing.getEndDate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (date.getTime() < System.currentTimeMillis()) {
                this.thing_name.append("(已过期)");
                this.thing_name.setTextColor(Color.RED);
            }

        }
    }

    public ThingAdapter(List<Thing> List, Context context) {
        mThingList = List;//传入数据源
        mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //传入thing界面的内容
        //1.拿到thing见面的view
        //2.创建ViewHolder类型的view
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.thing, null);
        final MyViewHolder holder=new MyViewHolder(view1);

        holder.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.db.delete("table_Thing","name=?",new String[]{mThingList.get(holder.getAdapterPosition()).getthing_name()});
//                MainActivity.db.close();
                mThingList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
            }
        });


        return holder;
    }
    public void setOnMyItemClickListener(OnMyItemClickListener listener){
        this.listener = listener;
    }

    public interface OnMyItemClickListener{
        void myClick(View v, int position);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {
        //这个方法用于绑定内部holder，用来设置数据
        final Thing thing = mThingList.get(position);
        holder.setdata(thing, position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.myClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {

        return mThingList.size();
    }
}