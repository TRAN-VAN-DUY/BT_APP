package com.example.bt_app.Model;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bt_app.R;
import com.example.bt_app.Model.PhongTro;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class PhongTroAdapter extends RecyclerView.Adapter<com.example.bt_app.PhongTroAdapter.PhongTroViewHolder> {

    private final List<PhongTro> danhSachPhong;
    private final com.example.bt_app.PhongTroAdapter.OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PhongTro phong);

        void onDeleteClick(PhongTro phong);
    }

    public PhongTroAdapter(List<PhongTro> danhSachPhong, com.example.bt_app.PhongTroAdapter.OnItemClickListener listener) {
        this.danhSachPhong = danhSachPhong;
        this.listener = listener;
    }

    @NonNull
    @Override
    public com.example.bt_app.PhongTroAdapter.PhongTroViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_phong_tro, parent, false);
        return new com.example.bt_app.PhongTroAdapter.PhongTroViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull com.example.bt_app.PhongTroAdapter.PhongTroViewHolder holder, int position) {
        PhongTro phong = danhSachPhong.get(position);

        holder.tvTenPhong.setText(phong.getTenPhong());
        holder.tvMaPhong.setText("#" + phong.getMaPhong());

        // Format giá thuê kiểu Việt Nam: dấu chấm ngăn cách hàng nghìn
        DecimalFormat df = new DecimalFormat("#,###");
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        df.setDecimalFormatSymbols(symbols);
        holder.tvGiaThue.setText(df.format((long) phong.getGiaThue()) + " VNĐ/tháng");

        if (phong.isDaThue()) {
            int red = Color.parseColor("#F44336");
            holder.viewTinhTrang.setBackgroundColor(red);
            holder.tvTinhTrang.setText("Đã thuê");
            holder.tvTinhTrang.setTextColor(red);

            String tenNguoiThue = phong.getTenNguoiThue();
            if (tenNguoiThue != null && !tenNguoiThue.isEmpty()) {
                holder.tvNguoiThue.setVisibility(View.VISIBLE);
                holder.tvNguoiThue.setText("Người thuê: " + tenNguoiThue);
            } else {
                holder.tvNguoiThue.setVisibility(View.GONE);
            }
        } else {
            int green = Color.parseColor("#4CAF50");
            holder.viewTinhTrang.setBackgroundColor(green);
            holder.tvTinhTrang.setText("Còn trống");
            holder.tvTinhTrang.setTextColor(green);
            holder.tvNguoiThue.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (listener != null && adapterPosition != RecyclerView.NO_POSITION) {
                listener.onItemClick(danhSachPhong.get(adapterPosition));
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (listener != null && adapterPosition != RecyclerView.NO_POSITION) {
                listener.onDeleteClick(danhSachPhong.get(adapterPosition));
            }
            return true;
        });

        holder.btnXoa.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (listener != null && adapterPosition != RecyclerView.NO_POSITION) {
                listener.onDeleteClick(danhSachPhong.get(adapterPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return danhSachPhong.size();
    }

    static class PhongTroViewHolder extends RecyclerView.ViewHolder {
        View viewTinhTrang;
        TextView tvTenPhong, tvMaPhong, tvGiaThue, tvTinhTrang, tvNguoiThue;
        ImageButton btnXoa;

        public PhongTroViewHolder(@NonNull View itemView) {
            super(itemView);
            viewTinhTrang = itemView.findViewById(R.id.viewTinhTrang);
            tvTenPhong = itemView.findViewById(R.id.tvTenPhong);
            tvMaPhong = itemView.findViewById(R.id.tvMaPhong);
            tvGiaThue = itemView.findViewById(R.id.tvGiaThue);
            tvTinhTrang = itemView.findViewById(R.id.tvTinhTrang);
            tvNguoiThue = itemView.findViewById(R.id.tvNguoiThue);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }
}
