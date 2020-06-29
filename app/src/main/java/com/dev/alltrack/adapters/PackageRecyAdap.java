package com.dev.alltrack.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.dev.alltrack.R;
import com.dev.alltrack.models.Package;

import java.util.List;

public class PackageRecyAdap extends RecyclerView.Adapter<PackageRecyAdap.ViewHolder> {

    private final Context mContext;
    private final List<Package> mPackage;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String code);
    }

    public PackageRecyAdap(Context mContext, List<Package> mPackage) {
        this.mContext = mContext;
        this.mPackage = mPackage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.track_package_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Package aPackage = mPackage.get(position);

        String lastStatus = aPackage.getStatus();
        if(lastStatus.equals("501")) {
            holder.statusImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_check_white_24dp));
            holder.statusImageLayout.setBackground(mContext.getDrawable(R.drawable.success_status_background));
        } else {
            holder.statusImage.setImageDrawable(mContext.getDrawable(R.drawable.ic_delivery_line_white));
        }

        holder.codeText.setText(aPackage.getCode());
        holder.compText.setText(aPackage.getCompany());
        holder.descText.setText(aPackage.getDescription());

        holder.bind(aPackage.getCode(), this.listener);
    }

    @Override
    public int getItemCount() {
        return mPackage.size();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView codeText, compText, descText;
        private final ImageView statusImage, menuImage;
        private final RelativeLayout statusImageLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            codeText = itemView.findViewById(R.id.no_text_view);
            compText = itemView.findViewById(R.id.company_text_view);
            descText = itemView.findViewById(R.id.description_text_view);
            statusImage = itemView.findViewById(R.id.status_image_view);
            statusImageLayout = itemView.findViewById(R.id.status_image_layout);
            menuImage = itemView.findViewById(R.id.menu_image_view);
            menuImage.setOnClickListener(this);
        }

        public void bind(final String code, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(code);
                }
            });
        }

        @Override
        public void onClick(View v) {
            showPopupMenu(v);
        }

        private void showPopupMenu(View v) {
            PopupMenu popupMenu = new PopupMenu(mContext, v);
            popupMenu.inflate(R.menu.popup_menu);
            popupMenu.show();

            Log.e("PopuMenu", "" + getAdapterPosition());
        }
    }

}
