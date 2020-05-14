package com.example.lifecycle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class LifeCycle extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity);
        Log.d("LifeCycle","onCreate2");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("LifeCycle","onStart2");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LifeCycle","onResume2");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LifeCycle","onPause2");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("LifeCycle","onStop2");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LifeCycle","onDestroy2");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("LifeCycle","onRestart2");
    }
}
