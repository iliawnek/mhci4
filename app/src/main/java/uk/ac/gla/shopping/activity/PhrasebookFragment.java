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
import uk.ac.gla.shopping.databinding.FragmentPhrasebookBinding;
import uk.ac.gla.shopping.recyclerview.PhrasebookActivityRecyclerViewAdapter;
import uk.ac.gla.shopping.viewmodel.PhraseViewModel;

public class PhrasebookFragment extends Fragment {
    public static PhrasebookFragment newInstance() {
        PhrasebookFragment fragment = new PhrasebookFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentPhrasebookBinding binding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_phrasebook,
                container,
                false
        );

        PhrasebookActivityRecyclerViewAdapter recyclerViewAdapter = new PhrasebookActivityRecyclerViewAdapter(
                new ArrayList<>(),
                getContext()
        );

        binding.phrasebookRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.phrasebookRecyclerView.setAdapter(recyclerViewAdapter);

        PhraseViewModel viewModel = ViewModelProviders.of(this).get(PhraseViewModel.class);
        viewModel.getPhrases().observe(PhrasebookFragment.this, recyclerViewAdapter::setPhrases);

        return binding.getRoot();
    }
}