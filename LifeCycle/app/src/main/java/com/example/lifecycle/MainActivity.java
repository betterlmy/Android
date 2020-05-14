package com.example.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("LifeCycle","onCreate1");
        Button btn=findViewById(R.id.jump);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LifeCycle.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LifeCycle","onStart1");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LifeCycle","onResume1");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LifeCycle","onPause1");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LifeCycle","onStop1");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LifeCycle","onDestroy1");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("LifeCycle","onRestart1");
    }
}
