package com.n1technology.app.exampreparationnetwork.quiz;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.n1technology.app.exampreparationnetwork.R;
import com.n1technology.app.exampreparationnetwork.model.LeaderBoard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by RAKEZ on 12/8/2017.
 */

public class AdapterLeader extends RecyclerView.Adapter<AdapterLeader.ViewHolder> {
    private Context context;
    private List<LeaderBoard> leaderBoardList;

    public AdapterLeader(Context context, List<LeaderBoard> leaderBoardList) {
        this.context = context;
        this.leaderBoardList = leaderBoardList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_leader,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LeaderBoard leaderBoard = leaderBoardList.get(position);
        if(position==0){
            holder.leaderRank.setBackground(context.getResources().getDrawable(R.drawable.ic_leader_1st));
        }else if(position==1){
            holder.leaderRank.setBackground(context.getResources().getDrawable(R.drawable.ic_leader_2nd));
        }else if(position==2){
            holder.leaderRank.setBackground(context.getResources().getDrawable(R.drawable.ic_leader_3rd));
        }else{
            holder.leaderRank.setText("#"+(position+1));
        }
        Picasso.with(context).load(leaderBoard.getImageURL()).into(holder.leaderImage);
        holder.leaderName.setText(leaderBoard.getName());
        holder.leaderScore.setText(leaderBoard.getScore());
    }

    @Override
    public int getItemCount() {
        return leaderBoardList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.leader_rank) TextView leaderRank;
        @BindView(R.id.leader_image) ImageView leaderImage;
        @BindView(R.id.leader_name) TextView leaderName;
        @BindView(R.id.leader_score) TextView leaderScore;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Typeface custom_font = Typeface.createFromAsset(context.getAssets(),  "fonts/leaaderboard_name.otf");
            leaderName.setTypeface(custom_font);
        }
    }

    public void updateData(LinkedHashMap<String, String> name, LinkedHashMap<String, String> photoURL, LinkedHashMap<String, String> score){
        leaderBoardList.clear();
        List<String> keys = new ArrayList<String>(name.keySet());
        for(int i = 0 ; i < photoURL.size() ; i++){
            leaderBoardList.add(new LeaderBoard(name.get(keys.get(i)),photoURL.get(keys.get(i)),score.get(keys.get(i))));
        }
        notifyDataSetChanged();
    }
}
