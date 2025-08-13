package phanquan.ph58748.mob2041.models;

import java.util.ArrayList;
import java.util.List;

public class PhieuMuon {
    private int mapm;
    private String ngaymuon;
    private String ngaytra;
    private int mand;
    private String tenNguoiMuon;
    private String sdtNguoiMuon;
    private List<ChiTietPhieuMuon> danhSachSach;
    private int tongSoSach;
    private double tongTien;

    public PhieuMuon() {
        this.danhSachSach = new ArrayList<>();
    }

    public PhieuMuon(int mapm, String ngaymuon, String ngaytra, int mand) {
        this.mapm = mapm;
        this.ngaymuon = ngaymuon;
        this.ngaytra = ngaytra;
        this.mand = mand;
        this.danhSachSach = new ArrayList<>();
    }

    public PhieuMuon(int mapm, String ngaymuon, String ngaytra, int mand, String tenNguoiMuon, String sdtNguoiMuon) {
        this.mapm = mapm;
        this.ngaymuon = ngaymuon;
        this.ngaytra = ngaytra;
        this.mand = mand;
        this.tenNguoiMuon = tenNguoiMuon;
        this.sdtNguoiMuon = sdtNguoiMuon;
        this.danhSachSach = new ArrayList<>();
    }

    // Getters and Setters
    public int getMapm() {
        return mapm;
    }

    public void setMapm(int mapm) {
        this.mapm = mapm;
    }

    public String getNgaymuon() {
        return ngaymuon;
    }

    public void setNgaymuon(String ngaymuon) {
        this.ngaymuon = ngaymuon;
    }

    public String getNgaytra() {
        return ngaytra;
    }

    public void setNgaytra(String ngaytra) {
        this.ngaytra = ngaytra;
    }

    public int getMand() {
        return mand;
    }

    public void setMand(int mand) {
        this.mand = mand;
    }

    public String getTenNguoiMuon() {
        return tenNguoiMuon;
    }

    public void setTenNguoiMuon(String tenNguoiMuon) {
        this.tenNguoiMuon = tenNguoiMuon;
    }

    public String getSdtNguoiMuon() {
        return sdtNguoiMuon;
    }

    public void setSdtNguoiMuon(String sdtNguoiMuon) {
        this.sdtNguoiMuon = sdtNguoiMuon;
    }

    public List<ChiTietPhieuMuon> getDanhSachSach() {
        return danhSachSach;
    }

    public void setDanhSachSach(List<ChiTietPhieuMuon> danhSachSach) {
        this.danhSachSach = danhSachSach;
    }

    public int getTongSoSach() {
        return tongSoSach;
    }

    public void setTongSoSach(int tongSoSach) {
        this.tongSoSach = tongSoSach;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }

    // Thêm sách vào phiếu mượn
    public void themSach(ChiTietPhieuMuon ctpm) {
        this.danhSachSach.add(ctpm);
        this.tongSoSach += ctpm.getSoluong();
        this.tongTien += ctpm.getThanhTien();
    }

    // Xóa sách khỏi phiếu mượn
    public void xoaSach(ChiTietPhieuMuon ctpm) {
        this.danhSachSach.remove(ctpm);
        this.tongSoSach -= ctpm.getSoluong();
        this.tongTien -= ctpm.getThanhTien();
    }

    // Tính tổng tiền
    public void tinhTongTien() {
        this.tongTien = 0;
        this.tongSoSach = 0;
        for (ChiTietPhieuMuon ctpm : danhSachSach) {
            this.tongTien += ctpm.getThanhTien();
            this.tongSoSach += ctpm.getSoluong();
        }
    }
}
