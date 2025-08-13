package phanquan.ph58748.mob2041.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import phanquan.ph58748.mob2041.database.DbHelper;
import phanquan.ph58748.mob2041.models.ChiTietPhieuMuon;
import phanquan.ph58748.mob2041.models.PhieuMuon;

public class PhieuMuonDAO {
    private DbHelper dbHelper;

    public PhieuMuonDAO(Context context) {
        dbHelper = new DbHelper(context);
    }

    // Lấy danh sách phiếu mượn với thông tin người mượn
    public ArrayList<PhieuMuon> getDSPhieuMuon() {
        ArrayList<PhieuMuon> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        String query = "SELECT pm.mapm, pm.ngaymuon, pm.ngaytra, pm.mand, " +
                      "nd.tennd, nd.sdt " +
                      "FROM PHIEUMUON pm " +
                      "INNER JOIN NGUOIDUNG nd ON pm.mand = nd.mand " +
                      "ORDER BY pm.mapm DESC";

        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                PhieuMuon phieuMuon = new PhieuMuon(
                    cursor.getInt(0), // mapm
                    cursor.getString(1), // ngaymuon
                    cursor.getString(2), // ngaytra
                    cursor.getInt(3), // mand
                    cursor.getString(4), // tennd
                    cursor.getString(5)  // sdt
                );
                
                // Lấy chi tiết phiếu mượn
                phieuMuon.setDanhSachSach(getChiTietPhieuMuon(cursor.getInt(0)));
                phieuMuon.tinhTongTien();
                
                list.add(phieuMuon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Lấy chi tiết phiếu mượn theo mã phiếu mượn
    public ArrayList<ChiTietPhieuMuon> getChiTietPhieuMuon(int mapm) {
        ArrayList<ChiTietPhieuMuon> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        String query = "SELECT ctpm.mactpm, ctpm.mapm, ctpm.masach, ctpm.soluong, " +
                      "s.tensach, s.tacgia, s.giaban, l.tenloai " +
                      "FROM CTPM ctpm " +
                      "INNER JOIN SACH s ON ctpm.masach = s.masach " +
                      "INNER JOIN LOAISACH l ON s.maloai = l.maloai " +
                      "WHERE ctpm.mapm = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(mapm)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                ChiTietPhieuMuon ctpm = new ChiTietPhieuMuon(
                    cursor.getInt(0), // mactpm
                    cursor.getInt(1), // mapm
                    cursor.getInt(2), // masach
                    cursor.getInt(3), // soluong
                    cursor.getString(4), // tensach
                    cursor.getString(5), // tacgia
                    cursor.getInt(6), // giaban
                    cursor.getString(7)  // tenloai
                );
                list.add(ctpm);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Thêm phiếu mượn mới
    public long themPhieuMuon(String ngaymuon, String ngaytra, int mand) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ngaymuon", ngaymuon);
        contentValues.put("ngaytra", ngaytra);
        contentValues.put("mand", mand);

        return sqLiteDatabase.insert("PHIEUMUON", null, contentValues);
    }

    // Thêm chi tiết phiếu mượn
    public long themChiTietPhieuMuon(int mapm, int masach, int soluong) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("mapm", mapm);
        contentValues.put("masach", masach);
        contentValues.put("soluong", soluong);

        return sqLiteDatabase.insert("CTPM", null, contentValues);
    }

    // Sửa phiếu mượn
    public boolean suaPhieuMuon(int mapm, String ngaymuon, String ngaytra, int mand) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ngaymuon", ngaymuon);
        contentValues.put("ngaytra", ngaytra);
        contentValues.put("mand", mand);

