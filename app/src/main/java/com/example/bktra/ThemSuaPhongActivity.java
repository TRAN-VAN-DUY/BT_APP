package com.example.bktra;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bktra.model.PhongTro;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class ThemSuaPhongActivity extends AppCompatActivity {

    public static final String EXTRA_PHONG = "extra_phong";
    public static final String EXTRA_POSITION = "extra_position";

    private TextInputLayout tilMaPhong, tilTenPhong, tilGiaThue, tilTenNguoiThue;
    private TextInputEditText etMaPhong, etTenPhong, etGiaThue, etTenNguoiThue, etSoDienThoai;
    private RadioGroup rgTinhTrang;
    private RadioButton rbDaThue;

    private PhongTro phongEdit = null;
    private int editPosition = -1;

    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_them_sua_phong);
//
//        MaterialToolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }
//
//        initViews();
//        loadIntent();
//
//        // Khi đổi trạng thái → bật/tắt trường người thuê
//        rgTinhTrang.setOnCheckedChangeListener((group, checkedId) -> {
//            boolean daThue = (checkedId == R.id.rbDaThue);
//            findViewById(R.id.tilTenNguoiThue).setEnabled(daThue);
//            findViewById(R.id.tilSoDienThoai).setEnabled(daThue);
//            tilTenNguoiThue.setError(null);
//            if (!daThue) {
//                etTenNguoiThue.setText("");
//                etSoDienThoai.setText("");
//            }
//        });
//
//        Button btnLuu = findViewById(R.id.btnLuu);
//        btnLuu.setOnClickListener(v -> luuPhong());
//    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sua_phong);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initViews();
        loadIntent();

        // Khi đổi trạng thái → bật/tắt trường người thuê
        rgTinhTrang.setOnCheckedChangeListener((group, checkedId) -> {
            boolean daThue = (checkedId == R.id.rbDaThue);
            findViewById(R.id.tilTenNguoiThue).setEnabled(daThue);
            findViewById(R.id.tilSoDienThoai).setEnabled(daThue);
            tilTenNguoiThue.setError(null);
            if (!daThue) {
                etTenNguoiThue.setText("");
                etSoDienThoai.setText("");
            }
        });

        Button btnLuu = findViewById(R.id.btnLuu);
        btnLuu.setOnClickListener(v -> luuPhong());
    }
    private void initViews() {
        tilMaPhong = findViewById(R.id.tilMaPhong);
        tilTenPhong = findViewById(R.id.tilTenPhong);
        tilGiaThue = findViewById(R.id.tilGiaThue);
        tilTenNguoiThue = findViewById(R.id.tilTenNguoiThue);

        etMaPhong = findViewById(R.id.etMaPhong);
        etTenPhong = findViewById(R.id.etTenPhong);
        etGiaThue = findViewById(R.id.etGiaThue);
        etTenNguoiThue = findViewById(R.id.etTenNguoiThue);
        etSoDienThoai = findViewById(R.id.etSoDienThoai);

        rgTinhTrang = findViewById(R.id.rgTinhTrang);
        rbDaThue = findViewById(R.id.rbDaThue);
    }

    @SuppressWarnings("deprecation")
    private void loadIntent() {
        Intent intent = getIntent();
        if (!intent.hasExtra(EXTRA_PHONG)) {
            if (getSupportActionBar() != null)
                getSupportActionBar().setTitle("Thêm phòng trọ");
            return;
        }

        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle("Sửa phòng trọ");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            phongEdit = intent.getSerializableExtra(EXTRA_PHONG, PhongTro.class);
        } else {
            phongEdit = (PhongTro) intent.getSerializableExtra(EXTRA_PHONG);
        }
        editPosition = intent.getIntExtra(EXTRA_POSITION, -1);

        if (phongEdit == null)
            return;

        etMaPhong.setText(phongEdit.getMaPhong());
        etTenPhong.setText(phongEdit.getTenPhong());
        etGiaThue.setText(String.valueOf((long) phongEdit.getGiaThue()));

        if (phongEdit.isDaThue()) {
            rbDaThue.setChecked(true);
            // Bật các trường người thuê (listener chưa chạy nên set tay)
            findViewById(R.id.tilTenNguoiThue).setEnabled(true);
            findViewById(R.id.tilSoDienThoai).setEnabled(true);
            etTenNguoiThue.setText(phongEdit.getTenNguoiThue());
            etSoDienThoai.setText(phongEdit.getSoDienThoai());
        }
        // rbConTrong mặc định checked → tilTenNguoiThue đã disabled từ XML
    }

    private void luuPhong() {
        // Đọc giá trị
        String maPhong = etMaPhong.getText() != null ? etMaPhong.getText().toString().trim() : "";
        String tenPhong = etTenPhong.getText() != null ? etTenPhong.getText().toString().trim() : "";
        String giaThueStr = etGiaThue.getText() != null ? etGiaThue.getText().toString().trim() : "";
        boolean daThue = rbDaThue.isChecked();
        String tenNguoiThue = etTenNguoiThue.getText() != null ? etTenNguoiThue.getText().toString().trim() : "";
        String soDienThoai = etSoDienThoai.getText() != null ? etSoDienThoai.getText().toString().trim() : "";

        // Validate
        if (TextUtils.isEmpty(maPhong)) {
            tilMaPhong.setError("Vui lòng nhập mã phòng");
            etMaPhong.requestFocus();
            return;
        }
        tilMaPhong.setError(null);

        if (TextUtils.isEmpty(tenPhong)) {
            tilTenPhong.setError("Vui lòng nhập tên phòng");
            etTenPhong.requestFocus();
            return;
        }
        tilTenPhong.setError(null);

        if (TextUtils.isEmpty(giaThueStr)) {
            tilGiaThue.setError("Vui lòng nhập giá thuê");
            etGiaThue.requestFocus();
            return;
        }

        double giaThue;
        try {
            giaThue = Double.parseDouble(giaThueStr);
            if (giaThue <= 0) {
                tilGiaThue.setError("Giá thuê phải lớn hơn 0");
                etGiaThue.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            tilGiaThue.setError("Giá thuê không hợp lệ");
            etGiaThue.requestFocus();
            return;
        }
        tilGiaThue.setError(null);

        if (daThue && TextUtils.isEmpty(tenNguoiThue)) {
            tilTenNguoiThue.setError("Vui lòng nhập tên người thuê");
            etTenNguoiThue.requestFocus();
            return;
        }
        tilTenNguoiThue.setError(null);

        // Tạo đối tượng và trả về
        PhongTro phong = new PhongTro(maPhong, tenPhong, giaThue, daThue, tenNguoiThue, soDienThoai);

        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_PHONG, phong);
        if (editPosition != -1) {
            resultIntent.putExtra(EXTRA_POSITION, editPosition);
            Toast.makeText(this, "Đã cập nhật: " + tenPhong, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Đã thêm: " + tenPhong, Toast.LENGTH_SHORT).show();
        }
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
