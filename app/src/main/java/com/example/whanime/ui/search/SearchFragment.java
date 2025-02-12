package com.example.whanime.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.whanime.R;
import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchAdapter adapter;
    private List<SearchItem> searchItems;
    private List<SearchItem> filteredItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        SearchView searchView = view.findViewById(R.id.search_view);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        searchItems = new ArrayList<>();
        filteredItems = new ArrayList<>();
        // Add sample data to searchItems
        searchItems.add(new SearchItem("https://images.plurk.com/32B15UXxymfSMwKGTObY5e.jpg", "Main Title 1", "Sub Title 1"));
        searchItems.add(new SearchItem("https://cirsova.wordpress.com/2023/11/27/thoughts-on-frieren-beyond-journeys-end/", "Main Title 2", "Sub Title 2"));
        // Add more items as needed

        filteredItems.addAll(searchItems);

        adapter = new SearchAdapter(filteredItems, item -> {
            // Handle item click
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                filteredItems.remove(position);
                adapter.notifyItemRemoved(position);
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        return view;
    }

    private void filter(String text) {
        filteredItems.clear();
        if (text.isEmpty()) {
            filteredItems.addAll(searchItems);
        } else {
            for (SearchItem item : searchItems) {
                if (item.getMainTitle().toLowerCase().contains(text.toLowerCase()) ||
                        item.getSubTitle().toLowerCase().contains(text.toLowerCase())) {
                    filteredItems.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}