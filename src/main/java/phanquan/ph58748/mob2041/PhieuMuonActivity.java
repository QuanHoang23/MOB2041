package phanquan.ph58748.mob2041;

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

public class PhieuMuonActivity extends AppCompatActivity implements PhieuMuonAdapter.PhieuMuonAdapterListener {

    private RecyclerView recyclerViewPhieuMuon;
    private PhieuMuonDAO phieuMuonDAO;
    private FloatingActionButton floatAdd;
    private PhieuMuonAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_phieu_muon);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
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
        LoadData();
    }

    private void InitEvent() {
        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogThem();
            }
        });
    }

    private void LoadData() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewPhieuMuon.setLayoutManager(linearLayoutManager);
        adapter = new PhieuMuonAdapter(phieuMuonDAO.getDSPhieuMuon(), this, this);
        recyclerViewPhieuMuon.setAdapter(adapter);
    }

    // Hiển thị dialog thêm phiếu mượn mới
    public void showDialogThem() {
        showDialogPhieuMuon("Thêm Phiếu Mượn Mới", null);
    }

    // Hiển thị dialog sửa phiếu mượn
    public void showDialogSua(PhieuMuon phieuMuon) {
        showDialogPhieuMuon("Sửa Thông Tin Phiếu Mượn", phieuMuon);
    }

    // Hiển thị dialog chung cho thêm/sửa phiếu mượn
    private void showDialogPhieuMuon(String tieuDe, PhieuMuon phieuMuon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_phieu_muon, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        // Khởi tạo các view
        EditText edtNgayMuon = view.findViewById(R.id.edtNgayMuon);
        EditText edtNgayTra = view.findViewById(R.id.edtNgayTra);
        Spinner spinnerNguoiMuon = view.findViewById(R.id.spinnerNguoiMuon);
        Button btnLuu = view.findViewById(R.id.btnLuu);
        Button btnHuy = view.findViewById(R.id.btnHuy);

        // Cập nhật tiêu đề
        TextView tvTieuDe = view.findViewById(R.id.ttTieuDe);
        tvTieuDe.setText(tieuDe);

        // Load danh sách người dùng vào spinner
        ArrayList<String> listNguoiDung = phieuMuonDAO.getDSNguoiDung();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listNguoiDung);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNguoiMuon.setAdapter(spinnerAdapter);

        // Nếu là sửa phiếu mượn, điền thông tin hiện tại
        if (phieuMuon != null) {
            edtNgayMuon.setText(phieuMuon.getNgaymuon());
            edtNgayTra.setText(phieuMuon.getNgaytra());
            
            // Chọn người mượn hiện tại trong spinner
            int position = listNguoiDung.indexOf(phieuMuon.getTenNguoiMuon());
            if (position >= 0) {
                spinnerNguoiMuon.setSelection(position);
            }
        } else {
            // Nếu thêm mới, điền ngày hiện tại
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String ngayHienTai = sdf.format(new Date());
            edtNgayMuon.setText(ngayHienTai);
            
            // Ngày trả mặc định là 7 ngày sau
            try {
                Date ngayMuon = sdf.parse(ngayHienTai);
                ngayMuon.setTime(ngayMuon.getTime() + (7 * 24 * 60 * 60 * 1000L));
                edtNgayTra.setText(sdf.format(ngayMuon));
            } catch (Exception e) {
                edtNgayTra.setText("");
            }
        }

        // Xử lý sự kiện nút Lưu
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ngayMuon = edtNgayMuon.getText().toString().trim();
                String ngayTra = edtNgayTra.getText().toString().trim();
                String tenNguoiMuon = spinnerNguoiMuon.getSelectedItem().toString();

                // Kiểm tra dữ liệu
                if (ngayMuon.isEmpty()) {
                    Toast.makeText(PhieuMuonActivity.this, "Vui lòng nhập ngày mượn", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ngayTra.isEmpty()) {
                    Toast.makeText(PhieuMuonActivity.this, "Vui lòng nhập ngày trả", Toast.LENGTH_SHORT).show();
                    return;
                }

                int maNguoiDung = phieuMuonDAO.getMaNguoiDungTheoTen(tenNguoiMuon);
                if (maNguoiDung == -1) {
                    Toast.makeText(PhieuMuonActivity.this, "Người mượn không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean check;
                if (phieuMuon == null) {
                    // Thêm phiếu mượn mới
                    long mapm = phieuMuonDAO.themPhieuMuon(ngayMuon, ngayTra, maNguoiDung);
                    if (mapm != -1) {
                        Toast.makeText(PhieuMuonActivity.this, "Thêm phiếu mượn thành công", Toast.LENGTH_SHORT).show();
                        check = true;
                    } else {
                        Toast.makeText(PhieuMuonActivity.this, "Thêm phiếu mượn thất bại", Toast.LENGTH_SHORT).show();
                        check = false;
                    }
                } else {
                    // Sửa phiếu mượn
                    check = phieuMuonDAO.suaPhieuMuon(phieuMuon.getMapm(), ngayMuon, ngayTra, maNguoiDung);
                    if (check) {
                        Toast.makeText(PhieuMuonActivity.this, "Sửa phiếu mượn thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PhieuMuonActivity.this, "Sửa phiếu mượn thất bại", Toast.LENGTH_SHORT).show();
                    }
                }

                if (check) {
                    LoadData(); // Reload data
                    alertDialog.dismiss();
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

    // Xóa phiếu mượn
    public void xoaPhieuMuon(PhieuMuon phieuMuon) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa phiếu mượn '" + phieuMuon.getMapm() + "' không?\nTất cả sách trong phiếu mượn sẽ bị xóa!")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean check = phieuMuonDAO.xoaPhieuMuon(phieuMuon.getMapm());
                    if (check) {
                        Toast.makeText(PhieuMuonActivity.this, "Xóa phiếu mượn thành công", Toast.LENGTH_SHORT).show();
                        LoadData(); // Reload data
                    } else {
                        Toast.makeText(PhieuMuonActivity.this, "Xóa phiếu mượn thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Implement interface PhieuMuonAdapterListener
    @Override
    public void onEditPhieuMuon(PhieuMuon phieuMuon) {
        showDialogSua(phieuMuon);
    }

    @Override
    public void onDeletePhieuMuon(PhieuMuon phieuMuon) {
        xoaPhieuMuon(phieuMuon);
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
