package phanquan.ph58748.mob2041;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private LinearLayout linearLoaiSach , linearSach , linearPM , linearThongKe , linearLSPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        InitUI();
        InitEvent();

        SharedPreferences sharedPreferences = getSharedPreferences("dataUser",MODE_PRIVATE);
        int role = sharedPreferences.getInt("role",-1);
        switch (role){
            case 1:
                linearLoaiSach.setVisibility(View.);
                break;
            case 2:
                break;
            case 3:
                break;
        }
    }

    private void InitEvent() {
        linearLoaiSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CategoryActivity.class));
            }
        });

        linearSach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, BookActivity.class));
            }
        });
    }

    private void InitUI() {

        linearLoaiSach = findViewById(R.id.linearLoaiSach);
        linearSach = findViewById(R.id.linearSach);
        linearPM = findViewById(R.id.linearPM);
        linearThongKe = findViewById(R.id.linearThongKe);
        linearLSPM = findViewById(R.id.linearLSPM);
    }
}