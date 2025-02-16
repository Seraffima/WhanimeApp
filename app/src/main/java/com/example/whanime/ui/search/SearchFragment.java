package com.example.whanime.ui.search;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import com.example.whanime.R;
import java.util.ArrayList;
import java.util.List;

// Fragmento para manejar la búsqueda de elementos
public class SearchFragment extends Fragment {

    private RecyclerView recyclerView; // RecyclerView para mostrar los elementos de búsqueda
    private SearchAdapter searchAdapter; // Adaptador para el RecyclerView
    private SearchViewModel searchViewModel; // ViewModel para los elementos de búsqueda
    private SearchView searchView; // SearchView para filtrar los elementos

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchAdapter = new SearchAdapter(new ArrayList<>(), item -> {
            // Manejar clic en el elemento
        });
        recyclerView.setAdapter(searchAdapter);

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        searchViewModel.getAllItems().observe(getViewLifecycleOwner(), new Observer<List<SearchItem>>() {
            @Override
            public void onChanged(List<SearchItem> items) {
                searchAdapter.setSearchItems(items);
            }
        });

        searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchAdapter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.filter(newText);
                return false;
            }
        });

        // Agregar ItemTouchHelper para deslizar y eliminar
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

        // Apply text size preference
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        int textSize = sharedPreferences.getInt("text_size", 16);
        applyTextSize(view, textSize);

        return view;
    }

    private void applyTextSize(View view, int textSize) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                applyTextSize(viewGroup.getChildAt(i), textSize);
            }
        } else if (view instanceof TextView) {
            ((TextView) view).setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
        }
    }
}