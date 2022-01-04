package com.example.redelcom_test.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redelcom_test.R;
import com.example.redelcom_test.model.MusicListModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    private final ArrayList<MusicListModel> musicModelArrayList;
    private final Context context;
    private final OnItemMusicClickListener onItemMusicClickListener;

    public MusicAdapter(ArrayList<MusicListModel> musicModelArrayList, Context context, OnItemMusicClickListener onItemMusicClickListener) {
        this.musicModelArrayList = musicModelArrayList;
        this.context = context;
        this.onItemMusicClickListener = onItemMusicClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, onItemMusicClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.ViewHolder holder, int position) {
        MusicListModel musicModel = musicModelArrayList.get(position);
        holder.trackName.setText(musicModel.getTrackName());
        holder.artist.setText(musicModel.getArtist());
        Picasso.get().load(musicModel.getArtwork100()).into(holder.artwork);
        if (musicModel.isChecked()) holder.historic.setVisibility(View.VISIBLE);
    }

    @Override
    public int getItemCount() {
        return musicModelArrayList.size();
    }

    public interface OnItemMusicClickListener {
        void onItemClick(int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final TextView trackName;
        private final TextView artist;
        private final ImageView artwork;
        private final ImageView historic;
        OnItemMusicClickListener onItemMusicClickListener;

        public ViewHolder(View itemView, OnItemMusicClickListener onItemMusicClickListener) {
            super(itemView);
            trackName = itemView.findViewById(R.id.list_item_title);
            artist = itemView.findViewById(R.id.list_item_subtitle);
            artwork = itemView.findViewById(R.id.list_item_image);
            historic = itemView.findViewById(R.id.list_item_checked);
            this.onItemMusicClickListener = onItemMusicClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemMusicClickListener.onItemClick(getAdapterPosition());
        }
    }
}

