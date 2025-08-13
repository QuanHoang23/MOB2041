package phanquan.ph58748.mob2041.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import phanquan.ph58748.mob2041.database.DbHelper;
import phanquan.ph58748.mob2041.models.Sach;

public class SachDAO {
    private DbHelper dbHelper;
    
    public SachDAO(Context context){
        dbHelper = new DbHelper(context);
    }

    // lấy danh sách các cuốn sách
    public ArrayList<Sach> getDSSach(){
        ArrayList<Sach> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        /*
        SELECT * FROM SACH --> masach,tensach,tacgia,giaban,maloai
        SELECT s.masach,s.tensach,s.tacgia,s.giaban,s.maloai,l.tenloai FROM SACH s,LOAISACH l WHERE s.maloai = l.maloai
         */

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT s.masach,s.tensach,s.tacgia,s.giaban,s.maloai,l.tenloai FROM SACH s,LOAISACH l WHERE s.maloai = l.maloai",null);
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            do {
                list.add(new Sach(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        return list;
    }

    // Thêm sách mới
    public boolean themSach(String tensach, String tacgia, int giaban, int maloai) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tensach", tensach);
        contentValues.put("tacgia", tacgia);
        contentValues.put("giaban", giaban);
        contentValues.put("maloai", maloai);

        long check = sqLiteDatabase.insert("SACH", null, contentValues);
        return check != -1;
    }

    // Sửa sách
    public boolean suaSach(int masach, String tensach, String tacgia, int giaban, int maloai) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("tensach", tensach);
        contentValues.put("tacgia", tacgia);
        contentValues.put("giaban", giaban);
        contentValues.put("maloai", maloai);

        long check = sqLiteDatabase.update("SACH", contentValues, "masach = ?", new String[]{String.valueOf(masach)});
        return check > 0;
    }

    // Xóa sách
    public boolean xoaSach(int masach) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        long check = sqLiteDatabase.delete("SACH", "masach = ?", new String[]{String.valueOf(masach)});
        return check > 0;
    }

    // Lấy sách theo mã
    public Sach getSachTheoMa(int masach) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT s.masach,s.tensach,s.tacgia,s.giaban,s.maloai,l.tenloai FROM SACH s,LOAISACH l WHERE s.maloai = l.maloai AND s.masach = ?", new String[]{String.valueOf(masach)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return new Sach(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5));
        }
        return null;
    }

    // Lấy danh sách loại sách để hiển thị trong spinner
    public ArrayList<String> getDSLoaiSach() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT tenloai FROM LOAISACH", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        return list;
    }

    // Lấy mã loại sách theo tên
    public int getMaLoaiTheoTen(String tenloai) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT maloai FROM LOAISACH WHERE tenloai = ?", new String[]{tenloai});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(0);
        }
        return -1;
    }
}
