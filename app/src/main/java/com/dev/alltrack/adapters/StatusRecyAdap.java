package com.dev.alltrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.dev.alltrack.R;
import com.dev.alltrack.models.Status;

import java.util.List;

public class StatusRecyAdap extends RecyclerView.Adapter<StatusRecyAdap.ViewHolder> {

    private final Context mContext;
    private final List<Status> mStalist;

    public StatusRecyAdap(Context context, List<Status> staList) {
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

        holder.lineTop.setBackgroundColor(mContext.getColor(R.color.lineColor));
        holder.lineBottom.setBackgroundColor(mContext.getColor(R.color.lineColor));

        if(position == 0)
            holder.lineTop.setBackground(null);
        if(position == getItemCount() - 1)
            holder.lineBottom.setBackground(null);

        String code = status.getCode();
        switch (code) {
            case "401": holder.stateImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_error_outline_yellow_24dp));
                break;
            case "501": holder.stateImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_check_circle_green_24dp));
                break;
            default: holder.stateImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_circle_outline_black_24dp));
        }

        holder.detail.setText(status.getDetail());
        holder.province.setText(status.getProvince());
        holder.dateTime.setText(status.getDate().substring(0, 16).replace("-", "/").replace("T", " "));
    }

    @Override
    public int getItemCount() {
        return mStalist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView stateImage;
        private TextView detail, province, dateTime;
        private View lineTop, lineBottom;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stateImage = itemView.findViewById(R.id.state_image_view);
            detail = itemView.findViewById(R.id.detail_text_view);
            province = itemView.findViewById(R.id.province_text_view);
            dateTime = itemView.findViewById(R.id.datetime_text_view);
            lineTop = itemView.findViewById(R.id.line_state_vertical_top_view);
            lineBottom = itemView.findViewById(R.id.line_state_vertical_bottom_view);
        }
    }
}
