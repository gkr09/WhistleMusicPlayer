package com.whistledev.whistleplayer;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>{

    private static final String TAG= "Recycler View Adapter";

    private Context context;
  //  private ArrayList<String> title = new ArrayList<>();
   // private ArrayList<String> artist = new ArrayList<>();
    private ArrayList<SongObject> songs;

    public MainRecyclerViewAdapter(Context context, ArrayList<SongObject> songs){//ArrayList<String> title, ArrayList<String> artist) {
        this.context = context;
        this.songs= songs;
        Collections.sort(this.songs);
       // for(SongObject song : songs){
        //this.title.add(song.title);
       // this.artist.add(song.artist);
    }//}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_song_list_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int pos) {

        Log.d(TAG, "onBindViewHolder: called.");

        holder.SongTitle.setText(songs.get(pos).title);
        Log.d(TAG, ": POS------>>."+pos);
        holder.getView().setTag(pos);
        Log.d(TAG, "onBindViewHolder: ADDED-->>"+songs.get(pos).title);
        holder.SongArtist.setText(songs.get(pos).artist);
     /**   holder.SongListItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Toast.makeText(context,songs.get(pos).title,Toast.LENGTH_SHORT).show();
            }
        });**/
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ITEMCOUNT HERE---->>>>"+songs.size());
        return songs.size();
    }

    public void updateList(ArrayList<SongObject> filteredList){

        songs = new ArrayList<>();
        songs.addAll(filteredList);
        notifyDataSetChanged();

    }

   // public void removeSong(int pos){
     //   songs.remove(pos);
     //   notifyDataSetChanged();
    //}

    public class ViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout SongListItem;
        TextView SongTitle;
        TextView SongArtist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            SongListItem = itemView.findViewById(R.id.mainSongListItem);
            SongTitle = itemView.findViewById(R.id.mainSongListTitle);
            SongArtist = itemView.findViewById(R.id.mainSongListArtist);
            itemView.setOnLongClickListener(new DragTouchListener());

        }
        public ConstraintLayout getView(){
            return SongListItem;
        }
    }
}
