package com.keane9301.myapp001.ui.slideshow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.keane9301.myapp001.R;

public class QuotationList extends AppCompatActivity {
    private RecyclerView customerQuoteRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation_list);

        customerQuoteRecyclerView = findViewById(R.id.cusQuoteRecyclerView);


    }
}