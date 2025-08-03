package phanquan.ph58748.mob2041;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import phanquan.ph58748.mob2041.adapter.LoaiSachAdapter;
import phanquan.ph58748.mob2041.dao.LoaiSachDAO;
import phanquan.ph58748.mob2041.models.LoaiSach;

public class CategoryActivity extends AppCompatActivity {

    private LoaiSachDAO loaiSachDAO;

    private RecyclerView recyclerViewCategory;
    private FloatingActionButton floatAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        InitUI();
        InitData();
        InitEvent();
    }

    private void InitEvent() {
        floatAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogThem();
            }
        });
    }

    private void InitData() {
        loaiSachDAO = new LoaiSachDAO(this);
        ArrayList<LoaiSach> list = loaiSachDAO.getDSLoaiSach();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewCategory.setLayoutManager(linearLayoutManager);
        LoaiSachAdapter loaiSachAdapter = new LoaiSachAdapter(this,list);
        recyclerViewCategory.setAdapter(loaiSachAdapter);
    }

    private void InitUI() {
        recyclerViewCategory = findViewById(R.id.recyclerViewCategory);
        floatAdd = findViewById(R.id.floatAdd);
    }

    private void showDialogThem(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_category,null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        EditText edtTenLoai = view.findViewById(R.id.edtTenLoai);
        Button btnLuu = view.findViewById(R.id.btnLuu);
        Button btnHuy = view.findViewById(R.id.btnHuy);

        btnLuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenLoai = edtTenLoai.getText().toString();

                if(tenLoai.equals("")){
                    Toast.makeText(CategoryActivity.this, "Vui lòng nhập tên loại sách", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean check = loaiSachDAO.themLoaiSach(tenLoai);

                if (check){
                    Toast.makeText(CategoryActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    InitData();
                    alertDialog.dismiss();

                }else {
                    Toast.makeText(CategoryActivity.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();

                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


    }

}