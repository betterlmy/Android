package com.example.week8;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class adjust extends AppCompatActivity {
    private SeekBar seekBar;
    private TextView tv5;
    private Button queding;
    private Button cancle;

    private int position;
    Intent intent = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.adjust );
        Intent intent = getIntent();
        position= intent.getIntExtra("position",0);
        seekBar =this.findViewById( R.id.seekbar1 );
        tv5=this.findViewById( R.id.tv5 );
        queding=this.findViewById( R.id.queding);
        cancle=this.findViewById( R.id.cancle );
        initListener();
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
        }
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
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
