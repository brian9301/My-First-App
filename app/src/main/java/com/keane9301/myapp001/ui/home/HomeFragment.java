package com.keane9301.myapp001.ui.home;

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
import com.keane9301.myapp001.Database.Lubes.LubeEntity;
import com.keane9301.myapp001.Database.Lubes.LubeViewModel;
import com.keane9301.myapp001.Database.Parts.PartEntity;
import com.keane9301.myapp001.ViewsAndAdapters.LubeRecyclerViewAdapter;
import com.keane9301.myapp001.ViewsAndAdapters.OnLubeClickListener;
import com.keane9301.myapp001.ViewsAndAdapters.PartRecyclerViewAdapter;
import com.keane9301.myapp001.databinding.FragmentHomeBinding;
import com.keane9301.myapp001.ui.gallery.GalleryFragment;

import java.util.List;

public class HomeFragment extends Fragment implements OnLubeClickListener {

    private HomeViewModel homeViewModel;
    private LubeViewModel lubeViewModel;
    private LubeRecyclerViewAdapter lubeRecyclerViewAdapter;
    private RecyclerView lubeLayout;

    private FragmentHomeBinding binding;
    private FloatingActionButton addLube;
    private SearchView searchBar;

    private int id;
    private String brand;
    private String model;
    private String grade;
    private String viscosity;
    private String location;
    private String partNumberOE;
    private String content;
    private String cost;
    private String profit;
    private String sell;
    private String note;

    private byte[] image;

    private String time;







    // Process data received to be stored as new entry into database
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        brand = intent.getStringExtra(AddEditLube.BRAND_TAG);
                        model = intent.getStringExtra(AddEditLube.MODEL_TAG);
                        grade = intent.getStringExtra(AddEditLube.GRADE_TAG);
                        viscosity = intent.getStringExtra(AddEditLube.VISCOSITY_TAG);
                        location = intent.getStringExtra(AddEditLube.LOCATION_TAG);
                        partNumberOE = intent.getStringExtra(AddEditLube.PART_NUMBER_OE_TAG);
                        content = intent.getStringExtra(AddEditLube.CONTENT_TAG);
                        cost = intent.getStringExtra(AddEditLube.COST_TAG);
                        profit = intent.getStringExtra(AddEditLube.PROFIT_TAG);
                        sell = intent.getStringExtra(AddEditLube.SELL_TAG);
                        note = intent.getStringExtra(AddEditLube.NOTE_TAG);
                        image = intent.getByteArrayExtra(AddEditLube.IMAGE_TAG);
                        time = intent.getStringExtra(AddEditLube.TIME_TAG);

                        LubeEntity lubeEntity = new LubeEntity(brand, model, grade, viscosity, content, location,
                                cost, profit, sell, note, image, time);
                        LubeViewModel.insertLube(lubeEntity);
                    }
                }
            });




    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        lubeViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getActivity().getApplication()).create(LubeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        searchBar = binding.lubeSearchView;

        lubeLayout = binding.lubeRecyclerView;
        lubeLayout.setHasFixedSize(true);
        lubeLayout.setLayoutManager(new LinearLayoutManager(this.getActivity()));



        lubeViewModel.getAllLubes().observe(this.getActivity(), new Observer<List<LubeEntity>>() {
            @Override
            public void onChanged(List<LubeEntity> lubeEntities) {
                lubeRecyclerViewAdapter = new LubeRecyclerViewAdapter(lubeEntities, HomeFragment.this::OnLubeClick);
                lubeLayout.setAdapter(lubeRecyclerViewAdapter);
            }
        });



        searchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String[] querySplit = query.split(" ");
                if(querySplit.length > 0) {
                    for(int i = 0; i < querySplit.length; i++) {
                        lubeViewModel.searchLube(querySplit[i]).observe(HomeFragment.this.getActivity(), new Observer<List<LubeEntity>>() {
                            @Override
                            public void onChanged(List<LubeEntity> lubeEntities) {
                                lubeRecyclerViewAdapter = new LubeRecyclerViewAdapter(lubeEntities, HomeFragment.this::OnLubeClick);
                                lubeLayout.setAdapter(lubeRecyclerViewAdapter);
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
                        lubeViewModel.searchLube(anotherQuerySplit[i]).observe(HomeFragment.this.getActivity(), new Observer<List<LubeEntity>>() {
                            @Override
                            public void onChanged(List<LubeEntity> lubeEntities) {
                                lubeRecyclerViewAdapter = new LubeRecyclerViewAdapter(lubeEntities, HomeFragment.this::OnLubeClick);
                                lubeLayout.setAdapter(lubeRecyclerViewAdapter);
                            }
                        });
                    }
                }
                return false;
            }
        });





        // Floating button listener and launching add new item with result pending
        addLube = binding.lubeFloatingActionButton;

        addLube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditLube.class);
                mStartForResult.launch(intent);
            }
        });



        return root;
    }



    // Read the position of user click then pick up the right data to be pass to view class
    @Override
    public void OnLubeClick(int adapterPos, LubeEntity lubeEntity) {
        Intent intent = new Intent(requireActivity().getApplicationContext(), ViewLube.class);
        intent.putExtra(ViewLube.ID_TAG, lubeEntity.getId());
        intent.putExtra(AddEditLube.BRAND_TAG, lubeEntity.getBrand());
        intent.putExtra(AddEditLube.MODEL_TAG, lubeEntity.getModel());
        intent.putExtra(AddEditLube.GRADE_TAG, lubeEntity.getGrade());
        intent.putExtra(AddEditLube.VISCOSITY_TAG, lubeEntity.getViscosity());
        intent.putExtra(AddEditLube.CONTENT_TAG, lubeEntity.getContent());
        intent.putExtra(AddEditLube.LOCATION_TAG, lubeEntity.getLocation());
        intent.putExtra(AddEditLube.COST_TAG, lubeEntity.getCost());
        intent.putExtra(AddEditLube.PROFIT_TAG, lubeEntity.getProfit());
        intent.putExtra(AddEditLube.SELL_TAG, lubeEntity.getSell());
        intent.putExtra(AddEditLube.NOTE_TAG, lubeEntity.getNote());
        intent.putExtra(AddEditLube.IMAGE_TAG, lubeEntity.getImage());
        intent.putExtra(AddEditLube.TIME_TAG, lubeEntity.getDateModified());
        Log.d(AddEditLube.TAG, "OnLubeClick: " + lubeEntity.getBrand() + " " + lubeEntity.getModel());
        startActivity(intent);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}