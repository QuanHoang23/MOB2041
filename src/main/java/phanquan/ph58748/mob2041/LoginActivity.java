package phanquan.ph58748.mob2041;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import phanquan.ph58748.mob2041.dao.NguoiDungDAO;
import phanquan.ph58748.mob2041.models.NguoiDung;

public class LoginActivity extends AppCompatActivity {

    private EditText edtUser, edtPass;
    private Button btnLogin, btnRegister;
    private NguoiDungDAO nguoiDungDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        InitUI();
        InitEvent();
    }

    private void InitEvent() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = edtUser.getText().toString();
                String pass = edtPass.getText().toString();

                NguoiDung nguoiDung = nguoiDungDAO.getNguoiDungTheoDangNhap(user, pass);

                if(nguoiDung != null){
                    // Lưu thông tin người dùng vào SharedPreferences
                    SharedPreferences sharedPreferences = getSharedPreferences("dataUser", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("mand", nguoiDung.getMand());
                    editor.putString("tennd", nguoiDung.getTennd());
                    editor.putString("sdt", nguoiDung.getSdt());
                    editor.putInt("role", nguoiDung.getRole());
                    editor.apply();

                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công! Xin chào " + nguoiDung.getTennd(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu sai", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void InitUI() {
        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        nguoiDungDAO = new NguoiDungDAO(this);
        btnRegister = findViewById(R.id.btnRegister);
    }
}