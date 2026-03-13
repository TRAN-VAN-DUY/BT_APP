package com.example.bktra.model;

import java.io.Serializable;

public class PhongTro implements Serializable {
    private String maPhong;
    private String tenPhong;
    private double giaThue;
    private boolean daThue;
    private String tenNguoiThue;
    private String soDienThoai;

    public PhongTro() {
    }

    public PhongTro(String maPhong, String tenPhong, double giaThue, boolean daThue,
            String tenNguoiThue, String soDienThoai) {
        this.maPhong = maPhong;
        this.tenPhong = tenPhong;
        this.giaThue = giaThue;
        this.daThue = daThue;
        this.tenNguoiThue = tenNguoiThue;
        this.soDienThoai = soDienThoai;
    }

    public String getMaPhong() {
        return maPhong;
    }

    public void setMaPhong(String maPhong) {
        this.maPhong = maPhong;
    }

    public String getTenPhong() {
        return tenPhong;
    }

    public void setTenPhong(String tenPhong) {
        this.tenPhong = tenPhong;
    }

    public double getGiaThue() {
        return giaThue;
    }

    public void setGiaThue(double giaThue) {
        this.giaThue = giaThue;
    }

    public boolean isDaThue() {
        return daThue;
    }

    public void setDaThue(boolean daThue) {
        this.daThue = daThue;
    }

    public String getTenNguoiThue() {
        return tenNguoiThue;
    }

    public void setTenNguoiThue(String tenNguoiThue) {
        this.tenNguoiThue = tenNguoiThue;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }
}
