package com.example.epicentrum_app.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.epicentrum_app.R;
import com.example.epicentrum_app.model.AdminModel;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MyAdminAdapter extends RecyclerView.Adapter<MyAdminAdapter.MyAdminViewHolder> {

    private Context context;
    private List<AdminModel> adminModelList;

    public MyAdminAdapter(Context context, List<AdminModel> adminModelList) {
        this.context = context;
        this.adminModelList = adminModelList;
    }

    @NonNull
    @Override
    public MyAdminViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyAdminViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_admin_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdminViewHolder holder, int position) {
        holder.txtName.setText(new StringBuilder().append(adminModelList.get(position).getNama()));
        holder.txtTable.setText(new StringBuilder().append(adminModelList.get(position).getTable()));
        holder.txtState.setText(new StringBuilder().append(adminModelList.get(position).getState()));
    }

    @Override
    public int getItemCount() {
        return adminModelList.size();
    }

    public class MyAdminViewHolder extends RecyclerView.ViewHolder {

        @Nullable
        @BindView(R.id.txtName)
        TextView txtName;
        @BindView(R.id.txtTable)
        TextView txtTable;
        @BindView(R.id.state)
        TextView txtState;
        Unbinder unbinder;
        public MyAdminViewHolder(@NonNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
