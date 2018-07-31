package com.whistledev.whistleplayer;

import com.whistledev.whistleplayer.adapters.MainRecyclerViewAdapter;
import com.whistledev.whistleplayer.fragments.ControllerFragment;
import com.whistledev.whistleplayer.listeners.SeekBarListener;
import com.whistledev.whistleplayer.models.SongObject;
import com.whistledev.whistleplayer.services.MusicPlayerService;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.View;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class MainScreen extends AppCompatActivity implements SearchView.OnQueryTextListener,ActivityCompat.OnRequestPermissionsResultCallback,MediaPlayer.OnPreparedListener,MediaPlayer.OnCompletionListener {

    private ArrayList<SongObject> songArray;
    private MainRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private SeekBar seekBar;
    private long selectedId = -1;
    private SearchView searchView;
    private boolean isPlaying = false;
    private boolean shuffle = false ;

    private Intent playIntent;
    private MusicPlayerService musicSrv;
    private boolean musicBound = false;
    ControllerFragment controllerview;
    MusicController controller;

    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 0;

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MusicPlayerService.MusicBinder binder = (MusicPlayerService.MusicBinder) service;

            musicSrv = binder.getService();  // Get the service
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.mainSearchView);
        recyclerView = findViewById(R.id.mainRecyclerView);
        seekBar = findViewById(R.id.seekBar);

        searchView.setOnQueryTextListener(this);

        manageLayoutDirection();

        controllerview = (ControllerFragment) getSupportFragmentManager().findFragmentById(R.id.controllerLayout);
        controller = new MusicController();

        initSongs();
    }

    @Override
    protected void onStart() {

        super.onStart();

        if (playIntent == null) {

            playIntent = new Intent(this, MusicPlayerService.class);
            bindService(playIntent, musicConnection, this.BIND_AUTO_CREATE);

            startService(playIntent);
        }
    }

    private void manageLayoutDirection() {
        // Switches the search view and recycler view's layout direction

        final boolean isLeftToRight = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == View.LAYOUT_DIRECTION_LTR;

        if (isLeftToRight) {

            searchView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            searchView.setTextDirection(View.TEXT_DIRECTION_LTR);           // But Text to remain LTR

            recyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);     // For LTR locales scrollbar should go to left
        } else {

            searchView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);       // For RTL locales search view goes to left
            searchView.setTextDirection(View.TEXT_DIRECTION_RTL);           // Text changes to RTL

            recyclerView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);     // Scrollbar to right for RTL locales
        }
    }

    private boolean checkPermissions() {
        // Checks if the app has permission to access storage, in android 6.0 and above.

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int result = this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    private void requestPermissions() {
        // Requests permission for external storage.

        try {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {

            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Handle stuff if permissions are granted or otherwise.

        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {

            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                initSongs();

            else
                Toast.makeText(this, "Required Permissions were not granted !", Toast.LENGTH_SHORT).show();
        }
    }

    private void initSongs() {
        // Initialise and populate songArray

        MusicLoader loader;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {  // Marshmallow or higher

            if (!checkPermissions())
                requestPermissions();
            else {

                loader = new MusicLoader(this);
                songArray = loader.getSongList();
            }
        }
        else {                                                           // Lower than Marshmallow

            loader = new MusicLoader(this);
            songArray = loader.getSongList();
        }

        Collections.sort(songArray);
        controller.setSongList(songArray);
        initRecyclerView();
    }

    private void initRecyclerView() {
        // Set the adapter and other properties of the main recycler view

        adapter = new MainRecyclerViewAdapter(this, songArray);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createItemTouchHelper());    // To enable swipe to delete in the recycler view
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private ItemTouchHelper.Callback createItemTouchHelper() {
        // Implement swipe to delete

        ItemTouchHelper.Callback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                removeSong(viewHolder.getAdapterPosition());
                adapter.updateList(songArray);
            }
        };
        return simpleCallback;
    }

    private void startPlay(long id) {
        // Some stuff to set always when a new song is selected.

        this.selectedId = id;

        isPlaying = false;

        musicSrv.setSong(this.selectedId);
        musicSrv.playSong();

        controller.setCurrentSong(this.selectedId);        // Ideally this is only needed for songs picked from the recyclerview.

        controllerview.setPlaying();
        controllerview.setTitle(controller.getTitle());

        isPlaying = true;

        initSeekBar();
        setSelected();
    }

    public void songPicked(View view) {
        // When an item in recycler view has been selected.

        musicSrv.getPlayer().setOnPreparedListener(this);       // Find a new position for this.
        musicSrv.getPlayer().setOnCompletionListener(this);

        startPlay(Integer.parseInt(view.getTag().toString()));
    }

    public void playPause(View v) {

        musicSrv.pauseSong(isPlaying);

        if(isPlaying) {

            controllerview.setPaused();
            isPlaying = false;
        }
        else {

            controllerview.setPlaying();
            isPlaying = true;
        }
    }

    public void nextSong(View v) {

        boolean prev = false;                  // Next Song will be returned by controller.

        if(v != null && v.getId() == R.id.prevButton)
            prev = true;                      // Previous Song will be returned by controller.

        long id = controller.next(this.shuffle,prev);

        startPlay(id);

        this.recyclerView.scrollToPosition(controller.getIndex());
    }

    public void shuffleSwitch(View v) {

        if(shuffle)
            shuffle = false;
        else
            shuffle = true;
    }

    private void setSelected() {
        // Sets the selected item in the recyclerview which will be highlighted.

        adapter.setSelectedId(selectedId);
        adapter.notifyDataSetChanged();
    }

    private void removeSong(int i) {
        // Removes the song swiped off from the recyclerview from songArray

        long id = Integer.parseInt(recyclerView.findViewHolderForLayoutPosition(i).itemView.getTag().toString());

        for (int x = 0; x < songArray.size(); x++)
            if (songArray.get(x).getId() == id) {

                Snackbar.make(recyclerView, "Removed " + songArray.get(x).getTitle(), Snackbar.LENGTH_SHORT).show();   // Make a notification to the user and provide an undo action.
                songArray.remove(x);
            }

    }

    private void initSeekBar() {
        // Starts and updates the seekbar.

        final Handler mHandler = new Handler();

        Runnable progressTrack = new Runnable() {

            @Override
            public void run() {

                if(musicSrv.getPlayer() != null){

                    int mCurrentPosition = musicSrv.getPlayer().getCurrentPosition() / 1000;
                    seekBar.setProgress(mCurrentPosition);
                }
                mHandler.postDelayed(this, 1000);          // Update progress every 1000 ms
            }
        };
        mHandler.postDelayed(progressTrack, 100);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        // Passes a new list to recycler view which contains songs matching the query text

        ArrayList<SongObject> filteredList = new ArrayList<>();

        for (SongObject s : songArray) {

            if (s.getTitle().toLowerCase().contains(query.toLowerCase()) || s.getArtist().toLowerCase().contains(query.toLowerCase()))
                filteredList.add(s);
        }
        adapter.updateList(filteredList);

        return true;
    }

    @Override
    public void onPrepared(final MediaPlayer player) {

        seekBar.setMax(player.getDuration()/1000);       // Get duration only possible after MediaPlayer has prepared.

        SeekBarListener seeklistener = new SeekBarListener(player);
        seekBar.setOnSeekBarChangeListener(seeklistener);

        player.start();
    }

    @Override
    public void onCompletion(MediaPlayer player) {

        this.nextSong(null);
    }

    @Override
    public void onBackPressed() {
        // Search view should close on pressing back button instead of exiting the activity.

        if (!searchView.isIconified())
            searchView.setIconified(true);
        else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        // Stop and unbind the music service before destroying the activity.

        stopService(playIntent);
        musicSrv = null;
        unbindService(musicConnection);

        super.onDestroy();
    }
}
