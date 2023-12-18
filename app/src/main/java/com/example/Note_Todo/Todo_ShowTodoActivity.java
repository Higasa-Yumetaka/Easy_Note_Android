package com.example.Note_Todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activitydemo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;


public class Todo_ShowTodoActivity extends AppCompatActivity {

    private final List<String> noteList = new ArrayList<>();
    private TodoDBHelper dbHelper;
    private ListView todo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.todo_show_todo_layout);


        dbHelper = new TodoDBHelper(this, "TodoFolder.db", null, 1);

        todo = findViewById(R.id.todo_list_view);
        todo.setOnItemClickListener((parent, view, position, id) -> {
            String note = noteList.get(position);
            //Log.i("ShowNotesActivity", "note: " + note);
            Intent intent = new Intent(Todo_ShowTodoActivity.this, TodoActivity.class);
            intent.putExtra("note", note);
            startActivity(intent);
        });
        /*todo = findViewById(R.id.todo_list_view);
        TodoListAdapter adapter = new TodoListAdapter(this, R.layout.todo_unfinished_list_item_layout, noteList);
        todo.setAdapter(adapter);

        todo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 处理子项的点击事件
                // ...
                String note = noteList.get(position);
                //Log.i("ShowNotesActivity", "note: " + note);
                Intent intent = new Intent(ShowTodoActivity.this, TodoActivity.class);
                intent.putExtra("note", note);
                startActivity(intent);
            }
        });*/

        FloatingActionButton flbAddNote = findViewById(R.id.add_todo_button);
        flbAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(Todo_ShowTodoActivity.this, Todo_AddTodoActivity.class);
            startActivity(intent);
        });

        // 搜索
        EditText etSearch = findViewById(R.id.search_edit_text);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 不需要实现
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文字发生变化时调用
                String keyword = s.toString().trim();
                searchNotes(keyword);
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 不需要实现
            }
        });

        TextView btnNotes = findViewById(R.id.note_text_view);
        btnNotes.setOnClickListener(v -> {
            Intent intent = new Intent(Todo_ShowTodoActivity.this, Note_ShowNotesActivity.class);
            startActivity(intent);
        });

        //点击全部待办事项按钮显示全部待办事项
        Button btnAllTodo = findViewById(R.id.all_todo_button);
        btnAllTodo.setOnClickListener(v -> {
            loadData();
        });

        //点击完成待办事项按钮显示已完成待办事项
        Button btnCompletedTodo = findViewById(R.id.finished_todo_button);
        btnCompletedTodo.setOnClickListener(v -> {

                noteList.clear();

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Todo", null, "is_completed = ?", new String[]{"1"}, null, null,
                        "last_modified_time" + " desc");
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String content = cursor.getString(cursor.getColumnIndex("content"));
                        long createdTime = cursor.getLong(cursor.getColumnIndex("created_time"));
                        long lastModifiedTime = cursor.getLong(cursor.getColumnIndex("last_modified_time"));
                        int isCompleted = cursor.getInt(cursor.getColumnIndex("is_completed"));
                        String noteInfo = id + "|" + content + "|" + createdTime + "|" + lastModifiedTime + "|" + isCompleted;
                        noteList.add(noteInfo);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();

                //按照最后修改时间从最新向最早排序
                noteList.sort((n1, n2) -> {
                    String[] noteInfo1 = n1.split("\\|");
                    String[] noteInfo2 = n2.split("\\|");
                    long lastModifiedTime1 = Long.parseLong(noteInfo1[3]);
                    long lastModifiedTime2 = Long.parseLong(noteInfo2[3]);
                    return (int) (lastModifiedTime2 - lastModifiedTime1);
                });

                TodoListAdapter adapter = new TodoListAdapter(this, R.layout.todo_unfinished_list_item_layout, noteList);
                todo.setAdapter(adapter);


        });

        //点击未完成待办事项按钮显示未完成待办事项
        Button btnUncompletedTodo = findViewById(R.id.unfinished_todo_button);
        btnUncompletedTodo.setOnClickListener(v -> {

                noteList.clear();

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor = db.query("Todo", null, "is_completed = ?", new String[]{"0"}, null, null,
                        "last_modified_time" + " desc");
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        String content = cursor.getString(cursor.getColumnIndex("content"));
                        long createdTime = cursor.getLong(cursor.getColumnIndex("created_time"));
                        long lastModifiedTime = cursor.getLong(cursor.getColumnIndex("last_modified_time"));
                        int isCompleted = cursor.getInt(cursor.getColumnIndex("is_completed"));
                        String noteInfo = id + "|" + content + "|" + createdTime + "|" + lastModifiedTime + "|" + isCompleted;
                        noteList.add(noteInfo);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                db.close();

                //按照最后修改时间从最新向最早排序
                noteList.sort((n1, n2) -> {
                    String[] noteInfo1 = n1.split("\\|");
                    String[] noteInfo2 = n2.split("\\|");
                    long lastModifiedTime1 = Long.parseLong(noteInfo1[3]);
                    long lastModifiedTime2 = Long.parseLong(noteInfo2[3]);
                    return (int) (lastModifiedTime2 - lastModifiedTime1);
                });

                TodoListAdapter adapter = new TodoListAdapter(this, R.layout.todo_unfinished_list_item_layout, noteList);
                todo.setAdapter(adapter);

        });
        /*SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Todo", null, null, null, null, null,
                "last_modified_time" + " desc");
        int id = cursor.getInt(cursor.getColumnIndex("id"));
        String content = cursor.getString(cursor.getColumnIndex("content"));
        long createdTime = cursor.getLong(cursor.getColumnIndex("created_time"));
        long lastModifiedTime = cursor.getLong(cursor.getColumnIndex("last_modified_time"));
        int isCompleted = cursor.getInt(cursor.getColumnIndex("is_completed"));
        String noteInfo =
                id + "|"
                        + content + "|"
                        + createdTime + "|"
                        + lastModifiedTime + "|"
                        + isCompleted;
        noteList.add(noteInfo);
        if(noteInfo[4].trim().equals("0")){
            add_todo_is_button = findViewById(R.id.todo_unfinished_button);
            add_todo_is_button.setText("未完成");
        }
        else{
            add_todo_is_button = findViewById(R.id.todo_unfinished_button);
            add_todo_is_button.setText("已完成");
        }*/
        //初始化时加载全部
        loadData();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // 返回桌面
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出程序时关闭数据库连接
        dbHelper.close();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //利用 onResume() 方法实现返回 ShowNotesActivity 时自动刷新列表
        loadData();
    }

    //加载数据函数，为搜索与初始化共用
    @SuppressLint("Range")
    private void loadData() {
        noteList.clear();

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("Todo", null, null, null, null, null,
                "last_modified_time" + " desc");
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                long createdTime = cursor.getLong(cursor.getColumnIndex("created_time"));
                long lastModifiedTime = cursor.getLong(cursor.getColumnIndex("last_modified_time"));
                int isCompleted = cursor.getInt(cursor.getColumnIndex("is_completed"));
                String noteInfo =
                        id + "|"
                                + content + "|"
                                + createdTime + "|"
                                + lastModifiedTime + "|"
                                 + isCompleted;
                noteList.add(noteInfo);
                Log.e("noteInfo", noteInfo);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        //按照最后修改时间从最新向最早排序
        noteList.sort((n1, n2) -> {
            String[] noteInfo1 = n1.split("\\|");
            String[] noteInfo2 = n2.split("\\|");
            long lastModifiedTime1 = Long.parseLong(noteInfo1[4].substring(noteInfo1[4].indexOf(":") + 1));
            long lastModifiedTime2 = Long.parseLong(noteInfo2[4].substring(noteInfo2[4].indexOf(":") + 1));
            return (int) (lastModifiedTime2 - lastModifiedTime1);
        });


        TodoListAdapter adapter = new TodoListAdapter(this, R.layout.todo_unfinished_list_item_layout, noteList);
        todo.setAdapter(adapter);
    }

    @SuppressLint("Range")
    private void searchNotes(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            // 关键词为空，显示全部便签
            loadData();
        } else {
            noteList.clear();

            SQLiteDatabase db = dbHelper.getWritableDatabase();
            // 根据关键词搜索便签，并按照最后修改时间从最新向最早排序
            Cursor cursor = db.query("Todo", null,
                    "content like ?",
                    new String[]{"%" + keyword + "%"},
                    null, null, "last_modified_time DESC");
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    String content = cursor.getString(cursor.getColumnIndex("content"));
                    long createdTime = cursor.getLong(cursor.getColumnIndex("created_time"));
                    long lastModifiedTime = cursor.getLong(cursor.getColumnIndex("last_modified_time"));
                    int isCompleted = cursor.getInt(cursor.getColumnIndex("is_completed"));
                    String noteInfo =
                            id + "|"
                                    + content + "|"
                                    + createdTime + "|"
                                    + lastModifiedTime + "|"
                                    + isCompleted;
                    noteList.add(noteInfo);
                    Log.d("noteInfo", noteInfo);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();

            // 使用自定义的ListView适配器
            TodoListAdapter adapter = new TodoListAdapter(this, R.layout.todo_unfinished_list_item_layout, noteList);
            todo.setAdapter(adapter);
        }
    }

    //自定义的ListView适配器
    /*public class TodoListAdapter extends ArrayAdapter<String> {
        private int resourceId;

        public TodoListAdapter(Context context, int textViewResourceId, List<String> noteList) {
            super(context, textViewResourceId, noteList);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String note = getItem(position);
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvButton = view.findViewById(R.id.todo_unfinished_button);
                viewHolder.tvContent = view.findViewById(R.id.todo_content_text_view);
                viewHolder.tvLMTime = view.findViewById(R.id.todo_time_text_view);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }


            String[] noteInfo = note.split("\\|");
            *//*if (noteInfo[1].trim().equals("") && !noteInfo[2].trim().equals("")) {
                viewHolder.tvTitle.setText(noteInfo[2].trim());
                viewHolder.tvContent.setText("无附加文案");
            } else if (!noteInfo[1].trim().equals("") && noteInfo[2].trim().equals("")) {
                viewHolder.tvTitle.setText(noteInfo[1].trim());
                viewHolder.tvContent.setText("无附加文案");
            } else {
                viewHolder.tvTitle.setText(noteInfo[1].trim());
                viewHolder.tvContent.setText(noteInfo[2].trim());
            }*//*
            viewHolder.tvContent.setText(noteInfo[1].trim());
            viewHolder.tvLMTime.setText(GetDateTimeString.getDateTimeString(Long.parseLong(noteInfo[3])));
            //设置每个tvContent最多显示4行
            viewHolder.tvContent.setMaxLines(4);
            viewHolder.tvContent.setEllipsize(TextUtils.TruncateAt.END);
            //设置每个tvTitle最多显示1行
            //viewHolder.tvTitle.setMaxLines(1);
            //viewHolder.tvTitle.setEllipsize(TextUtils.TruncateAt.END);
            return view;
        }

        private class ViewHolder {
            //TextView tvTitle;
            TextView tvButton;
            TextView tvContent;
            TextView tvLMTime;
        }
    }*/
    public class TodoListAdapter extends ArrayAdapter<String> {
        private int resourceId;



        public TodoListAdapter(Context context, int textViewResourceId, List<String> noteList) {
            super(context, textViewResourceId, noteList);
            resourceId = textViewResourceId;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String note = getItem(position);
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                viewHolder = new ViewHolder();
                //viewHolder.tvButton = view.findViewById(R.id.todo_unfinished_button);
                viewHolder.tvContent = view.findViewById(R.id.todo_content_text_view);
                viewHolder.tvLMTime = view.findViewById(R.id.todo_time_text_view);
                //viewHolder.btnButton = view.findViewById(R.id.todo_unfinished_button);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            String[] noteInfo = note.split("\\|");
            viewHolder.tvContent.setText(noteInfo[1].trim());
            viewHolder.tvLMTime.setText(Note_GetDateTimeString.getDateTimeString(Long.parseLong(noteInfo[3]),false));
            viewHolder.tvContent.setMaxLines(4);
            viewHolder.tvContent.setEllipsize(TextUtils.TruncateAt.END);

            // 处理 Button 控件的点击事件
            /*viewHolder.btnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 处理 Button 控件的点击事件
                    // ...
                }
            });*/
            /*viewHolder.btnButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   *//* int position = (Integer) v.getTag(); // 获取子项的位置
                    String note = getItem(position); // 获取子项的数据
                    String[] noteInfo = note.split("\\|");
                    noteInfo[5] = "1"; // 修改 note 中的 noteInfo[5] 数据为 1
                    note = TextUtils.join("|", noteInfo); // 将修改后的数据重新拼接成字符串
                    remove(note); // 从 ArrayAdapter 中移除原有的子项数据
                    insert(note, position); // 在 ArrayAdapter 中插入修改后的子项数据
                    notifyDataSetChanged(); // 更新 ListView 中的数据*//*
                    *//*if(noteInfo[4].trim().equals("0")){
                        String note = getItem(position); // 获取子项的数据
                        String[] noteInfo = note.split("\\|");
                        noteInfo[4] = "1";
                        note = TextUtils.join("|", noteInfo);
                        remove(note);
                        insert(note, position);
                        notifyDataSetChanged();
                    }
                    else{
                        String note = getItem(position); // 获取子项的数据
                        String[] noteInfo = note.split("\\|");
                        noteInfo[4] = "0";
                        note = TextUtils.join("|", noteInfo);
                        remove(note);
                        insert(note, position);
                        notifyDataSetChanged();
                    }*//*
                    ContentValues values = new ContentValues();
                    if(noteInfo[4].trim().equals("0")){
                        values.put("is_completed", 1);
                    }
                    if(noteInfo[4].trim().equals("1")){
                        values.put("is_completed", 0);
                    }
                }
            });*/
            /*if(noteInfo[4].trim().equals("0")){
                add_todo_is_button = findViewById(R.id.todo_unfinished_button);
                add_todo_is_button.setText("未完成");
            }
            else{
                add_todo_is_button = findViewById(R.id.todo_unfinished_button);
                add_todo_is_button.setText("已完成");
            }*/

            return view;
        }

        private class ViewHolder {
            //TextView tvButton;
            TextView tvContent;
            TextView tvLMTime;
            Button btnButton;
        }
    }
}