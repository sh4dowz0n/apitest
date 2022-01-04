package com.example.redelcom_test;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.redelcom_test.adapter.MusicAdapter;
import com.example.redelcom_test.helper.AppHelper;
import com.example.redelcom_test.model.MusicListModel;
import com.example.redelcom_test.sqlite.HistoricFacade;
import com.example.redelcom_test.sqlite.SqliteHelper;
import com.example.redelcom_test.sqlite.model.HistoricModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements MusicAdapter.OnItemMusicClickListener {

    private ArrayList<MusicListModel> musicModelArrayList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private AutoCompleteTextView textInputEditText;
    private int init = 0, end = 20;
    private JSONArray dataArray;
    private LinearLayout linearLayout;
    private final AppHelper helper = new AppHelper();

    private String checked;
    private String keyWord = "";
    private String prevWord = "";
    private ArrayList<String> historic_search = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SqliteHelper sqliteHelper = new SqliteHelper(this);
        linearLayout = findViewById(R.id.not_network_layout);
        if (!helper.isInternetAvalaible()) {
            linearLayout.setVisibility(View.VISIBLE);
            helper.notNetworkDialog(this);
        }
        musicModelArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.list_view_recyclerview);
        progressBar = findViewById(R.id.list_progress_bar);
        textInputEditText = findViewById(R.id.list_filter_input);
        textInputEditText.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                pressEnterKey();
                getListData();
            }
            return false;
        });
        ArrayAdapter<String> search_adapter = new ArrayAdapter<String>(this, R.layout.historic_drop_textview, historic_search);
        ArrayList<HistoricModel> arrayList = new HistoricFacade(this).getAllHistoric();
        for (HistoricModel model : arrayList) historic_search.add(model.getText());
        textInputEditText.setAdapter(search_adapter);
        textInputEditText.setOnItemClickListener((parent, view, position, id) -> {
            keyWord = (String) parent.getItemAtPosition(position);
            getListData();
        });

        NestedScrollView nestedScrollView = findViewById(R.id.nested_list_view);
        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                if (end >= 200 || init > dataArray.length()) {
                    Toast.makeText(this, R.string.no_load_more, Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                init += 20;
                end += 20;
                getListData();
            }
        });
    }

    private void getListData() {
        keyWord = helper.capitalizeFirst(keyWord);
        checked = new HistoricFacade(this).getHistoricChecked(keyWord);
        if (dataArray == null && helper.isInternetAvalaible()) {
            String url = "https://itunes.apple.com/search?term=" + keyWord + "&media=music";
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
                try {
                    dataArray = response.getJSONArray("results");
                    new HistoricFacade(this).createHistoric(new HistoricModel(keyWord, dataArray.toString(), ""));
                    setRecyclerData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> Toast.makeText(MainActivity.this, "Fail to get data..", Toast.LENGTH_SHORT).show());
            queue.add(jsonObjectRequest);
        } else if (dataArray != null) {
            try {
                setRecyclerData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            linearLayout.setVisibility(View.GONE);
            try {
                setOfflineRecyclerData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setRecyclerData() throws JSONException {
        progressBar.setVisibility(View.VISIBLE);
        if (dataArray.length() < end) end = dataArray.length();
        for (int i = init; i < end; i++) {
            JSONObject jsonObject = dataArray.getJSONObject(i);
            MusicListModel musicListModel = new MusicListModel();
            musicListModel.setArtwork100(jsonObject.getString("artworkUrl100"));
            musicListModel.setTrackName(jsonObject.getString("trackName"));
            musicListModel.setArtist(jsonObject.getString("artistName"));
            musicListModel.setCollectionName(jsonObject.getString("collectionName"));
            musicListModel.setCollectionId(jsonObject.getInt("collectionId"));
            musicListModel.setTrackId(jsonObject.getInt("trackId"));
            if (checked.contains(Integer.toString(jsonObject.getInt("trackId")))) musicListModel.setChecked(true);
            musicModelArrayList.add(musicListModel);
            MusicAdapter musicAdapter = new MusicAdapter(musicModelArrayList, MainActivity.this, this);
            recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            recyclerView.setAdapter(musicAdapter);
        }
        progressBar.setVisibility(View.GONE);
    }

    public void setOfflineRecyclerData() throws JSONException {
        Cursor cursor = new HistoricFacade(this).getData(keyWord);
        if (dataArray.length() < end) end = dataArray.length();
        cursor.moveToFirst();
        JSONArray jsonArray = new JSONArray(cursor.getString(0));
    }

    private void pressEnterKey() {
        if (keyWord.equalsIgnoreCase("")) {
            keyWord = Objects.requireNonNull(textInputEditText.getText()).toString();
            prevWord = keyWord;
        } else keyWord = Objects.requireNonNull(textInputEditText.getText()).toString();
        if (!keyWord.equalsIgnoreCase(prevWord)) {
            musicModelArrayList.clear();
            prevWord = keyWord;
            dataArray = null;
            init = 0;
            end = 20;
        }
    }

    @Override
    public void onItemClick(int position) {
        MusicListModel musicListModel = musicModelArrayList.get(position);
        Intent intent = new Intent(this, MusicView.class);
        intent.putExtra("term", keyWord);
        intent.putExtra("artwork", musicListModel.getArtwork100());
        intent.putExtra("trackName", musicListModel.getTrackName());
        intent.putExtra("artistName", musicListModel.getArtist());
        intent.putExtra("collectionName", musicListModel.getCollectionName());
        intent.putExtra("collectionId", musicListModel.getCollectionId());
        new HistoricFacade(this).updateCheked(keyWord, musicListModel.getTrackId());
        startActivity(intent);
    }

}