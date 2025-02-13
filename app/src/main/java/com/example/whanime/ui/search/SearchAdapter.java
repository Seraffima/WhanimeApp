package com.example.whanime.ui.search;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.whanime.R;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<SearchItem> searchItems;
    private final OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(SearchItem item);
    }

    public SearchAdapter(List<SearchItem> searchItems, OnItemClickListener onItemClickListener) {
        this.searchItems = searchItems;
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
        this.searchItems = searchItems;
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
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item));
        }
    }
}