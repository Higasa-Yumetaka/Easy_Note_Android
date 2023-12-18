package com.example.Note_Todo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activitydemo.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Todo_AddTodoActivity extends AppCompatActivity {

    //private EditText mEdtTitle;
    private EditText mEdtContent;
    private TextView mTvCount;

    private TodoDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.todo_activity_add_todo);

        //mEdtTitle = findViewById(R.id.edt_title);
        mEdtContent = findViewById(R.id.edt_content);
        TextView mTvTime = findViewById(R.id.tv_time);
        mTvCount = findViewById(R.id.tv_count);

        // 设置时间
        SimpleDateFormat sdf = new SimpleDateFormat("M月d日 ahh:mm");
        String currentDateandTime = sdf.format(new Date());
        mTvTime.setText(currentDateandTime);

        // 统计字数
        mEdtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvCount.setText(String.valueOf(s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dbHelper = new TodoDBHelper(this, "TodoFolder.db", null, 1);

        findViewById(R.id.back_button).setOnClickListener(v -> {

            finish();
        });

        findViewById(R.id.save_button).setOnClickListener(v -> {
            //String title = mEdtTitle.getText().toString();
            String content = mEdtContent.getText().toString();
            long createTime = System.currentTimeMillis();

            /*if (title.equals("") && content.equals("")) {
                //Toast.makeText(AddNoteActivity.this, "请输入内容", Toast.LENGTH_SHORT).show();
                finish();
            }*/

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            //values.put("title", title);
            values.put("content", content);
            values.put("created_time", createTime);
            values.put("last_modified_time", System.currentTimeMillis());
            values.put("is_completed", 0); // 待办事件尚未完成
            long result = db.insert("Todo", null, values);
            if (result == -1){
                Toast.makeText(Todo_AddTodoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
            else {
                //Toast.makeText(AddNoteActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
            }
            finish();
        });
    }
}