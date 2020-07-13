package com.example.mediarecorderapp;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AudioListViewAdapter extends RecyclerView.Adapter<AudioListViewAdapter.ViewHolder>  {

    private ArrayList<AudioFile> audiolist;

    public AudioListViewAdapter(ArrayList<AudioFile> audiolist) {
        this.audiolist = audiolist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.audioview,parent,false);

        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        AudioFile audioFile=audiolist.get(position);
        holder.filename.setText(audioFile.getName());
        holder.filelength.setText(Long.toString(audioFile.getLength()/60)+":"+Long.toString(audioFile.getLength()%60));
    }

    @Override
    public int getItemCount() {
        return audiolist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView filename,filelength;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.filename=itemView.findViewById(R.id.filename);
            this.filelength=itemView.findViewById(R.id.filelength);
        }
    }


}
