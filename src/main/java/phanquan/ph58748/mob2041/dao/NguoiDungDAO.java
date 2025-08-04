package phanquan.ph58748.mob2041.dao;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import phanquan.ph58748.mob2041.database.DbHelper;

public class NguoiDungDAO  {
    private DbHelper dbHelper;
    private SharedPreferences sharedPreferences;

    public NguoiDungDAO(Context context) {

        dbHelper = new DbHelper(context);

        sharedPreferences = context.getSharedPreferences("dataUser",Context.MODE_PRIVATE);
    }

    //kiểm tra đăng nhập
    public boolean KiemTraDangNhap(String username, String password){
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM NGUOIDUNG WHERE tendangnhap = ? AND matkhau = ?", new String[]{username, password});
//        if (cursor.getCount() > 0){
//            return true;
//
//        }
        if(cursor.getCount() > 0){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("role",cursor.getInt(6));
            editor.apply();
        }


        return cursor.getCount() > 0;

    }
}
