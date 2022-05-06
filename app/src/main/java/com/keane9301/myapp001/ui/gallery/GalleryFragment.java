package com.keane9301.myapp001.ui.gallery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.keane9301.myapp001.Database.Parts.PartEntity;
import com.keane9301.myapp001.Database.Parts.PartViewModel;
import com.keane9301.myapp001.ViewsAndAdapters.OnPartClickListener;
import com.keane9301.myapp001.ViewsAndAdapters.PartRecyclerViewAdapter;
import com.keane9301.myapp001.databinding.FragmentGalleryBinding;
//import com.memetix.mst.translate.Translate;

import java.util.List;

public class GalleryFragment extends Fragment implements OnPartClickListener {

    private int ADD_REQUEST_CODE = 1;
    private GalleryViewModel galleryViewModel;
    private FragmentGalleryBinding binding;
    private FloatingActionButton addPart;

    private PartViewModel partViewModel;
    private PartRecyclerViewAdapter partRecyclerViewAdapter;
    private RecyclerView partLayout;
    private SearchView searchBar;


    private String application;
    private String category;
    private String subCategory;
    private String position;
    private String brand;
    private String condition;
    private String size;
    private String partNumber;
    private String partNumberAlt;
    private String partNumberOE;
    private String partWarranty;
    private String location;
    private String supplier;
    private String cost;
    private String profit;
    private String sell;
    private String note;
    private byte[] image;
    private String time;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        galleryViewModel =
//                new ViewModelProvider(this).get(GalleryViewModel.class);

        partViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getActivity().getApplication()).create(PartViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        searchBar = binding.partSearchView;

        partLayout = binding.partRecyclerView;
        partLayout.setHasFixedSize(true);
        partLayout.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        partViewModel.getAllParts().observe(this.getActivity(), new Observer<List<PartEntity>>() {
            @Override
            public void onChanged(List<PartEntity> partEntities) {
                partRecyclerViewAdapter = new PartRecyclerViewAdapter(partEntities, GalleryFragment.this::OnPartClick);
                partLayout.setAdapter(partRecyclerViewAdapter);
            }
        });



        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String[] querySplit = query.split(" ");
                if(querySplit.length > 0) {
                    for(int i = 0; i < querySplit.length; i++) {
                        partViewModel.searchPart(querySplit[i]).observe(GalleryFragment.this.getActivity(), new Observer<List<PartEntity>>() {
                            @Override
                            public void onChanged(List<PartEntity> partEntities) {
                                partRecyclerViewAdapter = new PartRecyclerViewAdapter(partEntities, GalleryFragment.this::OnPartClick);
                                partLayout.setAdapter(partRecyclerViewAdapter);
                            }
                        });
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String[] anotherQuerySplit = newText.split(" ");
                if(anotherQuerySplit.length > 0) {
                    for(int i = 0; i < anotherQuerySplit.length; i++) {
                        partViewModel.searchPart(anotherQuerySplit[i]).observe(GalleryFragment.this.getActivity(), new Observer<List<PartEntity>>() {
                            @Override
                            public void onChanged(List<PartEntity> partEntities) {
                                partRecyclerViewAdapter = new PartRecyclerViewAdapter(partEntities, GalleryFragment.this::OnPartClick);
                                partLayout.setAdapter(partRecyclerViewAdapter);
                            }
                        });
                    }
                }
                return false;
            }
        });





        // Setting up floating button and onClickListener with result intent pending
        addPart = binding.partFloatingActionButton;

        addPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditPart.class);
                mStartForResult.launch(intent);
            }
        });


        return root;
    }


    // Handling intent for new parts added
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        assert intent != null;
                        application = intent.getStringExtra(AddEditPart.APPLICATION_TAG);
                        category = intent.getStringExtra(AddEditPart.CATEGORY_TAG);
                        subCategory = intent.getStringExtra(AddEditPart.SUBCATEGORY_TAG);
                        position = intent.getStringExtra(AddEditPart.POSITION_TAG);
                        brand = intent.getStringExtra(AddEditPart.BRAND_TAG);
                        condition = intent.getStringExtra(AddEditPart.CONDITION_TAG);
                        size = intent.getStringExtra(AddEditPart.SIZE_TAG);
                        partNumber = intent.getStringExtra(AddEditPart.PART_NUMBER_TAG);
                        partNumberAlt = intent.getStringExtra(AddEditPart.PART_NUMBER_ALT_TAG);
                        partNumberOE = intent.getStringExtra(AddEditPart.PART_NUMBER_OE_TAG);
                        partWarranty = intent.getStringExtra(AddEditPart.PART_WARRANTY_TAG);
                        location = intent.getStringExtra(AddEditPart.LOCATION_TAG);
                        supplier = intent.getStringExtra(AddEditPart.SUPPLIER_TAG);
                        cost = intent.getStringExtra(AddEditPart.COST_TAG);
                        profit = intent.getStringExtra(AddEditPart.PROFIT_TAG);
                        sell = intent.getStringExtra(AddEditPart.SELL_TAG);
                        note = intent.getStringExtra(AddEditPart.NOTE_TAG);
                        image = intent.getByteArrayExtra(AddEditPart.IMAGE_TAG);
                        time = intent.getStringExtra(AddEditPart.TIME_TAG);

                        Log.d(AddEditPart.TAG, "onActivityResult: " + application + ' ' + category);

                        PartEntity partEntity = new PartEntity(application, category, subCategory,
                                position, brand, condition, size, partNumber, partNumberAlt, partNumberOE,
                                partWarranty, location, supplier, note, image, cost, profit, sell, time);
                        PartViewModel.insertPart(partEntity);
                    }
                }
            });





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


    // Get a hold on the position of the part where user clicked on and load up the info
    // On to the next screen for the user
    @Override
    public void OnPartClick(int adapterPos, PartEntity part) {
        Log.d(AddEditPart.TAG, "OnPartClick: " + adapterPos);
        Intent intent = new Intent(requireActivity().getApplicationContext(), ViewPart.class);
        intent.putExtra(ViewPart.ID_TAG, part.getId());
        intent.putExtra(AddEditPart.APPLICATION_TAG, part.getApplication());
        intent.putExtra(AddEditPart.CATEGORY_TAG, part.getCategory());
        intent.putExtra(AddEditPart.SUBCATEGORY_TAG, part.getSubCategory());
        intent.putExtra(AddEditPart.POSITION_TAG, part.getPosition());
        intent.putExtra(AddEditPart.BRAND_TAG, part.getBrand());
        intent.putExtra(AddEditPart.CONDITION_TAG, part.getCondition());
        intent.putExtra(AddEditPart.SIZE_TAG, part.getSize());
        intent.putExtra(AddEditPart.PART_NUMBER_TAG, part.getPartNumber());
        intent.putExtra(AddEditPart.PART_NUMBER_ALT_TAG, part.getPartNumberAlternative());
        intent.putExtra(AddEditPart.PART_NUMBER_OE_TAG, part.getPartNumberOE());
        intent.putExtra(AddEditPart.PART_WARRANTY_TAG, part.getWarranty());
        intent.putExtra(AddEditPart.LOCATION_TAG, part.getLocation());
        intent.putExtra(AddEditPart.SUPPLIER_TAG, part.getSupplier());
        intent.putExtra(AddEditPart.COST_TAG, part.getCost());
        intent.putExtra(AddEditPart.PROFIT_TAG, part.getProfit());
        intent.putExtra(AddEditPart.SELL_TAG, part.getSell());
        intent.putExtra(AddEditPart.NOTE_TAG, part.getNote());
        intent.putExtra(AddEditPart.IMAGE_TAG, part.getImage());
        intent.putExtra(AddEditPart.TIME_TAG, part.getDateModified());
        startActivity(intent);
    }
}