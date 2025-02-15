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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<SearchItem> searchItems;
    private List<SearchItem> searchItemsFull;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(SearchItem item);
    }

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

    public void setSearchItems(List<SearchItem> searchItems) {
        this.searchItems = new ArrayList<>(searchItems);
        this.searchItemsFull = new ArrayList<>(searchItems);
        notifyDataSetChanged();
    }

    public void removeItem(SearchItem item) {
        int position = searchItems.indexOf(item);
        if (position != -1) {
            searchItems.remove(position);
            notifyItemRemoved(position);
        }
    }

    public SearchItem getSearchItemAtPosition(int position) {
        return searchItems.get(position);
    }

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

        public void bind(SearchItem item, OnItemClickListener onItemClickListener) {
            Glide.with(itemView.getContext()).load(item.getImage()).into(imageView);
            filenameTextView.setText(item.getName());
            episodeTextView.setText(item.getEpisode());
            itemView.setOnClickListener(v -> {
                onItemClickListener.onItemClick(item);
                showAlertDialog(item);
            });
        }

        private void showAlertDialog(SearchItem item) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Anime Details");

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
            nameTextView.setText("Name: " + item.getName());
            nameTextView.setTextSize(18);
            nameTextView.setPadding(10, 10, 10, 10);

            TextView episodeTextView = new TextView(itemView.getContext());
            episodeTextView.setText("Episode: " + item.getEpisode());
            episodeTextView.setTextSize(18);
            episodeTextView.setPadding(10, 10, 10, 10);

            layout.addView(imageView);
            layout.addView(nameTextView);
            layout.addView(episodeTextView);

            ScrollView scrollView = new ScrollView(itemView.getContext());
            scrollView.addView(layout);

            builder.setView(scrollView);
            builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
}