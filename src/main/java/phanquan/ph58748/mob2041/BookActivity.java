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

import java.util.ArrayList;

import phanquan.ph58748.mob2041.adapter.SachAdapter;
import phanquan.ph58748.mob2041.dao.SachDAO;
import phanquan.ph58748.mob2041.models.Sach;

public class BookActivity extends AppCompatActivity implements SachAdapter.SachAdapterListener {

    private RecyclerView recyclerViewSach;
    private SachDAO sachDAO;
    private FloatingActionButton floatAdd;
    private SachAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_book);
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
        recyclerViewSach = findViewById(R.id.recyclerViewSach);
        floatAdd = findViewById(R.id.floatAdd);
        sachDAO = new SachDAO(this);
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

    private void LoadData(){
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewSach.setLayoutManager(linearLayoutManager);
        adapter = new SachAdapter(sachDAO.getDSSach(), this, sachDAO, this);
        recyclerViewSach.setAdapter(adapter);
    }

    // Hiển thị dialog thêm sách mới
    public void showDialogThem(){
        showDialogSach("Thêm Sách Mới", null);
    }

    // Hiển thị dialog sửa sách
    public void showDialogSua(Sach sach){
        showDialogSach("Sửa Thông Tin Sách", sach);
    }

    // Hiển thị dialog chung cho thêm/sửa sách
    private void showDialogSach(String tieuDe, Sach sach){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_book, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        // Khởi tạo các view
        EditText edtTenSach = view.findViewById(R.id.edtTenSach);
        EditText edtTacGia = view.findViewById(R.id.edtTacGia);
        EditText edtGiaBan = view.findViewById(R.id.edtGiaBan);
        Spinner spinnerLoaiSach = view.findViewById(R.id.spinnerLoaiSach);
        Button btnLuu = view.findViewById(R.id.btnLuu);
        Button btnHuy = view.findViewById(R.id.btnHuy);

        // Cập nhật tiêu đề
        TextView tvTieuDe = view.findViewById(R.id.ttTieuDe);
        tvTieuDe.setText(tieuDe);

        // Load danh sách loại sách vào spinner
        ArrayList<String> listLoaiSach = sachDAO.getDSLoaiSach();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listLoaiSach);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLoaiSach.setAdapter(spinnerAdapter);

        // Nếu là sửa sách, điền thông tin hiện tại
        if (sach != null) {
            edtTenSach.setText(sach.getTensach());
            edtTacGia.setText(sach.getTacgia());
            edtGiaBan.setText(String.valueOf(sach.getGiaban()));
            
            // Chọn loại sách hiện tại trong spinner
            int position = listLoaiSach.indexOf(sach.getTenloai());
            if (position >= 0) {
                spinnerLoaiSach.setSelection(position);
            }
        }

        // Xử lý sự kiện nút Lưu
        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenSach = edtTenSach.getText().toString().trim();
                String tacGia = edtTacGia.getText().toString().trim();
                String giaBanStr = edtGiaBan.getText().toString().trim();
                String tenLoai = spinnerLoaiSach.getSelectedItem().toString();

                // Kiểm tra dữ liệu
                if (tenSach.isEmpty()) {
                    Toast.makeText(BookActivity.this, "Vui lòng nhập tên sách", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tacGia.isEmpty()) {
                    Toast.makeText(BookActivity.this, "Vui lòng nhập tên tác giả", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (giaBanStr.isEmpty()) {
                    Toast.makeText(BookActivity.this, "Vui lòng nhập giá bán", Toast.LENGTH_SHORT).show();
                    return;
                }

                int giaBan;
                try {
                    giaBan = Integer.parseInt(giaBanStr);
                    if (giaBan <= 0) {
                        Toast.makeText(BookActivity.this, "Giá bán phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                        return;
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(BookActivity.this, "Giá bán không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                int maLoai = sachDAO.getMaLoaiTheoTen(tenLoai);
                if (maLoai == -1) {
                    Toast.makeText(BookActivity.this, "Loại sách không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean check;
                if (sach == null) {
                    // Thêm sách mới
                    check = sachDAO.themSach(tenSach, tacGia, giaBan, maLoai);
                    if (check) {
                        Toast.makeText(BookActivity.this, "Thêm sách thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BookActivity.this, "Thêm sách thất bại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Sửa sách
                    check = sachDAO.suaSach(sach.getMasach(), tenSach, tacGia, giaBan, maLoai);
                    if (check) {
                        Toast.makeText(BookActivity.this, "Sửa sách thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(BookActivity.this, "Sửa sách thất bại", Toast.LENGTH_SHORT).show();
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

    // Xóa sách
    public void xoaSach(Sach sach) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sách '" + sach.getTensach() + "' không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean check = sachDAO.xoaSach(sach.getMasach());
                    if (check) {
                        Toast.makeText(BookActivity.this, "Xóa sách thành công", Toast.LENGTH_SHORT).show();
                        LoadData(); // Reload data
                    } else {
                        Toast.makeText(BookActivity.this, "Xóa sách thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // Implement interface SachAdapterListener
    @Override
    public void onEditSach(Sach sach) {
        showDialogSua(sach);
    }

    @Override
    public void onDeleteSach(Sach sach) {
        xoaSach(sach);
    }
}