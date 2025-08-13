package phanquan.ph58748.mob2041.models;

public class NguoiDung {
    private int mand;
    private String tennd;
    private String sdt;
    private String diachi;
    private String tendangnhap;
    private String matkhau;
    private int role;

    public NguoiDung(int mand, String tennd, String sdt, String diachi, String tendangnhap, String matkhau, int role) {
        this.mand = mand;
        this.tennd = tennd;
        this.sdt = sdt;
        this.diachi = diachi;
        this.tendangnhap = tendangnhap;
        this.matkhau = matkhau;
        this.role = role;
    }

    public NguoiDung(String tennd, String sdt, String diachi, String tendangnhap, String matkhau) {
        this.tennd = tennd;
        this.sdt = sdt;
        this.diachi = diachi;
        this.tendangnhap = tendangnhap;
        this.matkhau = matkhau;
    }

    public int getMand() {
        return mand;
    }

    public void setMand(int mand) {
        this.mand = mand;
    }

    public String getTennd() {
        return tennd;
    }

    public void setTennd(String tennd) {
        this.tennd = tennd;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getTendangnhap() {
        return tendangnhap;
    }

    public void setTendangnhap(String tendangnhap) {
        this.tendangnhap = tendangnhap;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
