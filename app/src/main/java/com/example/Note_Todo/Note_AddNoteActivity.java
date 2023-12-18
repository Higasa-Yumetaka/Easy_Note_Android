package com.example.Note_Todo;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activitydemo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Note_AddNoteActivity extends AppCompatActivity {

    private static final boolean DEBUG = true;
    private EditText mEdtTitle;
    private EditText mEdtContent;
    private TextView mTvCount;
    long result = 0;
    private NoteDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.note_layout);
        Note_SetStatusBar.setStatusBar(this);

        mEdtTitle = findViewById(R.id.edt_title);
        mEdtContent = findViewById(R.id.edt_content);
        TextView mTvTime = findViewById(R.id.tv_time);
        mTvCount = findViewById(R.id.tv_count);
        dbHelper = new NoteDBHelper(this, "NoteFolder.db", null, 1);
        // 设置时间
        mTvTime.setText(Note_GetDateTimeString.getDateTimeString(System.currentTimeMillis(), true));
        // 统计字数
        mEdtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTvCount.setText(String.valueOf(Note_CountTextNum.countTextNum(s.toString())));
                saveNote(true);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mEdtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                saveNote(true);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        findViewById(R.id.save_button).setOnClickListener(v -> {
            saveNote(false);
            //__set_Result();
            finish();
        });


        findViewById(R.id.back_button).setOnClickListener(v -> {
            saveNote(false);
            //__set_Result();
            finish();
        });

        FloatingActionButton delete_button = findViewById(R.id.delete_button);
        delete_button.setVisibility(View.GONE);
    }

    //保存笔记
    private void saveNote(boolean isOnChange){
        String title = mEdtTitle.getText().toString();
        String content = mEdtContent.getText().toString();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        //还未插入数据，则插入数据
        if(result == 0){
            result = db.insert("Notes", null, getContentValues(false));
        } else if(result > 0){//已经插入数据，则更新数据
            if(title.equals("")&&content.equals("")) {//如果标题和内容都为空，则删除数据
                if(isOnChange)
                {
                    db.update("Notes", getContentValues(true), "id = ?", new String[]{String.valueOf(result)});
                }else {
                    db.delete("Notes", "id = ?", new String[]{String.valueOf(result)});
                    result = 0;
                    setResult(RESULT_CANCELED);
                }
            }else{//如果标题和内容不为空，则更新数据
                db.update("Notes", getContentValues(true), "id = ?", new String[]{String.valueOf(result)});
            }
        }
        if (result == -1){
            Toast.makeText(Note_AddNoteActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
        }
        if(!DEBUG&&!isOnChange){
            db.close();
        }
    }

    //为saveNote()方法提供ContentValues对象
    private ContentValues getContentValues(boolean isUpdate) {
        String title = mEdtTitle.getText().toString();
        String content = mEdtContent.getText().toString();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("content", content);
        if(isUpdate){
            values.put("last_modified_time", System.currentTimeMillis());
        }else {
            values.put("created_time", System.currentTimeMillis());
            values.put("last_modified_time", System.currentTimeMillis());
        }
        return values;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            finish();
        }

        @Override
        protected void onDestroy() {
            super.onDestroy();
           //__set_Result();
        }
}