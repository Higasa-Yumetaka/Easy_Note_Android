package com.example.Note_Todo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.activitydemo.R;

import java.util.ArrayList;
import java.util.List;

public class Note_ShowNotesActivity extends AppCompatActivity {

    private static final boolean DEBUG = true;
    private final List<String> noteList = new ArrayList<>();
    private NoteDBHelper dbHelper;
    private ListView lvNotes;
    private String keyWord;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.note_show_notes_layout);
        Note_SetStatusBar.setStatusBar(this);

        dbHelper = new NoteDBHelper(this, "NoteFolder.db", null, 1);

        lvNotes = findViewById(R.id.notes_list_view);
        //加载全部笔记并关闭数据库
        loadNotes(false,"");
        lvNotes.setOnItemClickListener((parent, view, position, id) -> {
            dbHelper.close();
            String note = noteList.get(position);
            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra("note", note);
            startActivity(intent);
        });
        //添加笔记按钮
        findViewById(R.id.add_note_button).setOnClickListener(v -> {
            dbHelper.close();
            Intent intent = new Intent(Note_ShowNotesActivity.this, Note_AddNoteActivity.class);
            startActivity(intent);
        });

        //待办页面接口a
//        findViewById(R.id.tv_todo).setOnClickListener(v -> {
//            dbHelper.close();
//            Intent intent = new Intent(Note_ShowNotesActivity.this, Todo_ShowTodoActivity.class);
//            startActivity(intent);
//        });

        // 搜索
        EditText etSearch = findViewById(R.id.search_edit_text);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //不需要实现
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //文字发生变化时调用搜索,不关闭数据库
                keyWord = s.toString().trim();
                loadNotes(true,keyWord);
            }
            @Override
            public void afterTextChanged(Editable s) {
                //不需要实现
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNotes(false,"");
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();

    }

    @SuppressLint("Range")
    protected void loadNotes(boolean isSearch, String keyword) {
        String selection = "title like ? or content like ?";
        String[] selectionArgs = new String[]{"%" + keyword + "%", "%" + keyword + "%"};
        noteList.clear();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor;
        if(isSearch) {
            cursor = db.query("Notes", null, selection, selectionArgs, null, null,
                    "last_modified_time DESC");

        }else{
            cursor = db.query("Notes", null, null, null, null, null,
                    "last_modified_time DESC");
        }
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String content = cursor.getString(cursor.getColumnIndex("content"));
                long createdTime = cursor.getLong(cursor.getColumnIndex("created_time"));
                long lastModifiedTime = cursor.getLong(cursor.getColumnIndex("last_modified_time"));
                if(title.equals("")&&content.equals("")){
                    db.delete("Notes", "id=?", new String[]{String.valueOf(id)});
                }else{
                    String noteInfo =
                            id + "|"
                                    + title + "|"
                                    + content + "|"
                                    + createdTime + "|"
                                    + lastModifiedTime;
                    noteList.add(noteInfo);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        if(!isSearch&&!DEBUG){
            dbHelper.close();
        }
        //使用memo_list_item_layout.xml布局文件
        NoteListAdapter adapter = new NoteListAdapter(this, R.layout.note_list_item, noteList);
        lvNotes.setAdapter(adapter);
    }

    //自定义的ListView适配器
    public static class NoteListAdapter extends ArrayAdapter<String> {
        private final int resourceId;
        public NoteListAdapter(Context context, int ResourceId, List<String> noteList) {
            super(context, ResourceId, noteList);
            resourceId = ResourceId;
        }
        //重写getView方法，在每个子项被滚动到屏幕内时被调用
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            String note = getItem(position);
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.tvTitle = view.findViewById(R.id.memo_title_text_view);
                viewHolder.tvContent = view.findViewById(R.id.memo_content_text_view);
                viewHolder.tvLMTime = view.findViewById(R.id.memo_time_text_view);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }

            String[] noteInfo = note.split("\\|");
            //标题为空，用内容代替，反之
            if (noteInfo[1].trim().equals("") && !noteInfo[2].trim().equals("")) {
                viewHolder.tvTitle.setText(noteInfo[2].trim());
                viewHolder.tvContent.setText("无附加文案");
            } else if (!noteInfo[1].trim().equals("") && noteInfo[2].trim().equals("")) {
                viewHolder.tvTitle.setText(noteInfo[1].trim());
                viewHolder.tvContent.setText("无附加文案");
            } else {
                viewHolder.tvTitle.setText(noteInfo[1].trim());
                viewHolder.tvContent.setText(noteInfo[2].trim());
            }
            viewHolder.tvLMTime.setText(Note_GetDateTimeString.getDateTimeString(Long.parseLong(noteInfo[4].trim()),false));
            return view;
        }

        private static class ViewHolder {
            TextView tvTitle;
            TextView tvContent;
            TextView tvLMTime;
        }
    }
}