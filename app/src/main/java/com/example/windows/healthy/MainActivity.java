package com.example.windows.healthy;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.windows.healthy.db.DatabaseOperation;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SQLiteDatabase db;//数据库对象
    private DatabaseOperation dop;//自定义数据库类
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dop = new DatabaseOperation(this, db);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart1111: ");
    }
}
