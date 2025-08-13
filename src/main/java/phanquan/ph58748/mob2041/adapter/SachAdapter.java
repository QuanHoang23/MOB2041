package phanquan.ph58748.mob2041.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import phanquan.ph58748.mob2041.R;
import phanquan.ph58748.mob2041.dao.SachDAO;
import phanquan.ph58748.mob2041.models.Sach;

public class SachAdapter extends RecyclerView.Adapter<SachAdapter.ViewHolder> {

    private ArrayList<Sach> list;
    private Context context;
    private SachDAO sachDAO;
    private SachAdapterListener listener;

    // Interface để callback về Activity
    public interface SachAdapterListener {
        void onEditSach(Sach sach);
        void onDeleteSach(Sach sach);
    }

    public SachAdapter(ArrayList<Sach> list, Context context, SachDAO sachDAO, SachAdapterListener listener) {
        this.list = list;
        this.context = context;
        this.sachDAO = sachDAO;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View view = inflater.inflate(R.layout.item_book,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Sach sach = list.get(position);
        
        holder.txtMaSach.setText("ID: " + sach.getMasach());
        holder.txtTenSach.setText(sach.getTensach());
        holder.txtGia.setText(String.valueOf(sach.getGiaban()) + " VNĐ");
        holder.txtTacGia.setText(sach.getTacgia());
        holder.txtTenLoai.setText(sach.getTenloai());

        // Xử lý sự kiện click nút sửa
        holder.ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onEditSach(sach);
                }
            }
        });

        // Xử lý sự kiện click nút xóa
        holder.ivDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onDeleteSach(sach);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtMaSach, txtTenSach, txtTacGia, txtGia, txtTenLoai;
        ImageView ivEdit, ivDel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtMaSach = itemView.findViewById(R.id.txtMaSach);
            txtTenSach = itemView.findViewById(R.id.txtTenSach);
            txtTacGia = itemView.findViewById(R.id.txtTacGia);
            txtGia = itemView.findViewById(R.id.txtGia);
            txtTenLoai = itemView.findViewById(R.id.txtTenLoai);
            ivEdit = itemView.findViewById(R.id.ivEdit);
            ivDel = itemView.findViewById(R.id.ivDel);
        }
    }
}
