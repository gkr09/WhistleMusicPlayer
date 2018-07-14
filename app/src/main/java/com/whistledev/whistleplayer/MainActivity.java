package com.whistledev.whistleplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,ActivityCompat.OnRequestPermissionsResultCallback{

    private static final String TAG = "MainActivity";

    //private ArrayList<String> titles = new ArrayList<>();
   // private ArrayList<String> artists = new ArrayList<>();
    private ArrayList<SongObject> songArray;//= new ArrayList<>();
    MainRecyclerViewAdapter adapter;
    ImageButton queueButton;
    MusicLoader loader;

    private MusicPlayerService musicSrv;
    private Intent playIntent;
    private boolean musicBound=false;

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initSongs();
        SearchView searchView = findViewById(R.id.mainSearchView);
        searchView.setOnQueryTextListener(this);
        queueButton = findViewById(R.id.queueButton);
        queueButton.setOnDragListener(new DragListener());//<----HERE
    }


    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: called <-----");
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, MusicPlayerService.class);
            bindService(playIntent, musicConnection, this.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder)service;
            //get service
            musicSrv = binder.getService();
            Log.d(TAG, "onServiceConnected: called <------");
            //pass list
            musicSrv.setList(songArray);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void initSongs(){
        Log.d(TAG, "initSongs: started initing names and artisis");
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        if(checkPermissionForReadExternalStorage()) {
            Log.d(TAG, "PERMISSION ---->: "+checkPermissionForReadExternalStorage());
            loader = new MusicLoader(this);
            songArray = loader.getSongList();
        }else
            requestPermissionForReadExternalStorage();
        else {
            loader = new MusicLoader(this);
            songArray = loader.getSongList();
        }
        /**
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
        artists.add("Eminem"); **/

        initRecyclerView();
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: Starting recyclerview");
        RecyclerView recyclerView = findViewById(R.id.mainRecyclerView);
        adapter = new MainRecyclerViewAdapter(this,songArray);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Swipe to Remove
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createItemTouchHelper());
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private ItemTouchHelper.Callback createItemTouchHelper() {
        ItemTouchHelper.Callback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
               // adapter.removeSong(viewHolder.getAdapterPosition());
                songArray.remove(viewHolder.getAdapterPosition());
                adapter.updateList(songArray);
            }
        };
    return simpleCallback;
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
        musicSrv.setList(filteredList);

        return true;
    }

    public boolean checkPermissionForReadExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForReadExternalStorage() {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
             //   Snackbar.make(mLayout, R.string.camera_permission_granted,
               //         Snackbar.LENGTH_SHORT)
                 //       .show();
                initSongs();
            } else {
                Toast.makeText(this,"Uh Oh.....Bye Bye !!", Toast.LENGTH_SHORT).show();
                // Permission request was denied.
             //   Snackbar.make(mLayout, R.string.camera_permission_denied,
               //         Snackbar.LENGTH_SHORT)
                 //       .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    public void songPicked(View view){
        if(view instanceof ConstraintLayout)
            Log.d(TAG, "YESssssssssssssssssssssssssssssssss --> "+view.getTag().toString());
        musicSrv.setSong(Integer.parseInt(view.getTag().toString()));
        musicSrv.playSong();
    }

}
