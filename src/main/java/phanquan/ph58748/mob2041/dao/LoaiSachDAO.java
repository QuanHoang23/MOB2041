package phanquan.ph58748.mob2041.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import phanquan.ph58748.mob2041.database.DbHelper;
import phanquan.ph58748.mob2041.models.LoaiSach;

public class LoaiSachDAO {

    private DbHelper dbHelper;
    public LoaiSachDAO(Context context) {
        dbHelper = new DbHelper(context);
    }
    // lấy danh sách loại sách

    public ArrayList<LoaiSach> getDSLoaiSach(){
        ArrayList<LoaiSach> list = new ArrayList<>();

        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM LOAISACH",null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                list.add(new LoaiSach(cursor.getInt(0),cursor.getString(1)));
            }while (cursor.moveToNext());

        }


        return list;
    }

    public boolean themLoaiSach(String tenLoai){

        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("tenloai",tenLoai);

         long check = sqLiteDatabase.insert("LOAISACH", null ,contentValues);

        return check != -1;

    }

}
