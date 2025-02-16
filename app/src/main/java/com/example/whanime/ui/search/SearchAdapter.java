package com.example.whanime.ui.search;

import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.whanime.R;
import java.util.ArrayList;
import java.util.List;

// Adaptador para mostrar los elementos de búsqueda en un RecyclerView
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<SearchItem> searchItems; // Lista de elementos de búsqueda
    private List<SearchItem> searchItemsFull; // Lista completa de elementos de búsqueda
    private final OnItemClickListener onItemClickListener; // Interfaz para manejar clics en los elementos

    // Interfaz para manejar clics en los elementos
    public interface OnItemClickListener {
        void onItemClick(SearchItem item);
    }

    // Constructor del adaptador
    public SearchAdapter(List<SearchItem> searchItems, OnItemClickListener onItemClickListener) {
        this.searchItems = new ArrayList<>(searchItems);
        this.searchItemsFull = new ArrayList<>(searchItems);
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchItem item = searchItems.get(position);
        holder.bind(item, onItemClickListener);
    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }

    // Método para establecer los elementos de búsqueda
    public void setSearchItems(List<SearchItem> searchItems) {
        this.searchItems = new ArrayList<>(searchItems);
        this.searchItemsFull = new ArrayList<>(searchItems);
        notifyDataSetChanged();
    }

    // Método para eliminar un elemento de búsqueda
    public void removeItem(SearchItem item) {
        int position = searchItems.indexOf(item);
        if (position != -1) {
            searchItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Método para obtener un elemento de búsqueda en una posición específica
    public SearchItem getSearchItemAtPosition(int position) {
        return searchItems.get(position);
    }

    // Método para filtrar los elementos de búsqueda
    public void filter(String text) {
        List<SearchItem> filteredList = new ArrayList<>();
        if (text.isEmpty()) {
            filteredList.addAll(searchItemsFull);
        } else {
            text = text.toLowerCase();
            for (SearchItem item : searchItemsFull) {
                if (item.getName().toLowerCase().contains(text) || item.getEpisode().toLowerCase().contains(text)) {
                    filteredList.add(item);
                }
            }
        }
        searchItems.clear();
        searchItems.addAll(filteredList);
        notifyDataSetChanged();
    }

    // ViewHolder para los elementos de búsqueda
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView filenameTextView;
        private final TextView episodeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            filenameTextView = itemView.findViewById(R.id.item_main_title);
            episodeTextView = itemView.findViewById(R.id.item_sub_title);
        }

        // Método para enlazar un elemento de búsqueda con el ViewHolder
        public void bind(SearchItem item, OnItemClickListener onItemClickListener) {
            Glide.with(itemView.getContext()).load(item.getImage()).into(imageView);
            filenameTextView.setText(item.getName());
            episodeTextView.setText(item.getEpisode());
            itemView.setOnClickListener(v -> {
                onItemClickListener.onItemClick(item);
                showAlertDialog(item);
            });
        }

        // Metodo para mostrar un diálogo con los detalles del anime
        private void showAlertDialog(SearchItem item) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle(R.string.anime_details); // Use string resource for the title

            LinearLayout layout = new LinearLayout(itemView.getContext());
            layout.setOrientation(LinearLayout.VERTICAL);

            ImageView imageView = new ImageView(itemView.getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    600, // width in pixels
                    400  // height in pixels
            );
            layoutParams.setMargins(10, 10, 10, 10);
            imageView.setLayoutParams(layoutParams);

            Glide.with(itemView.getContext())
                    .load(item.getImage()) // Load the image instead of the GIF
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Enable caching
                    .override(600, 400) // Limit the size
                    .into(imageView); // Use ImageView directly

            TextView nameTextView = new TextView(itemView.getContext());
            nameTextView.setText(itemView.getContext().getString(R.string.name, item.getName())); // Use string resource for the name
            nameTextView.setTextSize(18);
            nameTextView.setPadding(10, 10, 10, 10);

            TextView episodeTextView = new TextView(itemView.getContext());
            episodeTextView.setText(itemView.getContext().getString(R.string.episode, item.getEpisode())); // Use string resource for the episode
            episodeTextView.setTextSize(18);
            episodeTextView.setPadding(10, 10, 10, 10);

            layout.addView(imageView);
            layout.addView(nameTextView);
            layout.addView(episodeTextView);

            ScrollView scrollView = new ScrollView(itemView.getContext());
            scrollView.addView(layout);

            builder.setView(scrollView);
            builder.setPositiveButton(R.string.ok, (dialog, which) -> dialog.dismiss()); // Use string resource for the button

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}