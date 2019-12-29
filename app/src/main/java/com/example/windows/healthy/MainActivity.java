package com.example.windows.healthy;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.windows.healthy.adapter.MainAdapter;
import com.example.windows.healthy.bean.SQLBean;
import com.example.windows.healthy.db.DatabaseOperation;
import com.example.windows.healthy.view.MyGridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SQLiteDatabase db;//数据库对象
    private DatabaseOperation dop;//自定义数据库类
    private MyGridView lv_notes;// 消息列表
    public MainAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dop = new DatabaseOperation(this, db);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 显示记事列表
        showNotesList();
        Log.i(TAG, "onStart1111: ");
    }

    private void showNotesList() {
        // 创建或打开数据库 获取数据
        dop.create_db();
        //获取数据库内容
        Cursor cursor = dop.query_db();
        if (cursor.getCount() > 0) {
            List<SQLBean> list = new ArrayList<SQLBean>();//日记信息集合里
            while (cursor.moveToNext()) {// 光标移动成功
                // 把数据取出
                SQLBean bean = new SQLBean();//创建数据库实体类
                //保存日记信息id到实体类
                bean.set_id("" + cursor.getInt(cursor.getColumnIndex("_id")));
                bean.setContext(cursor.getString(cursor
                        .getColumnIndex("context")));//保存日记内容到实体类
                //保存日记标题到实体类
                bean.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                //保存日记记录时间到实体类
                bean.setTime(cursor.getString(cursor.getColumnIndex("time")));
                bean.setDatatype(cursor.getString(cursor
                        .getColumnIndex("datatype")));//保存日记是否设置提醒时间到实体类
                bean.setDatatime(cursor.getString(cursor
                        .getColumnIndex("datatime")));//保存日记提醒时间到实体类
                bean.setLocktype(cursor.getString(cursor
                        .getColumnIndex("locktype")));//保存日记是否设置了日记锁到实体类
                //保存日记锁秘密到实体类
                bean.setLock(cursor.getString(cursor.getColumnIndex("lock")));
                list.add(bean);//把保存日记信息实体类保存到日记信息集合里
            }
            //倒序显示数据
            Collections.reverse(list);
            adapter = new MainAdapter(list, this);//装载日记信息到首页
            lv_notes.setAdapter(adapter);//日记列表设置日记信息适配器
        }
        dop.close_db();//关闭数据库
    }
}
