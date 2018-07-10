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

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder>{

    private static final String TAG= "Recycler View Adapter";

    private Context context;
    private ArrayList<String> title = new ArrayList<>();
    private ArrayList<String> artist = new ArrayList<>();

    public MainRecyclerViewAdapter(Context context, ArrayList<String> title, ArrayList<String> artist) {
        this.context = context;
        this.title = title;
        this.artist = artist;
    }

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

        holder.SongTitle.setText(title.get(pos));
        Log.d(TAG, "onBindViewHolder: ADDED-->>"+title.get(pos));
        holder.SongArtist.setText(artist.get(pos));

        holder.SongListItem.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d(TAG, "onClick: CLICKED ON--->>>: " + artist.get(pos));
                Toast.makeText(context,title.get(pos),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ITEMCOUNT HERE---->>>>"+title.size());
        return title.size();
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
        }
    }
}
