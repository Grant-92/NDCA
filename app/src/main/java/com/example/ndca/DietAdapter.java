package com.example.ndca;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DietAdapter extends RecyclerView.Adapter<DietAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<DietModel> mList;


    DietAdapter(Context context, ArrayList<DietModel> list) {
        mContext = context;
        mList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.suggest_list_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        DietModel item = mList.get(position);

        ImageView image = holder.item_image;
        TextView item_header, item_descript;

        item_header = holder.item_header;
        item_descript = holder.item_descript;

        image.setImageResource(item.getImage());
        item_header.setText(item.getHeader());
        item_descript.setText(item.getDescript());


    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_header, item_descript;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.list_item_image);
            item_header = itemView.findViewById(R.id.list_item_header);
            item_descript = itemView.findViewById(R.id.list_item_descript);

        }
    }
}
