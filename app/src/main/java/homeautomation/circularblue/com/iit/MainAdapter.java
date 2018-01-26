package homeautomation.circularblue.com.iit;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

/**
 * Created by rohan on 26-01-2018.
 */

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<Uri> mDataset;
    public MainAdapter(ArrayList<Uri> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MainAdapter.ViewHolder holder, final int position) {

        holder.play_image.setVisibility(View.VISIBLE);
        holder.replay_image.setVisibility(View.GONE);
        holder.play_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mVideoView.start();
                holder.play_image.setVisibility(View.GONE);
            }
        });
        holder.replay_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.mVideoView.start();
                holder.replay_image.setVisibility(View.GONE);
            }
        });
        holder.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                holder.replay_image.setVisibility(View.VISIBLE);
            }
        });


        holder.mVideoView.setVideoURI(Singleton.getInstance().getVideoURIs().get(position));
        holder.mVideoView.seekTo(10);
        holder.mVideoView.setOnClickListener(new View.OnClickListener() {
                                                 @Override
                                                 public void onClick(View view) {
                                                     Log.d("debug","position = " + position );
                                                 }
                                             }
        );
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView mTitle;
        public VideoView mVideoView;
        public ImageView play_image,replay_image;
        public ViewHolder(View itemView) {
            super(itemView);
            play_image = (ImageView) itemView.findViewById(R.id.play_icon);
            replay_image = (ImageView) itemView.findViewById(R.id.replay_icon);
            mTitle  = (TextView) itemView.findViewById(R.id.title);
            mVideoView = (VideoView) itemView.findViewById(R.id.videoview);
        }
    }
}
