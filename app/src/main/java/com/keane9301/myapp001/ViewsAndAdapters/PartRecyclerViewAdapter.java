package com.keane9301.myapp001.ViewsAndAdapters;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keane9301.myapp001.Database.Parts.PartEntity;
import com.keane9301.myapp001.R;
import com.keane9301.myapp001.ui.gallery.AddEditPart;
import com.keane9301.myapp001.ui.home.AddEditLube;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.util.List;

public class PartRecyclerViewAdapter extends RecyclerView.Adapter<PartRecyclerViewAdapter.ViewHolder> {
    private final List<PartEntity> partLists;
    private final OnPartClickListener onPartClickListener;

    public PartRecyclerViewAdapter(List<PartEntity> partLists, OnPartClickListener onPartClickListener) {
        // Getting all parts available in database
        this.partLists = partLists;
        this.onPartClickListener = onPartClickListener;
    }





    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        // Setting up view for individual part found in database
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.part_layout, parent, false);
        return new ViewHolder(view);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull PartRecyclerViewAdapter.ViewHolder holder, int position) {
        // Get position of part to fill up
        PartEntity partEntity = partLists.get(position);
        if(!partEntity.getCategory().equals("N/A") && !partEntity.getSubCategory().equals("N/A") &&
                !partEntity.getApplication().equals("N/A")) {
            holder.categoryApplication.setText(partEntity.getCategory() + ' ' + partEntity.getSubCategory() + ' ' +
                    partEntity.getApplication());
        } else if(!partEntity.getCategory().equals("N/A") && partEntity.getSubCategory().equals("N/A") &&
                !partEntity.getApplication().equals("N/A")) {
            holder.categoryApplication.setText(partEntity.getCategory() + ' ' + partEntity.getApplication());
        }

        if(partEntity.getImage() != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(partEntity.getImage());
            Bitmap photo = BitmapFactory.decodeStream(bis);
            holder.image.setImageBitmap(photo);
//        } else {
//            holder.image.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return partLists.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView categoryApplication;
        private ImageView image;

        // Setting up data view to be display to user
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            categoryApplication = itemView.findViewById(R.id.cat_apply_textview);
            image = itemView.findViewById(R.id.part_image_preview);

            // If click doesn't work, check if view has onClickListener set
            itemView.setOnClickListener(this::onClick);
        }


        @Override
        public void onClick(View v) {
            int id = v.getId();
            Log.d(AddEditPart.TAG, "onClick: " + v.getId() + ' ' + R.id.part_layout);
            if(id == R.id.part_layout) {
                PartEntity currentPart = partLists.get(getAdapterPosition());
                onPartClickListener.OnPartClick(getAdapterPosition(), currentPart);
            }
        }
    }
}
