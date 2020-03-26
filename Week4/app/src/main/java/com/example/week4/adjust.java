package com.example.week4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class adjust extends AppCompatActivity {
    private SeekBar seekBar;
    private TextView tv5;
    private Button queding;
    private Button cancle;
    private int process;
    private int position;
    Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.adjust );
        seekBar =this.findViewById( R.id.seekbar1 );
        tv5=this.findViewById( R.id.tv5 );
        queding=this.findViewById( R.id.queding);
        cancle=this.findViewById( R.id.cancle );
        initListener();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null){
            process= data.getExtras().getInt( "process" ) ;
            position=data.getExtras().getInt( "position" );
        }
    }
    private void initListener(){
        seekBar.setOnSeekBarChangeListener( seekBarChangeListener);
        queding.setOnClickListener(quedingListener);
        cancle.setOnClickListener( cancleListener );
    }
    private SeekBar.OnSeekBarChangeListener seekBarChangeListener= new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            tv5.setText( progress+"%" );
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            Toast.makeText( adjust.this,"开始滑动",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            Toast.makeText( adjust.this,"结束滑动",Toast.LENGTH_SHORT).show();
        }
    } ;
    private View.OnClickListener quedingListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int now=seekBar.getProgress();
            intent.putExtra( "process",now );
            intent.putExtra( "position",position );
            Log.v( "发送的数值是", String.valueOf( now ) );
            Log.v( "发送的位置是", String.valueOf( position ) );
            setResult( 2, intent );
            finish();
        }
    };
    private View.OnClickListener cancleListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setResult( 3, intent );
            finish();
        }
    };
}
