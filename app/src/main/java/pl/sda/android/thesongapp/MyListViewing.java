package pl.sda.android.thesongapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MyListViewing extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<ListItem> listItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_viewing);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int engine = getEngine();

        setRecyclerViewWorking();

        threeMethods(engine);
    }

    private int getEngine() {
        Bundle bundle = getIntent().getExtras();
        return bundle.getInt("engine");
    }

    private void setRecyclerViewWorking() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
        case R.id.action_sortByName:
            sortByName();
            return true;
            case R.id.action_sortByArtist:
            sortByArtist();
            return true;
            case R.id.action_sortByReleaseDate:
            sortByRelease();
            return true;
            case R.id.action_sortByDatabaseType:
            sortByDatabase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sortByDatabase() {
        Collections.sort(listItems, new Comparator<ListItem>() {
            @Override
            public int compare(ListItem o1, ListItem o2) {
                return o1.getImageUrl().compareTo(o2.getImageUrl());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByRelease() {
        Collections.sort(listItems, new Comparator<ListItem>() {
            @Override
            public int compare(ListItem o1, ListItem o2) {
                return o1.getRelease().compareTo(o2.getRelease());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByArtist() {
        Collections.sort(listItems, new Comparator<ListItem>() {
            @Override
            public int compare(ListItem o1, ListItem o2) {
                return o1.getArtist().compareTo(o2.getArtist());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void sortByName() {
        Collections.sort(listItems, new Comparator<ListItem>() {
            @Override
            public int compare(ListItem o1, ListItem o2) {
                return o1.getHead().compareTo(o2.getHead());
            }
        });
        adapter.notifyDataSetChanged();
    }

    private void threeMethods(int engine) {
        listItems = new ArrayList<>();
        switch(engine){
            case 1:

                loadRecyclerViewData();

                break;
            case 2:

                loadRecyclerViewOfflineData();
                adapter();

                break;
            case 3:
                loadRecyclerViewOfflineData();
                loadRecyclerViewData();
        }
    }

    private void adapter() {
        adapter = new MyAdapter(listItems, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    private void loadRecyclerViewOfflineData() {
        Bundle bundle = getIntent().getExtras();
        String research = bundle.getString("search");

        try {
            JSONArray mainNode = new JSONArray(loadJSONFromRawData());
            for(int i=0;i<mainNode.length();i++){
                JSONObject eachObject = mainNode.getJSONObject(i);
                if(eachObject.getString("Song Clean").equalsIgnoreCase(research) ||
                        eachObject.getString("ARTIST CLEAN").equalsIgnoreCase(research) ||
                        eachObject.getString("Release Year").equalsIgnoreCase(research)
                        )
                {
                ListItem item = new ListItem(
                        eachObject.getString("Song Clean"),
                        eachObject.getString("ARTIST CLEAN"),
                        eachObject.getString("Release Year"),
                        eachObject.put("imageUrl", "none"));
                listItems.add(item);
            }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String loadJSONFromRawData() {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = getResources().openRawResource(R.raw.static_songlist);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }

            bufferedReader.close();
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void loadRecyclerViewData(){
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.loadData));
        progressDialog.show();

        Bundle bundle = getIntent().getExtras();
        String research = bundle.getString("search");
        String noSpacesResearch = research.replace(" ", "+");
        String urlOnline = "https://itunes.apple.com/search?term="+noSpacesResearch;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlOnline, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("results");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject o = jsonArray.getJSONObject(i);
                        ListItem item = new ListItem(
                                o.getString("trackName"),
                                o.getString("artistName"),
                                o.getString("releaseDate"),
                                o.getString("artworkUrl100"));
                        listItems.add(item);
                    }

                    adapter();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        );
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
