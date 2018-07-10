package com.whistledev.whistleplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.SearchView;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "MainActivity";

    //private ArrayList<String> titles = new ArrayList<>();
   // private ArrayList<String> artists = new ArrayList<>();
    private ArrayList<SongObject> songArray= new ArrayList<>();
    MainRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSongs();
        SearchView searchView = findViewById(R.id.mainSearchView);
        searchView.setOnQueryTextListener(this);
    }

    private void initSongs(){
        Log.d(TAG, "initSongs: started initing names and artisis");

        songArray.add(new SongObject("Bohemian Rhapsody","Queen"));
        songArray.add(new SongObject("Time","Pink Floyd"));
        songArray.add(new SongObject("Another One Bites The Dust","Queen"));
        songArray.add(new SongObject("Come As You Are","Nirvana"));
        songArray.add(new SongObject("Could You Be Loved","Bob Marley"));
        songArray.add(new SongObject("Billie Jean","Michael Jackson"));
        songArray.add(new SongObject("Hips Don't Lie","Shakira"));
        songArray.add(new SongObject("Paradise","Coldplay"));
        songArray.add(new SongObject("In The End","Linkin Park"));
        songArray.add(new SongObject("Love The Way You Lie","Eminem"));


     /**   titles.add("Bohemian Rhapsody");
        artists.add("Queen");
        titles.add("Time");
        artists.add("Pink Floyd");
        titles.add("Another One Bites The Dust");
        artists.add("Queen");
        titles.add("Come As You Are");
        artists.add("Nirvana");
        titles.add("Could You Be Loved");
        artists.add("Bob Marley");
        titles.add("Billie Jean");
        artists.add("Michael Jackson");
        titles.add("Hips Don't Lie");
        artists.add("Shakira");
        titles.add("Paradise");
        artists.add("Coldplay");
        titles.add("In The End");
        artists.add("Linkin Park");
        titles.add("Love The Way You Lie");
        artists.add("Eminem"); **/

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: Starting recyclerview");
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);
        adapter = new MainRecyclerViewAdapter(this,songArray);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        ArrayList<SongObject> filteredList = new ArrayList<>();

        for(SongObject s:songArray){
            if(s.title.toLowerCase().contains(query.toLowerCase()) || s.artist.toLowerCase().contains(query.toLowerCase())){
                filteredList.add(s);
            }
        }
        adapter.updateList(filteredList);

        return true;
    }
}