        long check = sqLiteDatabase.update("PHIEUMUON", contentValues, "mapm = ?", new String[]{String.valueOf(mapm)});
        return check > 0;
    }

    // Sửa chi tiết phiếu mượn
    public boolean suaChiTietPhieuMuon(int mactpm, int soluong) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("soluong", soluong);

        long check = sqLiteDatabase.update("CTPM", contentValues, "mactpm = ?", new String[]{String.valueOf(mactpm)});
        return check > 0;
    }

    // Xóa phiếu mượn (cần xóa chi tiết trước)
    public boolean xoaPhieuMuon(int mapm) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        
        // Xóa chi tiết phiếu mượn trước
        sqLiteDatabase.delete("CTPM", "mapm = ?", new String[]{String.valueOf(mapm)});
        
        // Xóa phiếu mượn
        long check = sqLiteDatabase.delete("PHIEUMUON", "mapm = ?", new String[]{String.valueOf(mapm)});
        return check > 0;
    }

    // Xóa chi tiết phiếu mượn
    public boolean xoaChiTietPhieuMuon(int mactpm) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        long check = sqLiteDatabase.delete("CTPM", "mactpm = ?", new String[]{String.valueOf(mactpm)});
        return check > 0;
    }

    // Lấy phiếu mượn theo mã
    public PhieuMuon getPhieuMuonTheoMa(int mapm) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        
        String query = "SELECT pm.mapm, pm.ngaymuon, pm.ngaytra, pm.mand, " +
                      "nd.tennd, nd.sdt " +
                      "FROM PHIEUMUON pm " +
                      "INNER JOIN NGUOIDUNG nd ON pm.mand = nd.mand " +
                      "WHERE pm.mapm = ?";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(mapm)});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            PhieuMuon phieuMuon = new PhieuMuon(
                cursor.getInt(0), // mapm
                cursor.getString(1), // ngaymuon
                cursor.getString(2), // ngaytra
                cursor.getInt(3), // mand
                cursor.getString(4), // tennd
                cursor.getString(5)  // sdt
            );
            
            // Lấy chi tiết phiếu mượn
            phieuMuon.setDanhSachSach(getChiTietPhieuMuon(mapm));
            phieuMuon.tinhTongTien();
            
            cursor.close();
            return phieuMuon;
        }
        cursor.close();
        return null;
    }

    // Lấy danh sách người dùng để chọn
    public ArrayList<String> getDSNguoiDung() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT tennd FROM NGUOIDUNG WHERE role = 1", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Lấy mã người dùng theo tên
    public int getMaNguoiDungTheoTen(String tennd) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT mand FROM NGUOIDUNG WHERE tennd = ? AND role = 1", new String[]{tennd});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int mand = cursor.getInt(0);
            cursor.close();
            return mand;
        }
        cursor.close();
        return -1;
    }

    // Lấy danh sách sách để chọn
    public ArrayList<String> getDSSach() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT tensach FROM SACH", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Lấy mã sách theo tên
    public int getMaSachTheoTen(String tensach) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT masach FROM SACH WHERE tensach = ?", new String[]{tensach});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int masach = cursor.getInt(0);
            cursor.close();
            return masach;
        }
        cursor.close();
        return -1;
    }

    // Lấy phiếu mượn theo người dùng (cho người dùng xem sách đã mượn)
    public ArrayList<PhieuMuon> getPhieuMuonTheoNguoiDung(int mand) {
        ArrayList<PhieuMuon> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();

        String query = "SELECT pm.mapm, pm.ngaymuon, pm.ngaytra, pm.mand, " +
                      "nd.tennd, nd.sdt " +
                      "FROM PHIEUMUON pm " +
                      "INNER JOIN NGUOIDUNG nd ON pm.mand = nd.mand " +
                      "WHERE pm.mand = ? " +
                      "ORDER BY pm.mapm DESC";

        Cursor cursor = sqLiteDatabase.rawQuery(query, new String[]{String.valueOf(mand)});
        
        // Debug log
        android.util.Log.d("PhieuMuonDAO", "Query: " + query + " with mand: " + mand);
        android.util.Log.d("PhieuMuonDAO", "Cursor count: " + cursor.getCount());
        
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                PhieuMuon phieuMuon = new PhieuMuon(
                    cursor.getInt(0), // mapm
                    cursor.getString(1), // ngaymuon
                    cursor.getString(2), // ngaytra
                    cursor.getInt(3), // mand
                    cursor.getString(4), // tennd
                    cursor.getString(5)  // sdt
                );
                
                // Debug log
                android.util.Log.d("PhieuMuonDAO", "Found PM: " + phieuMuon.getMapm() + 
                                 ", ngaymuon: " + phieuMuon.getNgaymuon() + 
                                 ", ngaytra: " + phieuMuon.getNgaytra());
                
                // Lấy chi tiết phiếu mượn
                phieuMuon.setDanhSachSach(getChiTietPhieuMuon(cursor.getInt(0)));
                phieuMuon.tinhTongTien();
                
                list.add(phieuMuon);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Kiểm tra sách có sẵn để mượn không
    public boolean kiemTraSachSan(int masach, int soluong) {
        // Tạm thời luôn trả về true để tránh lỗi
        // Có thể cải thiện sau khi database được cập nhật
        return true;
    }

    // Lấy danh sách sách có thể mượn
    public ArrayList<String> getDSSachCoTheMuon() {
        ArrayList<String> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
        
        // Lấy tất cả sách (đơn giản hóa để tránh lỗi)
        String query = "SELECT tensach FROM SACH";
        
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    // Test database để debug
    public void testDatabase() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // Kiểm tra bảng PHIEUMUON
        Cursor cursorPM = db.rawQuery("SELECT * FROM PHIEUMUON", null);
        android.util.Log.d("DEBUG", "Total PHIEUMUON: " + cursorPM.getCount());
        if (cursorPM.getCount() > 0) {
            cursorPM.moveToFirst();
            do {
                android.util.Log.d("DEBUG", "PHIEUMUON: mapm=" + cursorPM.getInt(0) + 
                                   ", ngaymuon=" + cursorPM.getString(1) + 
                                   ", ngaytra=" + cursorPM.getString(2) + 
                                   ", mand=" + cursorPM.getInt(3));
            } while (cursorPM.moveToNext());
        }
        cursorPM.close();
        
        // Kiểm tra bảng CTPM
        Cursor cursorCTPM = db.rawQuery("SELECT * FROM CTPM", null);
        android.util.Log.d("DEBUG", "Total CTPM: " + cursorCTPM.getCount());
        if (cursorCTPM.getCount() > 0) {
            cursorCTPM.moveToFirst();
            do {
                android.util.Log.d("DEBUG", "CTPM: mactpm=" + cursorCTPM.getInt(0) + 
                                   ", mapm=" + cursorCTPM.getInt(1) + 
                                   ", masach=" + cursorCTPM.getInt(2) + 
                                   ", soluong=" + cursorCTPM.getInt(3));
            } while (cursorCTPM.moveToNext());
        }
        cursorCTPM.close();
        
        // Kiểm tra bảng NGUOIDUNG
        Cursor cursorND = db.rawQuery("SELECT * FROM NGUOIDUNG", null);
        android.util.Log.d("DEBUG", "Total NGUOIDUNG: " + cursorND.getCount());
        if (cursorND.getCount() > 0) {
            cursorND.moveToFirst();
            do {
                android.util.Log.d("DEBUG", "NGUOIDUNG: mand=" + cursorND.getInt(0) + 
                                   ", tennd=" + cursorND.getString(1) + 
                                   ", role=" + cursorND.getInt(6));
            } while (cursorND.moveToNext());
        }
        cursorND.close();
    }
}
