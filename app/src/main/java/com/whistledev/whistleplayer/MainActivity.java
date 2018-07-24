package com.whistledev.whistleplayer;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "MainActivity";

    //private ArrayList<String> titles = new ArrayList<>();
    // private ArrayList<String> artists = new ArrayList<>();
    private ArrayList<SongObject> songArray;//= new ArrayList<>();
    MainRecyclerViewAdapter adapter;
    ImageButton queueButton;
    MusicLoader loader;
    RecyclerView recyclerView;
    ImageButton playButton;
    TextView currentLabel;
    SearchView searchView;
    ViewGroup controlsLayout;
    ViewGroup searchLayout;

    Animation searchViewIn,searchViewOut;

    private MusicPlayerService musicSrv;
    private Intent playIntent;
    private boolean musicBound = false;
    private boolean isPlaying = false;      //  <--------Replace with function in MediaPlayer isPlaying()
    public boolean shuffle=false;

    private int selectedId = -1;

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout topLayout = findViewById(R.id.topLayout);
        initSongs();
        searchView = findViewById(R.id.mainSearchView);
        searchView.setOnQueryTextListener(this);

        searchViewIn = AnimationUtils.loadAnimation(this, R.anim.search_view_in_animation);
        searchViewOut = AnimationUtils.loadAnimation(this, R.anim.search_view_out_animation);

        queueButton = findViewById(R.id.queueButton);
        queueButton.setOnDragListener(new DragListener());//<----HERE

        boolean isLeftToRight= TextUtils.getLayoutDirectionFromLocale(Locale.getDefault())==View.LAYOUT_DIRECTION_LTR;

        controlsLayout = findViewById(R.id.controlsLayout);
        controlsLayout.bringToFront();
        searchLayout = findViewById(R.id.searchLayout);
        searchLayout.bringToFront();
        searchView.bringToFront();
        if(isLeftToRight){
             searchView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
             searchView.setTextDirection(View.TEXT_DIRECTION_LTR);}
        else{
            searchView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            searchView.setTextDirection(View.TEXT_DIRECTION_RTL);}




        playButton = findViewById(R.id.playButton);
        currentLabel = findViewById(R.id.currentTrackLabel);
    }


    @Override
    protected void onStart() {
        Log.d(TAG, "onStart: called <-----");
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicPlayerService.class);
            bindService(playIntent, musicConnection, this.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder) service;
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

    private void initSongs() {
        Log.d(TAG, "initSongs: started initing names and artisis");
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            if (checkPermissionForReadExternalStorage()) {
                Log.d(TAG, "PERMISSION ---->: " + checkPermissionForReadExternalStorage());
                loader = new MusicLoader(this);
                songArray = loader.getSongList();
            } else
                requestPermissionForReadExternalStorage();
        else {
            loader = new MusicLoader(this);
            songArray = loader.getSongList();
        }
        Collections.sort(songArray);

        initRecyclerView();
    }
    static int y;

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: Starting recyclerview");
        recyclerView = findViewById(R.id.mainRecyclerView);

    /**    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                 super.onScrolled(recyclerView, dx, dy);
                 y = dy;
                 if(dy>0) {

                     searchView.startAnimation(searchViewOut);
                     searchView.setVisibility(View.GONE);
                 }
               //  else
               //      searchView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
              //  if(recyclerView.SCROLL_STATE_DRAGGING==newState){

                //    if(y>0) {
                  //      searchView.setVisibility(View.VISIBLE);
                    //    searchView.startAnimation(searchViewIn);
                    //}
                  //  else{
                    //    searchView.startAnimation(searchViewOut);
                      //  searchView.setVisibility(View.GONE);
                    //}
               // }
                if(recyclerView.SCROLL_STATE_IDLE==newState){
                    if(y>0){

                    // fragProductLl.setVisibility(View.VISIBLE);
                  //  if(y<0){   //<=
                        searchView.setVisibility(View.VISIBLE);
                        searchView.startAnimation(searchViewIn);
                    }
                    else{
                      //  y=0;
                       // searchView.startAnimation(searchViewOut);
                        searchView.setVisibility(View.GONE);
                    //}
                }
            }
        });**/



        boolean isLeftToRight= TextUtils.getLayoutDirectionFromLocale(Locale.getDefault())==View.LAYOUT_DIRECTION_LTR;

        if(isLeftToRight)
            recyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        else
            recyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);

        adapter = new MainRecyclerViewAdapter(this, songArray);
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
                removeSong(viewHolder.getAdapterPosition());
                //songArray.remove(viewHolder.getAdapterPosition());
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

        for (SongObject s : songArray) {
            if (s.title.toLowerCase().contains(query.toLowerCase()) || s.artist.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(s);
            }
        }
        adapter.updateList(filteredList);
        // musicSrv.setList(filteredList);

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
                Toast.makeText(this, "Uh Oh.....Bye Bye !!", Toast.LENGTH_SHORT).show();
                // Permission request was denied.
                //   Snackbar.make(mLayout, R.string.camera_permission_denied,
                //         Snackbar.LENGTH_SHORT)
                //       .show();
            }
        }
        // END_INCLUDE(onRequestPermissionsResult)
    }

    public void songPicked(View view) {
        this.selectedId = Integer.parseInt(view.getTag().toString());
        musicSrv.setSong(selectedId);
        musicSrv.playSong();
        this.isPlaying = true;
        playButton.setImageResource(R.drawable.pause_icon);
        adapter.setSelectedId(selectedId);
        adapter.notifyDataSetChanged();
        changeCurrentTitle(selectedId);
    }

    public void changeCurrentTitle(long id){
        for(SongObject x:songArray)
            if(x.equals(id))
                currentLabel.setText(x.title);

    }

    public void nextSong(View v){
        int index = 0;
        long id = 0;
        try{
        if(this.selectedId!=-1)
            for(SongObject x:songArray)
                if(x.equals(this.selectedId)){
                    index = songArray.indexOf(x)+1;
                    id = songArray.get(index).id;
                    musicSrv.setSong((int)id);
                    musicSrv.playSong();
                    this.isPlaying = true;
                    playButton.setImageResource(R.drawable.pause_icon);
                    adapter.setSelectedId(id);
                    adapter.notifyDataSetChanged();
                    recyclerView.getLayoutManager().scrollToPosition(index+1);
                    changeCurrentTitle(id);
                    selectedId = (int)id;
                    return;
    }}
    catch (ArrayIndexOutOfBoundsException e){
        Log.d(TAG, "nextSong: LIST END REACHED");
    }
        
        }

    public void prevSong(View v){
        int index = 0;
        long id = 0;
        try{
        if(this.selectedId!=-1)
            for(SongObject x:songArray)
                if(x.equals(this.selectedId)){
                    index = songArray.indexOf(x)-1;
                    id = songArray.get(index).id;
                    musicSrv.setSong((int)id);
                    musicSrv.playSong();
                    this.isPlaying = true;
                    playButton.setImageResource(R.drawable.pause_icon);
                    adapter.setSelectedId(id);
                    adapter.notifyDataSetChanged();
                    recyclerView.getLayoutManager().scrollToPosition(index-1);
                    changeCurrentTitle(id);
                    selectedId = (int)id;
                    return;
                }}
        catch(ArrayIndexOutOfBoundsException e){
            Log.d(TAG, "prevSong: LIST END REACHED");
            }
        }

    private void removeSong(int i) {
        long id = Integer.parseInt(recyclerView.findViewHolderForLayoutPosition(i).itemView.getTag().toString());
        for (int x = 0; x < songArray.size(); x++)
            if (songArray.get(x).id == id) {
                songArray.remove(x);
                Snackbar.make(recyclerView, "Removed " + songArray.get(x).title, Snackbar.LENGTH_SHORT).show();
            }

    }

    public void playPause(View v){

        if(isPlaying) {
            musicSrv.pauseSong();
            isPlaying = false;
            playButton.setImageResource(R.drawable.play_icon);
        }else{
            musicSrv.resumeSong();
            isPlaying = true;
            playButton.setImageResource(R.drawable.pause_icon);
        }


    }

    @Override
    public void onBackPressed(){
      /**  View v = this.getCurrentFocus();
        if(v!=null) {
            InputMethodManager im = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }**/
        if(!searchView.isIconified())
            searchView.setIconified(true);
        else
            super.onBackPressed();
        }



    @Override
    protected void onDestroy() {
        stopService(playIntent);
        musicSrv = null;
        unbindService(musicConnection);
        super.onDestroy();
    }
}