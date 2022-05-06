package com.keane9301.myapp001.ViewsAndAdapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.keane9301.myapp001.Database.Customers.CustomerEntity;
import com.keane9301.myapp001.R;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.util.List;

public class CustomerRecyclerViewAdapter extends RecyclerView.Adapter<CustomerRecyclerViewAdapter.ViewHolder> {
    private final List<CustomerEntity> customerLists;
    private final OnCustomerClickListener onCustomerClickListener;



    public CustomerRecyclerViewAdapter(List<CustomerEntity> customerLists, OnCustomerClickListener onCustomerClickListener) {
        this.customerLists = customerLists;
        this.onCustomerClickListener = onCustomerClickListener;
    }



    // Set up individual view layout for each item added
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.part_layout, parent, false);
//        return null;
        return new ViewHolder(view);
    }


    // Fill up the view with data and image (if any)
    @Override
    public void onBindViewHolder(@NonNull @NotNull CustomerRecyclerViewAdapter.ViewHolder holder, int position) {
        CustomerEntity customerEntity = customerLists.get(position);
        if((customerEntity.getName().isEmpty() || customerEntity.getName().equals("N/A")) &&
                (!customerEntity.getShopName().isEmpty() || !customerEntity.getShopName().equals("N/A"))) {
            holder.customerNameTextView.setText(customerEntity.getShopName());
        } else if((customerEntity.getShopName().isEmpty() || customerEntity.getShopName().equals("N/A")) &&
                (!customerEntity.getName().isEmpty() || !customerEntity.getName().equals("N/A"))) {
            holder.customerNameTextView.setText(customerEntity.getName());
        } else if(!customerEntity.getName().equals("N/A") && !customerEntity.getShopName().equals("N/A")) {
            holder.customerNameTextView.setText(customerEntity.getShopName() + " (" + customerEntity.getName() + ")");
        }

        if(customerEntity.getImage() != null) {
            ByteArrayInputStream bis = new ByteArrayInputStream(customerEntity.getImage());
            Bitmap photo = BitmapFactory.decodeStream(bis);
            holder.customerImageView.setImageBitmap(photo);
        }
    }



    @Override
    public int getItemCount() {
        return customerLists.size();
    }




    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView customerNameTextView;
        private ImageView customerImageView;

        // Set up view to their appropriate id's
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.cat_apply_textview);
            customerImageView = itemView.findViewById(R.id.part_image_preview);

            itemView.setOnClickListener(this::onClick);
        }



        @Override
        public void onClick(View v) {
            int id = v.getId();
            if(id == R.id.part_layout) {
                CustomerEntity customerEntity = customerLists.get(getAdapterPosition());
                onCustomerClickListener.OnCustomerClick(getAdapterPosition(), customerEntity);
            }
        }
    }
}
