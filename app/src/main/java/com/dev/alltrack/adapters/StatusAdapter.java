package com.dev.alltrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dev.alltrack.R;
import com.dev.alltrack.models.Status;

import java.util.List;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Status> mStalist;

    public StatusAdapter(Context context, List<Status> staList) {
        this.mContext = context;
        this.mStalist = staList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.status_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Status status = mStalist.get(position);

        holder.detail.setText(status.getDetail());
        holder.province.setText(status.getProvince());
        holder.date.setText(status.getDate());
    }

    @Override
    public int getItemCount() {
        return mStalist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView detail, province, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            detail = itemView.findViewById(R.id.detail_text_view);
            province = itemView.findViewById(R.id.province_text_view);
            date = itemView.findViewById(R.id.date_text_view);
        }
    }
}
