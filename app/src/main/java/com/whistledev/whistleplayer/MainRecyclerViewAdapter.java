package com.whistledev.whistleplayer;

import android.content.Context;
import android.graphics.Color;
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
    private long selectedId = -1;


    public MainRecyclerViewAdapter(Context context, ArrayList<SongObject> songs){//ArrayList<String> title, ArrayList<String> artist) {
        this.context = context;
        this.songs= songs;
       // for(SongObject song : songs){
        //this.title.add(song.title);
       // this.artist.add(song.artist);
    }//}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_song_list_item,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
       // holder.itemView.setBackgroundColor(Color.parseColor("#000000"));
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int pos) {

        Log.d(TAG, "SELECTED_ID------>"+selectedId);
        long id = songs.get(pos).id;
        Log.d(TAG, "POS_ID------>"+id);
        if(id == selectedId) {
            holder.itemView.setSelected(true);//BackgroundColor(Color.parseColor("#000000"));
            ((TextView)((ViewGroup) holder.itemView).getChildAt(0)).setTextColor(Color.parseColor("#ffffff"));  //These lines and the lines in setSelected in MainActivity are same,So consider them into a seperate class/function
            ((TextView) ((ViewGroup) holder.itemView).getChildAt(1)).setTextColor(Color.parseColor("#ffffff"));
        }
        else {
            holder.itemView.setSelected(false);//BackgroundColor(Color.parseColor("#ffffff"));
            ((TextView) ((ViewGroup) holder.itemView).getChildAt(0)).setTextColor(Color.parseColor("#000000"));
            ((TextView) ((ViewGroup) holder.itemView).getChildAt(1)).setTextColor(Color.parseColor("#000000"));
        }

        holder.SongTitle.setText(songs.get(pos).title);
        Log.d(TAG, "ID----> "+id);
        holder.getView().setTag(id);//(pos);
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
        return songs.size();
    }

    public void updateList(ArrayList<SongObject> filteredList){

        songs = new ArrayList<>();
        songs.addAll(filteredList);
        notifyDataSetChanged();

    }

    public void setSelectedId(long id){

        selectedId = id;
    }


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
