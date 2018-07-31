package com.whistledev.whistleplayer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whistledev.whistleplayer.listeners.DragTouchListener;
import com.whistledev.whistleplayer.R;
import com.whistledev.whistleplayer.models.SongObject;

import java.util.ArrayList;

public class MainRecyclerViewAdapter extends RecyclerView.Adapter<MainRecyclerViewAdapter.ViewHolder> {
    /** The adapter class for the main song list.
     *  The adapter also sets the background of the selected song. **/

    private ArrayList<SongObject> songs;
    private long selectedId = -1;

    private Context context;


    public MainRecyclerViewAdapter(Context context, ArrayList<SongObject> songs) {

        this.context = context;
        this.songs= songs;
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

        long id = songs.get(pos).getId();

        /** Checks if the item is the current selected one and if so change it's background and text colour accordingly.
         *  If not, revert the colours back to default colours.*/

        if(id == selectedId) {

            holder.itemView.setSelected(true);
            ((TextView)((ViewGroup) holder.itemView).getChildAt(0)).setTextColor(Color.parseColor("#ffffff"));
            ((TextView) ((ViewGroup) holder.itemView).getChildAt(1)).setTextColor(Color.parseColor("#ffffff"));
        }
        else {

            holder.itemView.setSelected(false);
            ((TextView) ((ViewGroup) holder.itemView).getChildAt(0)).setTextColor(Color.parseColor("#000000"));
            ((TextView) ((ViewGroup) holder.itemView).getChildAt(1)).setTextColor(Color.parseColor("#000000"));
        }

        holder.SongTitle.setText(songs.get(pos).getTitle());
        holder.SongArtist.setText(songs.get(pos).getArtist());

        // Sets the ID as the tag of the item. This helps in identifying the song when it is selected from the main list.

        holder.getView().setTag(id);
    }

    @Override
    public int getItemCount() {

        return songs.size();
    }

    public void updateList(ArrayList<SongObject> filteredList) {
        // Updates the list when the search view is used.

        songs = new ArrayList<>();
        songs.addAll(filteredList);

        notifyDataSetChanged();

    }

    public void setSelectedId(long id) {

        selectedId = id;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

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
        public ConstraintLayout getView() {
            // To set the tag on the ViewHolder item. See onBindViewHolder above.

            return SongListItem;
        }
    }
}
