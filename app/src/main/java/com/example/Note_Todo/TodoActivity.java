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

public class TodoActivity extends AppCompatActivity {

    private EditText mEdtTitle;
    private EditText mEdtContent;
    private TextView mTvTime;
    private TextView mTvCount;
    private String mNote;
    private Button add_todo_is_button;
    private TodoDBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.todo_layout);

        //mEdtTitle = findViewById(R.id.edt_title);
        mEdtContent = findViewById(R.id.edt_content);
        mTvTime = findViewById(R.id.tv_time);
        mTvCount = findViewById(R.id.tv_count);
        mNote = getIntent().getStringExtra("note");
        String[] noteInfo = mNote.split("\\|");


        //mEdtTitle.setText(noteInfo[1].trim());
        mEdtContent.setText(noteInfo[1].trim());
        mTvCount.setText(String.valueOf(noteInfo[1].trim().length()));
        // 设置时间
        /*SimpleDateFormat sdf = new SimpleDateFormat("M月d日 ahh:mm");
        String currentDateandTime = sdf.format(new Date());*/
        mTvTime.setText(Note_GetDateTimeString.getDateTimeString(Long.parseLong(noteInfo[3].trim()), true));

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
            //String title = mEdtTitle.getText().toString();
            String content = mEdtContent.getText().toString();

            /*if (title.equals("") && content.equals("")) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Todo", "id=?", new String[]{noteInfo[0].trim()});
            }
*/
            if ( content.equals("")) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Todo", "id=?", new String[]{noteInfo[0].trim()});
            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
           // values.put("title", title);
            values.put("content", content);
            values.put("last_modified_time", System.currentTimeMillis());
            int result = db.update("Todo", values, "id=?", new String[]{noteInfo[0].trim()});
            if (result == -1){
                Toast.makeText(TodoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
            else {
                finish();
            }
            finish();
        });

        findViewById(R.id.delete_button).setOnClickListener(v -> {
            LayoutInflater inflater = LayoutInflater.from(TodoActivity.this);
            View view = inflater.inflate(R.layout.note_delete_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(TodoActivity.this);

            builder.setView(view);
            AlertDialog dialog = builder.create();
            TextView title = view.findViewById(R.id.delete_title);
            title.setText("确认删除");
            TextView message = view.findViewById(R.id.delete_message);
            message.setText("确定要删除这条待办事项吗？");

            Button cancelButton = view.findViewById(R.id.delete_cancel);
            cancelButton.setOnClickListener(v1 -> {
                //取消dialog
                dialog.dismiss();
            });

            Button confirmButton = view.findViewById(R.id.delete_confirm);
            confirmButton.setOnClickListener(v12 -> {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                int result = db.delete("todo", "id=?", new String[]{noteInfo[0].trim()});
                if (result == -1){
                    Toast.makeText(TodoActivity.this, "删除失败", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(TodoActivity.this, "待办已删除", Toast.LENGTH_SHORT).show();
                    finish();
                }
                dialog.dismiss();
            });


            dialog.show();
        });

        findViewById(R.id.save_button).setOnClickListener(v -> {
//            String title = mEdtTitle.getText().toString();
            String content = mEdtContent.getText().toString();
            //long createTime = System.currentTimeMillis();

            /*if (title.equals("") && content.equals("")) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Todo", "id=?", new String[]{noteInfo[0].trim()});
            }
*/
            if (content.equals("")) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Todo", "id=?", new String[]{noteInfo[0].trim()});
            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            //values.put("title", title);
            values.put("content", content);
            //values.put("created_time", createTime);
            values.put("last_modified_time", System.currentTimeMillis());
            int result = db.update("Todo", values, "id=?", new String[]{noteInfo[0].trim()});
            if (result == -1){
                Toast.makeText(TodoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
            else {
                finish();
            }
        });

        findViewById(R.id.add_todo_is_button).setOnClickListener(v -> {
//            String title = mEdtTitle.getText().toString();
            String content = mEdtContent.getText().toString();
            //long createTime = System.currentTimeMillis();

            /*if (title.equals("") && content.equals("")) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Todo", "id=?", new String[]{noteInfo[0].trim()});
            }
*/
            if (content.equals("")) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("Todo", "id=?", new String[]{noteInfo[0].trim()});
            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            //values.put("title", title);
            //values.put("is_completed", 1);
            if(noteInfo[4].trim().equals("0")){
                values.put("is_completed", 1);
            }
            else{
                values.put("is_completed", 0);
            }
            //values.put("created_time", createTime);
            values.put("last_modified_time", System.currentTimeMillis());
            int result = db.update("Todo", values, "id=?", new String[]{noteInfo[0].trim()});
            if (result == -1){
                Toast.makeText(TodoActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
            }
            else {
                finish();
            }
        });
        if(noteInfo[4].trim().equals("0")){
            add_todo_is_button = findViewById(R.id.add_todo_is_button);
            add_todo_is_button.setText("未完成");
        }
        else{
            add_todo_is_button = findViewById(R.id.add_todo_is_button);
            add_todo_is_button.setText("已完成");
        }
    }
}