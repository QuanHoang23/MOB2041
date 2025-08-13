package phanquan.ph58748.mob2041.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import phanquan.ph58748.mob2041.database.DbHelper;
import phanquan.ph58748.mob2041.models.NguoiDung;

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
            cursor.moveToFirst();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("role",cursor.getInt(6));
            editor.apply();
        }


        return cursor.getCount() > 0;

    }

    // Lấy thông tin người dùng theo tên đăng nhập và mật khẩu
    public NguoiDung getNguoiDungTheoDangNhap(String username, String password) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM NGUOIDUNG WHERE tendangnhap = ? AND matkhau = ?", new String[]{username, password});
        
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            NguoiDung nguoiDung = new NguoiDung(
                cursor.getInt(0), // mand
                cursor.getString(1), // tennd
                cursor.getString(2), // sdt
                cursor.getString(3), // diachi
                cursor.getString(4), // tendangnhap
                cursor.getString(5), // matkhau
                cursor.getInt(6)  // role
            );
            cursor.close();
            return nguoiDung;
        }
        cursor.close();
        return null;
    }

    // đăng ký

    public  boolean dangKyTaiKhoan(NguoiDung nguoiDung){
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tennd",nguoiDung.getTennd());
        contentValues.put("sdt",nguoiDung.getSdt());
        contentValues.put("diachi",nguoiDung.getDiachi());
        contentValues.put("tendangnhap",nguoiDung.getTendangnhap());
        contentValues.put("matkhau",nguoiDung.getMatkhau());
        contentValues.put("role",1);

        long check = sqLiteDatabase.insert("NGUOIDUNG",null,contentValues);
        return  check != -1;

    }
}
