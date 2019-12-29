package com.example.windows.healthy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

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
    private TextView tv_note_id, tv_locktype, tv_lock;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dop = new DatabaseOperation(this, db);
        lv_notes = (MyGridView) findViewById(R.id.lv_notes);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // 显示记事列表
        showNotesList();
        // 为记事列表添加监听器
        lv_notes.setOnItemClickListener(new ItemClickEvent());
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

    // 记事列表单击监听器
    class ItemClickEvent implements OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            tv_note_id = (TextView) view.findViewById(R.id.tv_note_id);
            tv_locktype = (TextView) view.findViewById(R.id.tv_locktype);
            tv_lock = (TextView) view.findViewById(R.id.tv_lock);
            String locktype = tv_locktype.getText().toString();
            String lock = tv_lock.getText().toString();
            int item_id = Integer.parseInt(tv_note_id.getText().toString());
            if ("0".equals(locktype)) {
                //Intent intent = new Intent(MainActivity.this, AddActivity.class);
                //ntent.putExtra("editModel", "update");
                //intent.putExtra("noteId", item_id);
                //startActivity(intent);
            } else {
                //inputTitleDialog(lock, 0, item_id);
            }
        }
    }
    // 加密日记打开弹出的输入密码框
    public void inputTitleDialog(final String lock, final int idtype,
                                 final int item_id) {// 密码输入框
        final EditText inputServer = new EditText(this);
        inputServer.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        inputServer.setFocusable(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入密码").setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                String inputName = inputServer.getText().toString();
                if ("".equals(inputName)) {
                    Toast.makeText(MainActivity.this, "密码不能为空请重新输入！",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (inputName.equals(lock)) {
                        if (0 == idtype) {
                            //Intent intent = new Intent(MainActivity.this,
                                    //AddActivity.class);
                            //intent.putExtra("editModel", "update");
                            //intent.putExtra("noteId", item_id);
                            //startActivity(intent);
                        } else if (1 == idtype) {
                            dop.create_db();
                            dop.delete_db(item_id);
                            dop.close_db();
                            // 刷新列表显示
                            lv_notes.invalidate();
                            showNotesList();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "密码不正确！",
                                Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        builder.show();
    }
}

