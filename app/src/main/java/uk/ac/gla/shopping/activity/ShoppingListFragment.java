package uk.ac.gla.shopping.activity;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;

import uk.ac.gla.shopping.R;
import uk.ac.gla.shopping.database.ShoppingListItemDatabase;
import uk.ac.gla.shopping.databinding.FragmentShoppingListBinding;
import uk.ac.gla.shopping.entity.ShoppingListItem;
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

        ShoppingListActivityRecyclerViewAdapter recyclerViewAdapter = new ShoppingListActivityRecyclerViewAdapter(new ArrayList<>(), getContext());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(recyclerViewAdapter);

        ShoppingListItemsViewModel viewModel = ViewModelProviders.of(this).get(ShoppingListItemsViewModel.class);

        viewModel.getShoppingListItems().observe(ShoppingListFragment.this, recyclerViewAdapter::setShoppingListItems);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = getView().findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_create_list_item, null);
                builder.setView(dialogView)
                        .setPositiveButton("Add", null)
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });
                builder.setTitle("Add item");
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();

                dialog.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface d) {
                        Button b = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        b.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {
                                AutoCompleteTextView autocomplete = dialogView.findViewById(R.id.autocomplete_product);
                                String inputName = autocomplete.getText().toString();
                                if (inputName.length() > 0) {
                                    new AsyncTask<Void, Void, Void>() {
                                        @Override
                                        protected Void doInBackground(Void... params) {
                                            ShoppingListItemDatabase.getInstance(getActivity().getApplicationContext()).shoppingListItemDao().insertShoppingListItems(new ShoppingListItem(inputName));
                                            return null;
                                        }
                                    }.execute();
                                    dialog.dismiss();
                                } else {
                                    autocomplete.setError("Give the item a name");
                                }
                            }
                        });
                    }
                });

                dialog.show();

                AutoCompleteTextView autocomplete = dialog.findViewById(R.id.autocomplete_product);
                String[] productsArray = getResources().getStringArray(R.array.recipe_products_array);
                autocomplete.setAdapter(new ArrayAdapter<String>(dialog.getContext(), android.R.layout.simple_list_item_1, productsArray));

                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
            }
        });
    }
}