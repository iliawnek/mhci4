package uk.ac.gla.shopping.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import uk.ac.gla.shopping.R;
import uk.ac.gla.shopping.databinding.FragmentShoppingListBinding;
import uk.ac.gla.shopping.recyclerview.ShoppingListActivityRecyclerViewAdapter;
import uk.ac.gla.shopping.viewmodel.ShoppingListItemsViewModel;

public class ShoppingListFragment extends Fragment {
    public static ShoppingListFragment newInstance() {
        ShoppingListFragment fragment = new ShoppingListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        FragmentShoppingListBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shopping_list, container, false);

        ShoppingListActivityRecyclerViewAdapter recyclerViewAdapter = new ShoppingListActivityRecyclerViewAdapter(new ArrayList<>());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(recyclerViewAdapter);

        ShoppingListItemsViewModel viewModel = ViewModelProviders.of(this).get(ShoppingListItemsViewModel.class);

        viewModel.getShoppingListItems().observe(ShoppingListFragment.this, recyclerViewAdapter::setShoppingListItems);

        return binding.getRoot();
    }
}