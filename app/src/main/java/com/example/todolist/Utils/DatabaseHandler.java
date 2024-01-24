package com.example.todolist.Utils;

import static android.icu.lang.UProperty.NAME;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.todolist.Model.ToDoModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final  String Name = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID ="id";
    private static final String TASK ="task";
    private static final String STATUS ="status";
    private static final String CREATE_TODO_TABlE="CREATE TABLE "+TODO_TABLE+"("+ID+"INTEGER PRIMARY KEY AUTOINCREMENT,"+STATUS+"INTEGER)";
    private SQLiteDatabase db;
    public DatabaseHandler(Context context){
        super(context, String.valueOf(NAME),null,VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
         db.execSQL(CREATE_TODO_TABlE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //drop the older tables
        db.execSQL("DROP TABLE IF EXISTS "+TODO_TABLE);
        //Create tables again
        onCreate(db);

    }
    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertTask(ToDoModel task){
        ContentValues cv = new ContentValues();
        cv.put(TASK, task.getTask());
        cv.put(STATUS,0);
        db.insert(TODO_TABLE, null ,cv);

    }
    public List<ToDoModel> getAllTasks(){
        List<ToDoModel> tasksList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE,null,null,null,null,null,null,null);
            if (cur !=null){
                if(cur.moveToFirst()){
                    do {
                        int ColumnIndex=cur.getColumnIndex(ID);
                        int IndexTask=cur.getColumnIndex(TASK);
                        int IndexStatus = cur.getColumnIndex(STATUS);
                        if(ColumnIndex != -1 && IndexTask !=-1 &&IndexStatus!=-1) {
                            ToDoModel task = new ToDoModel();
                            task.setId(cur.getInt(ColumnIndex));
                            task.setTask(cur.getString(IndexTask));
                            task.setStatus(cur.getInt(IndexStatus));
                            tasksList.add(task);
                        }else {
                            Log.e("Error","Column not found"+ID);
                            Log.e("Error","Column not found"+TASK);
                            Log.e("Error","Column not found"+STATUS);

                        }
                    }while(cur.moveToNext());

                    }
                }
            }
            finally {
            db.endTransaction();
            cur.close();
        }
        return tasksList;

        }


    public void updateStatus(int id,int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS,status);
        db.update(TODO_TABLE,cv,ID +"=?", new String[] {String.valueOf(id)} );

    }
    public void updateTask(int id , String task){
        ContentValues cv =new ContentValues();
        cv.put(TASK,task);
        db.update(TODO_TABLE,cv,ID + "=?" ,new String[]{String.valueOf(id)});

    }
    public void deleteTask(int id ){
        db.delete(TODO_TABLE,ID +"=?", new String[] {String.valueOf(id)});

    }

    public void insertTask(String task, String text) {
    }
}
