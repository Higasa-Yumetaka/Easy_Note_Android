package com.example.Note_Todo;
//书籍数据库
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDBHelper extends SQLiteOpenHelper {
    public static final String CREATE_NOTES = "create table Notes("
            + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "title TEXT,"
            + "content TEXT,"
            + "created_time INTEGER,"
            + "last_modified_time INTEGER"
            + ")";

    //笔记示例
    public static final String ADD_EXAMPLES_LAST_YEAR ="insert into Notes(title,content,created_time,last_modified_time) values('去年的笔记','示例内容',1653207507000,1653799210000)";
    public static final String ADD_EXAMPLES_LAST_MONTH ="insert into Notes(title,content,created_time,last_modified_time) values('上个月的笔记','示例内容',1682151314000,1682743210000)";
    public static final String ADD_EXAMPLES_2_DAY ="insert into Notes(title,content,created_time,last_modified_time) values('前天的笔记','示例内容',1684691942000,1685162410000)";
    public static final String ADD_EXAMPLES_LAST_DAY ="insert into Notes(title,content,created_time,last_modified_time) values('昨天的笔记','示例内容',1684778342000,1685248810000)";
    public static final String ADD_EXAMPLES_TODAY_1 ="insert into Notes(title,content,created_time,last_modified_time) values('今天凌晨的笔记','示例内容',1684864965000,1685302810000)";
    public static final String ADD_EXAMPLES_TODAY_2 ="insert into Notes(title,content,created_time,last_modified_time) values('今天上午的笔记','示例内容',1684886565000,1685320810000)";
    public static final String ADD_EXAMPLES_TODAY_3 ="insert into Notes(title,content,created_time,last_modified_time) values('今天下午的笔记','示例内容',1685338810000,1685338810000)";
    public static final String ADD_EXAMPLES_TODAY_4 ="insert into Notes(title,content,created_time,last_modified_time) values('今天晚上的笔记','示例内容',1685360410000,1685360410000)";
    public NoteDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库表
        db.execSQL(CREATE_NOTES);
        //插入示例
        db.execSQL(ADD_EXAMPLES_LAST_YEAR);
        db.execSQL(ADD_EXAMPLES_LAST_MONTH);
        db.execSQL(ADD_EXAMPLES_2_DAY);
        db.execSQL(ADD_EXAMPLES_LAST_DAY);
        db.execSQL(ADD_EXAMPLES_TODAY_1);
        db.execSQL(ADD_EXAMPLES_TODAY_2);
        db.execSQL(ADD_EXAMPLES_TODAY_3);
        db.execSQL(ADD_EXAMPLES_TODAY_4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}