package com.keane9301.myapp001.ui.slideshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.keane9301.myapp001.Database.Customers.CustomerEntity;
import com.keane9301.myapp001.Database.Customers.CustomerViewModel;
import com.keane9301.myapp001.R;
import com.keane9301.myapp001.ViewsAndAdapters.CustomerRecyclerViewAdapter;
import com.keane9301.myapp001.ViewsAndAdapters.OnCustomerClickListener;
import com.keane9301.myapp001.databinding.FragmentSlideshowBinding;

import java.util.List;

public class SlideshowFragment extends Fragment implements OnCustomerClickListener {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    private CustomerRecyclerViewAdapter customerRecyclerViewAdapter;

    private CustomerViewModel customerViewModel;
    private RecyclerView customerRecyclerView;
    private SearchView customerSearchBar;
    private FloatingActionButton customerFAB;

    private String name;
    private String identification;
    private String shop;
    private String registration;
    private String phone;
    private String phoneAlt;
    private String address;
    private String locLatLng;
    private String time;
    private byte[] image;






    // Getting data from add activity to fill up database
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        // Handle the Intent
                        assert intent != null;
                        Log.d(AddEditCustomer.TAG, "onActivityResult: intent received");
                        name = intent.getStringExtra(AddEditCustomer.NAME_TAG);
                        identification = intent.getStringExtra(AddEditCustomer.IDENTIFICATION_TAG);
                        shop = intent.getStringExtra(AddEditCustomer.SHOP_TAG);
                        registration = intent.getStringExtra(AddEditCustomer.REGISTRATION_TAG);
                        phone = intent.getStringExtra(AddEditCustomer.PHONE_TAG);
                        phoneAlt = intent.getStringExtra(AddEditCustomer.PHONE_ALT_TAG);
                        address = intent.getStringExtra(AddEditCustomer.ADDRESS_TAG);
                        locLatLng = intent.getStringExtra(AddEditCustomer.LOC_LAT_LNG_TAG);
                        image = intent.getByteArrayExtra(AddEditCustomer.IMAGE_TAG);
                        time = intent.getStringExtra(AddEditCustomer.TIME_TAG);

                        CustomerEntity customerEntity = new CustomerEntity(name, identification, shop,
                                registration, phone, phoneAlt, address, locLatLng, time, image);
                        Log.d(AddEditCustomer.TAG, "onActivityResult: before adding");
                        CustomerViewModel.insertCustomer(customerEntity);
                        Log.d(AddEditCustomer.TAG, "onActivityResult: after added " +
                                customerEntity.getId() + customerEntity.getName());
                    }
                }
            });






    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        slideshowViewModel =
//                new ViewModelProvider(this).get(SlideshowViewModel.class);

        customerViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getActivity().getApplication()).create(CustomerViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        customerRecyclerView = binding.customerRecyclerView;
        customerRecyclerView.setHasFixedSize(true);
        customerRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));

        customerSearchBar = binding.customerSearchView;
        customerFAB = binding.customerFloatingActionButton;

        // Populate the view with live data from database and implements on click listener
        customerViewModel.getAllCustomers().observe(this.getActivity(), new Observer<List<CustomerEntity>>() {
            @Override
            public void onChanged(List<CustomerEntity> customerEntities) {
                customerRecyclerViewAdapter = new CustomerRecyclerViewAdapter(customerEntities, SlideshowFragment.this::OnCustomerClick);
                customerRecyclerView.setAdapter(customerRecyclerViewAdapter);
            }
        });


        // Getting input from user when user search data in the database
        // Filter those lists based on keywords and populate the view with on click listener
        customerSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String[] querySplit = query.split(" ");
                if(querySplit.length > 0) {
                    for(int i = 0; i < querySplit.length; i++) {
                        customerViewModel.searchCustomer(querySplit[i]).observe(SlideshowFragment.this.getActivity(), new Observer<List<CustomerEntity>>() {
                            @Override
                            public void onChanged(List<CustomerEntity> customerEntities) {
                                customerRecyclerViewAdapter = new CustomerRecyclerViewAdapter(customerEntities, SlideshowFragment.this::OnCustomerClick);
                                customerRecyclerView.setAdapter(customerRecyclerViewAdapter);
                            }
                        });
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String[] newTextSplit = newText.split(" ");
                if(newTextSplit.length > 0) {
                    for(int i = 0; i < newTextSplit.length; i++) {
                        customerViewModel.searchCustomer(newTextSplit[i]).observe(SlideshowFragment.this.getActivity(), new Observer<List<CustomerEntity>>() {
                            @Override
                            public void onChanged(List<CustomerEntity> customerEntities) {
                                customerRecyclerViewAdapter = new CustomerRecyclerViewAdapter(customerEntities, SlideshowFragment.this::OnCustomerClick);
                                customerRecyclerView.setAdapter(customerRecyclerViewAdapter);
                            }
                        });
                    }
                }
                return false;
            }
        });






        // Floating action button used when user wanted to add new customers
        customerFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddEditCustomer.class);
                mStartForResult.launch(intent);
            }
        });





        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    // Passing the data when user click on certain item on the view by pinpoint which item
    // using adapter position to get the right data to be passed to the view activity
    @Override
    public void OnCustomerClick(int adapterPosition, CustomerEntity customer) {
        Intent intent = new Intent(requireActivity().getApplicationContext(), ViewCustomer.class);
        intent.putExtra(AddEditCustomer.ID_TAG, customer.getId());
        intent.putExtra(AddEditCustomer.NAME_TAG, customer.getName());
        intent.putExtra(AddEditCustomer.IDENTIFICATION_TAG, customer.getIdentification());
        intent.putExtra(AddEditCustomer.SHOP_TAG, customer.getShopName());
        intent.putExtra(AddEditCustomer.REGISTRATION_TAG, customer.getRegistration());
        intent.putExtra(AddEditCustomer.PHONE_TAG, customer.getNumber());
        intent.putExtra(AddEditCustomer.PHONE_ALT_TAG, customer.getNumberAlternative());
        intent.putExtra(AddEditCustomer.ADDRESS_TAG, customer.getAddress());
        intent.putExtra(AddEditCustomer.LOC_LAT_LNG_TAG, customer.getLatLng());
        intent.putExtra(AddEditCustomer.IMAGE_TAG, customer.getImage());
        intent.putExtra(AddEditCustomer.TIME_TAG, customer.getLastModified());
        Log.d(AddEditCustomer.TAG, "OnCustomerClick: adapter " + adapterPosition + ' ' + customer.getId() + ' ' + customer.getLastModified());
        startActivity(intent);
    }







}