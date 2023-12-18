package com.example.Note_Todo;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activitydemo.R;

public class NoteActivity extends AppCompatActivity {

    private static final boolean DEBUG = true;
    private EditText mEdtTitle;
    private EditText mEdtContent;
    private TextView mTvCount;
    private String[] noteInfo;
    private NoteDBHelper dbHelper;
    private boolean isChanged = false;

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
        //获取传递过来的笔记信息
        String mNote = getIntent().getStringExtra("note");
        //将笔记信息分割成数组
        noteInfo = mNote.split("\\|");

        //将笔记信息显示在界面上
        mEdtTitle.setText(noteInfo[1].trim());
        mEdtContent.setText(noteInfo[2].trim());
        mTvCount.setText(String.valueOf(Note_CountTextNum.countTextNum(noteInfo[2].trim())));
        mTvTime.setText(Note_GetDateTimeString.getDateTimeString(Long.parseLong(noteInfo[4].trim()),true));

        //用于统计字数和判断内容是否经过修改
        mEdtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 用于统计字数
                // 用于判断内容是否经过修改
                mTvCount.setText(String.valueOf(Note_CountTextNum.countTextNum(s.toString())));
                isChanged = true;
                saveNote(true);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //用于确认标题是否经过修改
        mEdtTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isChanged = true;
                saveNote(true);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        dbHelper = new NoteDBHelper(this, "NoteFolder.db", null, 1);

        //按下返回键，保存笔记
        findViewById(R.id.back_button).setOnClickListener(View ->{
            saveNote(false);
            //__setResult();
            closeDB();
            finish();
        });

        //按下删除键，弹出确认删除对话框
        findViewById(R.id.delete_button).setOnClickListener(v -> {
            //弹出确认删除对话框
            LayoutInflater inflater = LayoutInflater.from(NoteActivity.this);
            //引入自定义的对话框布局
            View view = inflater.inflate(R.layout.note_delete_dialog, null);
            AlertDialog.Builder builder = new AlertDialog.Builder(NoteActivity.this);
            //设置对话框布局
            builder.setView(view);
            AlertDialog dialog = builder.create();

            Button cancelButton = view.findViewById(R.id.delete_cancel);
            cancelButton.setOnClickListener(v1 -> {
                //取消dialog
                dialog.dismiss();
            });
            //确认删除
            Button confirmButton = view.findViewById(R.id.delete_confirm);
            confirmButton.setOnClickListener(v12 -> {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int result = db.delete("Notes", "id=?", new String[]{noteInfo[0].trim()});
                if (result == -1){
                    Toast.makeText(NoteActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(NoteActivity.this, "笔记已删除", Toast.LENGTH_SHORT).show();
                    finish();

                }
                //setResult(RESULT_OK);
                db.close();
                dialog.dismiss();
            });
            dialog.show();
        });

        //按下保存键，保存笔记
        findViewById(R.id.save_button).setOnClickListener(v -> {
            saveNote(false);
            //__setResult();
            closeDB();
            finish();
        });
    }

    //按下返回键，保存笔记
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveNote(false);
        //__setResult();
        closeDB();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        closeDB();
        //__setResult();
        saveNote(false);
        dbHelper.close();
    }


    //保存笔记
    protected void saveNote(boolean isOnChange)//isOnChange用于判断是否为内容改变时的保存
    {
        String title = mEdtTitle.getText().toString();
        String content = mEdtContent.getText().toString();
        if (!isChanged) {
            //内容未改变，不保存
            finish();
        }else if(title.equals("") && content.equals("") && !isOnChange) {
            //内容与标题为空，删除该笔记
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("Notes", "id=?", new String[]{noteInfo[0].trim()});
            db.close();
        }else{
            //内容与标题有修改且不为空，更新该笔记
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("title", title);
            values.put("content", content);
            values.put("last_modified_time", System.currentTimeMillis());
            int result = db.update("Notes", values, "id=?", new String[]{noteInfo[0].trim()});
            if (result == -1){
                Toast.makeText(NoteActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected void closeDB(){
        if(!DEBUG){
            dbHelper.close();
        }
    }
}