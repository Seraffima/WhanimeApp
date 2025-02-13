package com.example.whanime.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.whanime.R;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private SearchAdapter searchAdapter;
    private SearchViewModel searchViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchAdapter = new SearchAdapter(new ArrayList<>(), item -> {
            // Handle item click
        });
        recyclerView.setAdapter(searchAdapter);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<SearchItem>>() {
            @Override
            public void onChanged(List<SearchItem> items) {
                searchAdapter.setSearchItems(items);
            }
        });

        // Add ItemTouchHelper for swipe-to-delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                SearchItem item = searchAdapter.getSearchItemAtPosition(position);
                searchViewModel.delete(item);
                searchAdapter.removeItem(item);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }
}