package com.example.bktra.controller;

import com.example.bktra.model.PhongTro;

import java.util.ArrayList;
import java.util.List;

public class PhongTroController {

    private static PhongTroController instance;
    private final List<PhongTro> danhSachPhong;

    private PhongTroController() {
        danhSachPhong = new ArrayList<>();
        // Dữ liệu mẫu
        danhSachPhong.add(new PhongTro("P101", "Phòng 101", 2500000, false, "", ""));
        danhSachPhong.add(new PhongTro("P102", "Phòng 102", 3000000, true, "Nguyễn Văn A", "0912345678"));
        danhSachPhong.add(new PhongTro("P103", "Phòng 103", 2000000, false, "", ""));
        danhSachPhong.add(new PhongTro("P201", "Phòng 201", 3500000, true, "Trần Thị B", "0987654321"));
    }

    public static PhongTroController getInstance() {
        if (instance == null) {
            instance = new PhongTroController();
        }
        return instance;
    }

    public List<PhongTro> getDanhSachPhong() {
        return danhSachPhong;
    }

    public int getSoPhong() {
        return danhSachPhong.size();
    }

    public PhongTro getPhong(int viTri) {
        return danhSachPhong.get(viTri);
    }

    public void themPhong(PhongTro phong) {
        danhSachPhong.add(phong);
    }

    public void suaPhong(int viTri, PhongTro phong) {
        danhSachPhong.set(viTri, phong);
    }

    public void xoaPhong(int viTri) {
        danhSachPhong.remove(viTri);
    }
}
