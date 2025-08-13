package phanquan.ph58748.mob2041.models;

public class ChiTietPhieuMuon {
    private int mactpm;
    private int mapm;
    private int masach;
    private int soluong;
    
    // Thông tin sách (join từ bảng SACH)
    private String tensach;
    private String tacgia;
    private int giaban;
    private String tenloai;
    
    // Tính toán
    private double thanhTien;

    public ChiTietPhieuMuon() {
    }

    public ChiTietPhieuMuon(int mactpm, int mapm, int masach, int soluong) {
        this.mactpm = mactpm;
        this.mapm = mapm;
        this.masach = masach;
        this.soluong = soluong;
        this.tinhThanhTien();
    }

    public ChiTietPhieuMuon(int mactpm, int mapm, int masach, int soluong, String tensach, String tacgia, int giaban, String tenloai) {
        this.mactpm = mactpm;
        this.mapm = mapm;
        this.masach = masach;
        this.soluong = soluong;
        this.tensach = tensach;
        this.tacgia = tacgia;
        this.giaban = giaban;
        this.tenloai = tenloai;
        this.tinhThanhTien();
    }

    // Getters and Setters
    public int getMactpm() {
        return mactpm;
    }

    public void setMactpm(int mactpm) {
        this.mactpm = mactpm;
    }

    public int getMapm() {
        return mapm;
    }

    public void setMapm(int mapm) {
        this.mapm = mapm;
    }

    public int getMasach() {
        return masach;
    }

    public void setMasach(int masach) {
        this.masach = masach;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
        this.tinhThanhTien();
    }

    public String getTensach() {
        return tensach;
    }

    public void setTensach(String tensach) {
        this.tensach = tensach;
    }

    public String getTacgia() {
        return tacgia;
    }

    public void setTacgia(String tacgia) {
        this.tacgia = tacgia;
    }

    public int getGiaban() {
        return giaban;
    }

    public void setGiaban(int giaban) {
        this.giaban = giaban;
        this.tinhThanhTien();
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }

    public double getThanhTien() {
        return thanhTien;
    }

    public void setThanhTien(double thanhTien) {
        this.thanhTien = thanhTien;
    }

    // Tính thành tiền
    private void tinhThanhTien() {
        this.thanhTien = this.giaban * this.soluong;
    }

    // Tính thành tiền với giá mới
    public void tinhThanhTien(int giaMoi) {
        this.thanhTien = giaMoi * this.soluong;
    }
}
