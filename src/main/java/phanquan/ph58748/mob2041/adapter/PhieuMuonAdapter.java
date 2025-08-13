package phanquan.ph58748.mob2041.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import phanquan.ph58748.mob2041.R;
import phanquan.ph58748.mob2041.models.ChiTietPhieuMuon;
import phanquan.ph58748.mob2041.models.PhieuMuon;

public class PhieuMuonAdapter extends RecyclerView.Adapter<PhieuMuonAdapter.ViewHolder> {

    private ArrayList<PhieuMuon> list;
    private Context context;
    private PhieuMuonAdapterListener listener;

    // Interface để callback về Activity
    public interface PhieuMuonAdapterListener {
        void onEditPhieuMuon(PhieuMuon phieuMuon);
        void onDeletePhieuMuon(PhieuMuon phieuMuon);
        void onViewChiTiet(PhieuMuon phieuMuon);
    }

    public PhieuMuonAdapter(ArrayList<PhieuMuon> list, Context context, PhieuMuonAdapterListener listener) {
        this.list = list;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_phieu_muon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhieuMuon phieuMuon = list.get(position);
        
        // Hiển thị thông tin cơ bản
        holder.txtMaPhieuMuon.setText("PM" + String.format("%03d", phieuMuon.getMapm()));
        holder.txtNgayMuon.setText("Ngày mượn: " + phieuMuon.getNgaymuon());
        holder.txtNgayTra.setText("Ngày trả: " + phieuMuon.getNgaytra());
        holder.txtTenNguoiMuon.setText(phieuMuon.getTenNguoiMuon());
        holder.txtSDTNguoiMuon.setText(phieuMuon.getSdtNguoiMuon());
        
        // Hiển thị tổng số sách và tổng tiền
        holder.txtTongSoSach.setText(phieuMuon.getTongSoSach() + " cuốn");
        holder.txtTongTien.setText(String.format("%,.0f VNĐ", phieuMuon.getTongTien()));
        
        // Hiển thị danh sách sách
        hienThiDanhSachSach(holder.linearDanhSachSach, phieuMuon.getDanhSachSach());

        // Xử lý sự kiện click nút sửa
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditPhieuMuon(phieuMuon);
                }
            }
        });

        // Xử lý sự kiện click nút xóa
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeletePhieuMuon(phieuMuon);
                }
            }
        });

        // Xử lý sự kiện click vào item để xem chi tiết
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onViewChiTiet(phieuMuon);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Hiển thị danh sách sách trong phiếu mượn
    private void hienThiDanhSachSach(LinearLayout container, List<ChiTietPhieuMuon> danhSachSach) {
        container.removeAllViews();
        
        if (danhSachSach == null || danhSachSach.isEmpty()) {
            TextView tvNoData = new TextView(context);
            tvNoData.setText("Không có sách nào");
            tvNoData.setTextColor(context.getResources().getColor(R.color.text_hint));
            tvNoData.setTextSize(14);
            container.addView(tvNoData);
            return;
        }

        for (ChiTietPhieuMuon ctpm : danhSachSach) {
            View sachView = taoViewSach(ctpm);
            container.addView(sachView);
        }
    }

    // Tạo view cho từng sách
    private View taoViewSach(ChiTietPhieuMuon ctpm) {
        LinearLayout sachLayout = new LinearLayout(context);
        sachLayout.setOrientation(LinearLayout.HORIZONTAL);
        sachLayout.setPadding(0, 4, 0, 4);
        sachLayout.setGravity(android.view.Gravity.CENTER_VERTICAL);

        // Icon sách
        ImageView ivSach = new ImageView(context);
        ivSach.setImageResource(R.mipmap.book);
        ivSach.setLayoutParams(new LinearLayout.LayoutParams(20, 20));
        ivSach.setPadding(0, 0, 8, 0);
        sachLayout.addView(ivSach);

        // Thông tin sách
        LinearLayout infoLayout = new LinearLayout(context);
        infoLayout.setOrientation(LinearLayout.VERTICAL);
        infoLayout.setLayoutParams(new LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, 
            LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        TextView tvTenSach = new TextView(context);
        tvTenSach.setText(ctpm.getTensach());
        tvTenSach.setTextColor(context.getResources().getColor(R.color.text_primary));
        tvTenSach.setTextSize(14);
        tvTenSach.setTypeface(null, android.graphics.Typeface.BOLD);
        infoLayout.addView(tvTenSach);

        TextView tvChiTiet = new TextView(context);
        tvChiTiet.setText(String.format("%s - %s - SL: %d - %,.0f VNĐ", 
            ctpm.getTacgia(), 
            ctpm.getTenloai(), 
            ctpm.getSoluong(), 
            ctpm.getThanhTien()));
        tvChiTiet.setTextColor(context.getResources().getColor(R.color.text_secondary));
        tvChiTiet.setTextSize(12);
        infoLayout.addView(tvChiTiet);

        sachLayout.addView(infoLayout);
        return sachLayout;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaPhieuMuon, txtNgayMuon, txtNgayTra, txtTenNguoiMuon, txtSDTNguoiMuon;
        TextView txtTongSoSach, txtTongTien;
        LinearLayout linearDanhSachSach;
        ImageView ivEdit, ivDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMaPhieuMuon = itemView.findViewById(R.id.txtMaPhieuMuon);
            txtNgayMuon = itemView.findViewById(R.id.txtNgayMuon);
            txtNgayTra = itemView.findViewById(R.id.txtNgayTra);
            txtTenNguoiMuon = itemView.findViewById(R.id.txtTenNguoiMuon);
            txtSDTNguoiMuon = itemView.findViewById(R.id.txtSDTNguoiMuon);
            txtTongSoSach = itemView.findViewById(R.id.txtTongSoSach);
            txtTongTien = itemView.findViewById(R.id.txtTongTien);
            linearDanhSachSach = itemView.findViewById(R.id.linearDanhSachSach);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDel = itemView.findViewById(R.id.ivDel);
        }
    }
}
