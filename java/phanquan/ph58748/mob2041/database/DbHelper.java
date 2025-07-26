package phanquan.ph58748.mob2041.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context,"QUANLYTHUVIEN",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // bảng loại sách
        String tLoaiSach = "CREATE TABLE LOAISACH(maloai integer primary key autoincrement, tenloai text)";
        db.execSQL(tLoaiSach);

        // data mẫu loại sách
        db.execSQL("INSERT INTO LOAISACH VALUES(1,'thiếu nhi'),(2,'tình cảm'),(3,'hành động')");


        // bảng sách
        String tSach = "CREATE TABLE SACH(masach integer primary key autoincrement, tensach text, tacgia text, giaban integer, maloai integer references LOAISACH(maloai))";
        db.execSQL(tSach);

        // data mẫu sách

        db.execSQL("INSERT INTO SACH VALUES(1,'kể cho em nghe','Phan Hoàng Quân',15000,1),(2,'Trạng Quỳnh','Kim Đồng',10000,1)");



        // bảng người dùng
        /*
        role:
        1 - người dùng
        2 - thủ thư
        3 - admin
         */
        String tNguoiDung = "CREATE TABLE NGUOIDUNG(mand integer primary key autoincrement, tennd text, sdt text, diachi text , tendangnhap text, matkhau text, role integer)";
        db.execSQL(tNguoiDung);


        //data mẫu người dùng
        db.execSQL("INSERT INTO NGUOIDUNG VALUES(1,'Phan Hoàng Quân','0325488221','Hải tiến Ninh Bình','quan23','123456',1)," +
                "(2,'Ngô Văn Mạnh','0355488221','Hải tiến Ninh Bình','manh12','1234567',2)," +
                "(3,'Lê Văn Long','0355498221','Hải tiến Ninh Bình','long12','12345678',3)");




        // bảng phiếu mượn
        String tPhieuMuon = "CREATE TABLE PHIEUMUON(mapm integer primary key autoincrement, ngaymuon text , ngaytra text , mand integer references NGUOIDUNG(mand))";
        db.execSQL(tPhieuMuon);


        // data mẫu phiếu mượn
        db.execSQL("INSERT INTO PHIEUMUON VALUES(1,'20/7/2025','27/7/2025',1)");


        // bảng chi tiết phiếu mượn
        String tCTPM = "CREATE TABLE CTPM(mapm integer primary key references PHIEUMUON(mapm), masach integer primary key references SACH(masach), soluong integer)";
        db.execSQL(tCTPM);

        // data mẫu chi tiết phiếu mượn
        db.execSQL("INSERT INTO CTPM VALUES(1,1,2),(1,2,1)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion != newVersion){
            db.execSQL("DROP TABLE IF EXISTS LOAISACH");
            db.execSQL("DROP TABLE IF EXISTS SACH");
            db.execSQL("DROP TABLE IF EXISTS NGUOIDUNG");
            db.execSQL("DROP TABLE IF EXISTS PHIEUMUON");
            db.execSQL("DROP TABLE IF EXISTS CTPM");
            onCreate(db);
        }

    }
}
