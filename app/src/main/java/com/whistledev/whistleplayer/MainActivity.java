package com.whistledev.whistleplayer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private ArrayList<String> titles = new ArrayList<>();
    private ArrayList<String> artists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSongs();
    }

    private void initSongs(){
        Log.d(TAG, "initSongs: started initing names and artisis");

        titles.add("Bohemian Rhapsody");
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
        artists.add("Eminem");
        titles.add("Bohemian Rhapsody");
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
        artists.add("Eminem");

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: Starting recyclerview");
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);
        MainRecyclerViewAdapter adapter = new MainRecyclerViewAdapter(this,titles,artists);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
