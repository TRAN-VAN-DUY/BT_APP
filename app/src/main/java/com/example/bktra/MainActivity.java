package com.example.bktra;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bktra.adapter.PhongTroAdapter;
import com.example.bktra.controller.PhongTroController;
import com.example.bktra.model.PhongTro;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PhongTroAdapter adapter;
    private PhongTroController controller;
    private TextView tvEmpty;
    private EditText etTimTenPhong, etGiaTu, etGiaDen;
    private SwitchMaterial switchConTrong;
    private final List<PhongTro> danhSachHienThi = new ArrayList<>();
    private ActivityResultLauncher<Intent> themSuaLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        controller = PhongTroController.getInstance();
        tvEmpty = findViewById(R.id.tvEmpty);
        etTimTenPhong = findViewById(R.id.etTimTenPhong);
        etGiaTu = findViewById(R.id.etGiaTu);
        etGiaDen = findViewById(R.id.etGiaDen);
        switchConTrong = findViewById(R.id.switchConTrong);

        Button btnApDungLoc = findViewById(R.id.btnApDungLoc);
        Button btnXoaLoc = findViewById(R.id.btnXoaLoc);

        recyclerView = findViewById(R.id.recyclerViewPhong);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        danhSachHienThi.addAll(controller.getDanhSachPhong());

        adapter = new PhongTroAdapter(danhSachHienThi,
                new PhongTroAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(PhongTro phong) {
                        moManHinhSua(phong);
                    }

                    @Override
                    public void onDeleteClick(PhongTro phong) {
                        xacNhanXoa(phong);
                    }
                });
        recyclerView.setAdapter(adapter);

        btnApDungLoc.setOnClickListener(v -> apDungBoLoc());
        btnXoaLoc.setOnClickListener(v -> {
            etTimTenPhong.setText("");
            etGiaTu.setText("");
            etGiaDen.setText("");
            switchConTrong.setChecked(false);
            apDungBoLoc();
        });

        FloatingActionButton fab = findViewById(R.id.fabThemPhong);
        fab.setOnClickListener(v -> moManHinhThem());

        // Đăng ký launcher nhận kết quả từ ThemSuaPhongActivity
        themSuaLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() != RESULT_OK || result.getData() == null)
                        return;

                    Intent data = result.getData();
                    PhongTro phong = layPhongTuIntent(data);
                    if (phong == null)
                        return;

                    int position = data.getIntExtra(ThemSuaPhongActivity.EXTRA_POSITION, -1);
                    if (position == -1) {
                        controller.themPhong(phong);
                    } else {
                        controller.suaPhong(position, phong);
                    }
                    apDungBoLoc();
                });

        apDungBoLoc();
    }

    @SuppressWarnings("deprecation")
    private PhongTro layPhongTuIntent(Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return data.getSerializableExtra(ThemSuaPhongActivity.EXTRA_PHONG, PhongTro.class);
        } else {
            return (PhongTro) data.getSerializableExtra(ThemSuaPhongActivity.EXTRA_PHONG);
        }
    }

    private void moManHinhThem() {
        Intent intent = new Intent(this, ThemSuaPhongActivity.class);
        themSuaLauncher.launch(intent);
    }

    private void moManHinhSua(PhongTro phongCanSua) {
        int position = timViTriTrongDanhSachGoc(phongCanSua);
        if (position == -1) {
            return;
        }
        Intent intent = new Intent(this, ThemSuaPhongActivity.class);
        intent.putExtra(ThemSuaPhongActivity.EXTRA_PHONG, controller.getPhong(position));
        intent.putExtra(ThemSuaPhongActivity.EXTRA_POSITION, position);
        themSuaLauncher.launch(intent);
    }

    private void xacNhanXoa(PhongTro phongCanXoa) {
        int position = timViTriTrongDanhSachGoc(phongCanXoa);
        if (position == -1) {
            return;
        }
        PhongTro phong = controller.getPhong(position);
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa \"" + phong.getTenPhong() + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    controller.xoaPhong(position);
                    apDungBoLoc();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private int timViTriTrongDanhSachGoc(PhongTro phong) {
        return controller.getDanhSachPhong().indexOf(phong);
    }

    private void apDungBoLoc() {
        String keyword = etTimTenPhong.getText() != null
                ? etTimTenPhong.getText().toString().trim().toLowerCase()
                : "";

        Double giaTu = parseGia(etGiaTu, true);
        if (giaTu == null && !TextUtils.isEmpty(etGiaTu.getText())) {
            return;
        }

        Double giaDen = parseGia(etGiaDen, false);
        if (giaDen == null && !TextUtils.isEmpty(etGiaDen.getText())) {
            return;
        }

        if (giaTu != null && giaDen != null && giaTu > giaDen) {
            etGiaDen.setError("Giá đến phải lớn hơn hoặc bằng giá từ");
            etGiaDen.requestFocus();
            return;
        }
        etGiaDen.setError(null);

        boolean chiPhongTrong = switchConTrong.isChecked();

        danhSachHienThi.clear();
        for (PhongTro phong : controller.getDanhSachPhong()) {
            if (!TextUtils.isEmpty(keyword)
                    && (phong.getTenPhong() == null
                            || !phong.getTenPhong().toLowerCase().contains(keyword))) {
                continue;
            }
            if (chiPhongTrong && phong.isDaThue()) {
                continue;
            }
            if (giaTu != null && phong.getGiaThue() < giaTu) {
                continue;
            }
            if (giaDen != null && phong.getGiaThue() > giaDen) {
                continue;
            }
            danhSachHienThi.add(phong);
        }

        adapter.notifyDataSetChanged();
        capNhatHienThi();
    }

    private Double parseGia(EditText editText, boolean isGiaTu) {
        String value = editText.getText() != null ? editText.getText().toString().trim() : "";
        if (TextUtils.isEmpty(value)) {
            editText.setError(null);
            return null;
        }

        try {
            double gia = Double.parseDouble(value);
            if (gia < 0) {
                editText.setError(isGiaTu ? "Giá từ không được âm" : "Giá đến không được âm");
                editText.requestFocus();
                return null;
            }
            editText.setError(null);
            return gia;
        } catch (NumberFormatException e) {
            editText.setError(isGiaTu ? "Giá từ không hợp lệ" : "Giá đến không hợp lệ");
            editText.requestFocus();
            return null;
        }
    }

    private void capNhatHienThi() {
        if (danhSachHienThi.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}