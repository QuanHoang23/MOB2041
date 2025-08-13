package phanquan.ph58748.mob2041;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import phanquan.ph58748.mob2041.adapter.PhieuMuonAdapter;
import phanquan.ph58748.mob2041.dao.PhieuMuonDAO;
import phanquan.ph58748.mob2041.models.ChiTietPhieuMuon;
import phanquan.ph58748.mob2041.models.PhieuMuon;

public class UserBorrowActivity extends AppCompatActivity implements PhieuMuonAdapter.PhieuMuonAdapterListener {

    private RecyclerView recyclerViewPhieuMuon;
    private PhieuMuonDAO phieuMuonDAO;
    private FloatingActionButton floatAdd;
    private PhieuMuonAdapter adapter;
    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_borrow);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Lấy thông tin người dùng hiện tại
        SharedPreferences sharedPreferences = getSharedPreferences("dataUser", MODE_PRIVATE);
        currentUserId = sharedPreferences.getInt("mand", -1);
        
        // Debug log
        android.util.Log.d("DEBUG", "UserBorrowActivity - currentUserId: " + currentUserId);
        android.util.Log.d("DEBUG", "UserBorrowActivity - SharedPreferences mand: " + sharedPreferences.getInt("mand", -1));
        android.util.Log.d("DEBUG", "UserBorrowActivity - SharedPreferences tennd: " + sharedPreferences.getString("tennd", "null"));
        android.util.Log.d("DEBUG", "UserBorrowActivity - SharedPreferences role: " + sharedPreferences.getInt("role", -1));
        
        InitUI();
        InitData();
        InitEvent();
    }

    private void InitUI() {
        recyclerViewPhieuMuon = findViewById(R.id.recyclerViewPhieuMuon);
        floatAdd = findViewById(R.id.floatAdd);
        phieuMuonDAO = new PhieuMuonDAO(this);
    }

    private void InitData() {
        // Test database trước
        phieuMuonDAO.testDatabase();
        LoadData();
    }

    private void InitEvent() {
        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogMuonSach();
            }
        });
    }

    private void LoadData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewPhieuMuon.setLayoutManager(linearLayoutManager);
        
        // Chỉ hiển thị phiếu mượn của người dùng hiện tại
        ArrayList<PhieuMuon> danhSachPhieuMuon = phieuMuonDAO.getPhieuMuonTheoNguoiDung(currentUserId);
        
        // Debug log
        Toast.makeText(this, "Tìm thấy " + danhSachPhieuMuon.size() + " phiếu mượn cho user " + currentUserId, Toast.LENGTH_SHORT).show();
        
        adapter = new PhieuMuonAdapter(danhSachPhieuMuon, this, this);
        recyclerViewPhieuMuon.setAdapter(adapter);
    }

    // Hiển thị dialog mượn sách
    private void showDialogMuonSach() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_user_borrow, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        // Khởi tạo các view
        Spinner spinnerSach = view.findViewById(R.id.spinnerSach);
        EditText edtSoLuong = view.findViewById(R.id.edtSoLuong);
        Button btnMuon = view.findViewById(R.id.btnMuon);
        Button btnHuy = view.findViewById(R.id.btnHuy);

        // Load danh sách sách có thể mượn
        ArrayList<String> listSach = phieuMuonDAO.getDSSachCoTheMuon();
        if (listSach.isEmpty()) {
            Toast.makeText(this, "Hiện tại không có sách nào để mượn", Toast.LENGTH_LONG).show();
            alertDialog.dismiss();
            return;
        }
        
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listSach);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSach.setAdapter(spinnerAdapter);

        // Xử lý sự kiện nút Mượn
        btnMuon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenSach = spinnerSach.getSelectedItem().toString();
                String soLuongStr = edtSoLuong.getText().toString().trim();

                // Kiểm tra dữ liệu
                if (soLuongStr.isEmpty()) {
                    Toast.makeText(UserBorrowActivity.this, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
                    return;
                }

                int soLuong = Integer.parseInt(soLuongStr);
                if (soLuong <= 0) {
                    Toast.makeText(UserBorrowActivity.this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Lấy mã sách
                int maSach = phieuMuonDAO.getMaSachTheoTen(tenSach);
                if (maSach == -1) {
                    Toast.makeText(UserBorrowActivity.this, "Sách không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra sách có sẵn không
                if (!phieuMuonDAO.kiemTraSachSan(maSach, soLuong)) {
                    Toast.makeText(UserBorrowActivity.this, "Sách không đủ số lượng để mượn", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Tạo phiếu mượn mới
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String ngayHienTai = sdf.format(new Date());
                
                // Ngày trả mặc định là 7 ngày sau
                String ngayTra = "";
                try {
                    Date ngayMuon = sdf.parse(ngayHienTai);
                    if (ngayMuon != null) {
                        ngayMuon.setTime(ngayMuon.getTime() + (7 * 24 * 60 * 60 * 1000L));
                        ngayTra = sdf.format(ngayMuon);
                    } else {
                        ngayTra = ngayHienTai;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    ngayTra = ngayHienTai;
                }

                // Thêm phiếu mượn
                long mapm = phieuMuonDAO.themPhieuMuon(ngayHienTai, ngayTra, currentUserId);
                if (mapm != -1) {
                    // Thêm chi tiết phiếu mượn
                    long mactpm = phieuMuonDAO.themChiTietPhieuMuon((int)mapm, maSach, soLuong);
                    if (mactpm != -1) {
                        Toast.makeText(UserBorrowActivity.this, "Mượn sách thành công! PM" + mapm, Toast.LENGTH_SHORT).show();
                        LoadData(); // Reload data
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(UserBorrowActivity.this, "Lỗi khi thêm chi tiết phiếu mượn", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UserBorrowActivity.this, "Lỗi khi tạo phiếu mượn", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện nút Hủy
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    // Implement interface PhieuMuonAdapterListener
    @Override
    public void onEditPhieuMuon(PhieuMuon phieuMuon) {
        // Người dùng không thể sửa phiếu mượn
        Toast.makeText(this, "Bạn không thể sửa phiếu mượn", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeletePhieuMuon(PhieuMuon phieuMuon) {
        // Người dùng không thể xóa phiếu mượn
        Toast.makeText(this, "Bạn không thể xóa phiếu mượn", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewChiTiet(PhieuMuon phieuMuon) {
        // Hiển thị chi tiết phiếu mượn
        showDialogChiTiet(phieuMuon);
    }

    // Hiển thị dialog chi tiết phiếu mượn
    private void showDialogChiTiet(PhieuMuon phieuMuon) {
        StringBuilder message = new StringBuilder();
        message.append("Mã phiếu: PM").append(String.format("%03d", phieuMuon.getMapm())).append("\n");
        message.append("Ngày mượn: ").append(phieuMuon.getNgaymuon()).append("\n");
        message.append("Ngày trả: ").append(phieuMuon.getNgaytra()).append("\n");
        message.append("Người mượn: ").append(phieuMuon.getTenNguoiMuon()).append("\n");
        message.append("SĐT: ").append(phieuMuon.getSdtNguoiMuon()).append("\n\n");
        message.append("Danh sách sách:\n");

        if (phieuMuon.getDanhSachSach() != null && !phieuMuon.getDanhSachSach().isEmpty()) {
            for (ChiTietPhieuMuon ctpm : phieuMuon.getDanhSachSach()) {
                message.append("• ").append(ctpm.getTensach())
                       .append(" - ").append(ctpm.getTacgia())
                       .append(" - SL: ").append(ctpm.getSoluong())
                       .append(" - ").append(String.format("%,d VNĐ", (int)ctpm.getThanhTien()))
                       .append("\n");
            }
        } else {
            message.append("Không có sách nào");
        }

        message.append("\nTổng: ").append(phieuMuon.getTongSoSach()).append(" cuốn - ")
               .append(String.format("%,.0f VNĐ", phieuMuon.getTongTien()));

        new AlertDialog.Builder(this)
                .setTitle("Chi Tiết Phiếu Mượn")
                .setMessage(message.toString())
                .setPositiveButton("Đóng", null)
                .show();
    }
}
