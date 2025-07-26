package phanquan.ph58748.mob2041;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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
}