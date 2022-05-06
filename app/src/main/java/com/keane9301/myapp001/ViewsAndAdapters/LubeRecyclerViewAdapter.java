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

import com.keane9301.myapp001.Database.Lubes.LubeEntity;
import com.keane9301.myapp001.R;
import com.keane9301.myapp001.ui.home.AddEditLube;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.util.List;

public class LubeRecyclerViewAdapter extends RecyclerView.Adapter<LubeRecyclerViewAdapter.ViewHolder> {
    private final List<LubeEntity> lubeLists;
    private final OnLubeClickListener onLubeClickListener;


    public LubeRecyclerViewAdapter(List<LubeEntity> lubeLists, OnLubeClickListener onLubeClickListener) {
        // Getting a list of lube from database
        this.lubeLists = lubeLists;
        this.onLubeClickListener = onLubeClickListener;
    }

    // Set up individual view layout for lube
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lube_layout, parent, false);
        return new ViewHolder(view);
    }

    // Set what to display when lube is found
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull LubeRecyclerViewAdapter.ViewHolder holder, int position) {
        // Detecting which lube has been clicked
        LubeEntity lube = lubeLists.get(position);
        if(!lube.getBrand().equals("N/A") && !lube.getModel().equals("N/A")) {
            holder.firstRow.setText(lube.getBrand() + " " + lube.getModel());
        } else if(lube.getBrand().equals("N/A") && !lube.getModel().equals("N/A")) {
            holder.firstRow.setText(lube.getModel());
        } else if(!lube.getBrand().equals("N/A") && lube.getModel().equals("N/A")) {
            holder.firstRow.setText(lube.getBrand());
        }

        if(!lube.getGrade().equals("N/A") && !lube.getViscosity().equals("N/A")) {
            holder.secondRow.setText(lube.getGrade() + " " + lube.getViscosity());
        } else if(lube.getGrade().equals("N/A") && !lube.getViscosity().equals("N/A")) {
            holder.secondRow.setText(lube.getViscosity());
        } else if(!lube.getGrade().equals("N/A") && lube.getViscosity().equals("N/A")) {
            holder.secondRow.setText(lube.getGrade());
        }

        // Image will depend on if the database actually contain any photo
        if(lube.getImage() != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(lube.getImage());
            Bitmap photo = BitmapFactory.decodeStream(bis);
            holder.image.setImageBitmap(photo);
//        } else {
//            holder.image.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return lubeLists.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView firstRow;
        private TextView secondRow;
        private ImageView image;

        // Setting the layout data to be display when in use
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            firstRow = itemView.findViewById(R.id.brand_model_textview);
            secondRow = itemView.findViewById(R.id.grade_visco_textview);
            image = itemView.findViewById(R.id.lube_image_preview);

            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();
            Log.d(AddEditLube.TAG, "onClick: " + v.getId() + ' ' + R.id.lube_layout + " " + R.id.part_layout);
            if(id == R.id.lube_layout) {
                LubeEntity currentLube = lubeLists.get(getAdapterPosition());
                Log.d(AddEditLube.TAG, "onClick: " + currentLube.getModel() + ' ' + currentLube.getBrand());
                onLubeClickListener.OnLubeClick(getAdapterPosition(), currentLube);
            }
        }
    }
}
