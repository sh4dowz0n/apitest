package com.example.redelcom_test.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.redelcom_test.R;
import com.example.redelcom_test.helper.AppHelper;
import com.example.redelcom_test.model.MusicModel;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;


public class MusicItemAdapter extends RecyclerView.Adapter<MusicItemAdapter.ViewHolder> {
    private final ArrayList<MusicModel> musicModelArrayList;
    private final Context context;


    public MusicItemAdapter(ArrayList<MusicModel> musicModelArrayList, Context context) {
        this.musicModelArrayList = musicModelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.music_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicItemAdapter.ViewHolder holder, int position) {
        MusicModel musicModel = musicModelArrayList.get(position);
        holder.trackName.setText(musicModel.getTrackName());
        holder.artist.setText(musicModel.getArtistName());
        Picasso.get().load(musicModel.getArtworkUrl100()).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                holder.imageButton.setBackground(new BitmapDrawable(bitmap));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
        if (new AppHelper().isInternetAvalaible()) {
            MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(musicModel.getPreviewUrl()));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(mp -> holder.imageButton.setOnClickListener(v -> {
                if (mp.isPlaying()) {
                    mp.pause();
                    mp.seekTo(0);
                    holder.imageButton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
                } else if (!mp.isPlaying()) {
                    mp.start();
                    holder.imageButton.setImageResource(R.drawable.ic_baseline_stop_circle_24);
                }
            }));
        }
    }

    @Override
    public int getItemCount() {
        return musicModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView trackName;
        private final TextView artist;
        private final ImageButton imageButton;

        public ViewHolder(View itemView) {
            super(itemView);
            trackName = itemView.findViewById(R.id.music_item_name);
            artist = itemView.findViewById(R.id.music_item_artist);
            imageButton = itemView.findViewById(R.id.music_list_image);
        }
    }
}
