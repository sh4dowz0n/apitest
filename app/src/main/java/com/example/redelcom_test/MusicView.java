package com.example.redelcom_test;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.redelcom_test.adapter.MusicItemAdapter;
import com.example.redelcom_test.helper.AppHelper;
import com.example.redelcom_test.model.MusicModel;
import com.example.redelcom_test.sqlite.MusicFacade;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MusicView extends AppCompatActivity {

    private ProgressBar progressBar;
    private ArrayList<MusicModel> musicModelArrayList;
    private RecyclerView recyclerView;
    private MusicItemAdapter musicItemAdapter;
    private int collectionId;
    private JSONArray dataArray;
    private final AppHelper helper = new AppHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_view);
        Bundle extras = getIntent().getExtras();

        TextView title = findViewById(R.id.view_album_title);
        title.setText(extras.getString("collectionName"));
        TextView band = findViewById(R.id.view_album_band);
        band.setText(extras.getString("artistName"));
        ImageView imageBand = findViewById(R.id.view_album_image);
        Picasso.get().load(extras.getString("artwork")).into(imageBand);
        progressBar = findViewById(R.id.music_progress_bar);
        collectionId = extras.getInt("collectionId");
        musicModelArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.list_view_recyclerview_album);
        NestedScrollView nestedScrollView = findViewById(R.id.nested_list_view_album);
        String collectionName = extras.getString("collectionName");
        if (collectionName.contains("(")) {
            collectionName = collectionName.split("\\(")[0];
        } else if (collectionName.contains("[")) {
            collectionName = collectionName.split("\\[")[0];
        }
        getListData(collectionName);
    }

    private void getListData(String keyWord) {
        if (helper.isInternetAvalaible()) {
            String url = "https://itunes.apple.com/search?term=" + keyWord + "&mediaType=music&attribute=albumTerm";
            RequestQueue queue = Volley.newRequestQueue(MusicView.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    dataArray = response.getJSONArray("results");
                    setRecyclerData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(MusicView.this, "Fail to get data..", Toast.LENGTH_SHORT).show());
            queue.add(jsonObjectRequest);
        } else {
            ArrayList<MusicModel> arrayList = new MusicFacade(this).getSingsByCollection(collectionId);
            setRecyclerWithoutNetwork(arrayList);
        }
    }

    private void setRecyclerData() throws JSONException {
        for (int i = 0; i < dataArray.length(); i++) {
            JSONObject jsonObject = dataArray.getJSONObject(i);
            if (jsonObject.getInt("collectionId") != collectionId) continue;
            MusicModel musicModel = new MusicModel();
            musicModel.setArtworkUrl100(jsonObject.getString("artworkUrl100"));
            musicModel.setTrackName(jsonObject.getString("trackName"));
            musicModel.setArtistName(jsonObject.getString("artistName"));
            musicModel.setPreviewUrl(jsonObject.getString("previewUrl"));
            musicModel.setCollectionId(jsonObject.getString("collectionId"));
            musicModel.setCollectionName(jsonObject.getString("collectionName"));
            new MusicFacade(this).createSing(musicModel);
            musicModelArrayList.add(musicModel);
            musicItemAdapter = new MusicItemAdapter(musicModelArrayList, MusicView.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(MusicView.this));
            recyclerView.setAdapter(musicItemAdapter);
        }
        progressBar.setVisibility(View.GONE);
    }

    public void setRecyclerWithoutNetwork(ArrayList<MusicModel> arrayList) {
        for (MusicModel musicModel: arrayList) {
            musicModelArrayList.add(musicModel);
            musicItemAdapter = new MusicItemAdapter(musicModelArrayList, MusicView.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(MusicView.this));
            recyclerView.setAdapter(musicItemAdapter);
        }
        progressBar.setVisibility(View.GONE);
    }
}