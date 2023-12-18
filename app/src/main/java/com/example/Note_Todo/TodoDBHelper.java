package com.example.Note_Todo;
//书籍数据库
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoDBHelper extends SQLiteOpenHelper {
    public static final String CREATE_NOTES = "create table Todo("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "content TEXT,"
            + "created_time INTEGER,"
            + "last_modified_time INTEGER,"
            + "is_completed INTEGER DEFAULT 0"//0表示未完成，1表示已完成
            + ")";
    private Context mContext;

    public TodoDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}