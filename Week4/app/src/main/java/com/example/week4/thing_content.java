package com.example.week4;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class thing_content extends AppCompatActivity {

    //绑定控件
    @BindView( R.id.text_thing )
    EditText text;
    @BindView( R.id.text_date )
    EditText date;
    @BindView(R.id.datepicker)
    DatePicker datePicker;
    @BindView(R.id.text_rate)
    EditText rate;
    @BindView( R.id.btn_submit )
    Button submit_btn;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.thing_content );
        ButterKnife.bind( this );
        date.setFocusable(false); //失去焦点，点击日期文本框不在弹出输入法
        initListener();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initListener(){
        submit_btn.setOnClickListener(complete);
        date.setOnClickListener( setDate );
        datePicker.setOnDateChangedListener( dateChanged );
    }

    View.OnClickListener complete=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //获取变量值
                String thing_name = String.valueOf( text.getText() );
                String thing_date = String.valueOf( date.getText() );
                int thing_rate = Integer.parseInt( String.valueOf( rate.getText() ) );
                if (thing_rate > 100 || thing_rate < 0) {
                    Toast.makeText( thing_content.this,"格式错误",Toast.LENGTH_SHORT).show();
                }else {
                    Bundle bundle = new Bundle();
                    bundle.putString( "thing_name", thing_name );
                    bundle.putString( "thing_date", thing_date );
                    bundle.putInt( "thing_rate", thing_rate );
                    Message msg = new Message();
                    msg.setData( bundle );
                    //传参
                    Intent intent = new Intent();
                    intent.putExtras( bundle );
                    setResult( 1, intent );
                    finish();
                }

        }
    };
    View.OnClickListener setDate=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            datePicker.setVisibility(View.VISIBLE);//日历设置不可见
            datePicker.bringToFront();//把日历显示到最前方，防止显示冲突
        }
    };
        public DatePicker.OnDateChangedListener dateChanged=new DatePicker.OnDateChangedListener() {
        //点击日历后把选定的日期转换成字符串写入到日期文本中去
        @Override
        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            date.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
            datePicker.setVisibility(View.INVISIBLE);//设置完后不可见
        }
    };









}
