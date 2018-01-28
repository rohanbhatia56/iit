package homeautomation.circularblue.com.iit;

import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;


/**
 * Created by rohan on 26-01-2018.
 */

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {

    private ArrayList<PathsInfo> mDataset;
    private String path;
    public MainAdapter(ArrayList<PathsInfo> mDataset) {
        this.mDataset = mDataset;
        path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + "/Vid.mp4";
    }

    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v =  LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MainAdapter.ViewHolder holder, final int position) {

        holder.position = position;
        PathsInfo pathsInfo = Singleton.getInstance().getVideoPaths().get(position);
        Bitmap thumb = ThumbnailUtils.createVideoThumbnail(pathsInfo.videoPath, MediaStore.Images.Thumbnails.MINI_KIND);
//        if(pathsInfo.type == PathType.IMAGE){
//
//            holder.thumb.setImageURI(Uri.parse(pathsInfo.imagePath));
//        }else {
            holder.thumb.setImageBitmap(thumb);
     //   }
        holder.play_image.setVisibility(View.VISIBLE);
        holder.replay_image.setVisibility(View.GONE);
        holder.play_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                holder.mVideoView.start();
                holder.play_image.setVisibility(View.GONE);
                holder.thumb.setVisibility(View.GONE);
            }
        });
//        holder.replay_image.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.mVideoView.start();
//                holder.replay_image.setVisibility(View.GONE);
//            }
//        });
//        holder.mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mediaPlayer) {
//                holder.replay_image.setVisibility(View.VISIBLE);
//            }
//        });
//
//
//        holder.mVideoView.setVideoURI(Singleton.getInstance().getVideoURIs().get(position));
//        holder.mVideoView.seekTo(10);
//        holder.mVideoView.setOnClickListener(new View.OnClickListener() {
//                                                 @Override
//                                                 public void onClick(View view) {
//                                                     Log.d("debug","position = " + position );
//                                                 }
//                                             }
//        );
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
    public class ViewHolder extends  RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextureView mVideoView;
        public ImageView play_image,replay_image,thumb;
        private MediaPlayer mediaPlayer;
        Surface surface;
        int position;

        public ViewHolder(final View itemView) {
            super(itemView);
            play_image = (ImageView) itemView.findViewById(R.id.play_icon);
            replay_image = (ImageView) itemView.findViewById(R.id.replay_icon);
            mTitle  = (TextView) itemView.findViewById(R.id.title);
            mVideoView = (TextureView) itemView.findViewById(R.id.videoview);
            thumb = (ImageView) itemView.findViewById(R.id.thumb);
            mediaPlayer =  new MediaPlayer();


            mVideoView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                    surface = new Surface(surfaceTexture);

//                    try {
//                        mediaPlayer.setDataSource(afd);
                    try {
                        Log.d("debug","POS = " + position + " "+ Singleton.getInstance().getVideoPaths().get(position));
                        mediaPlayer.setDataSource(Singleton.getInstance().getVideoPaths().get(position).videoPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setSurface(surface);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.seekTo(100);
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                            @Override
                            public void onPrepared(MediaPlayer mediaPlayer) {
//                                mediaPlayer.start();
                            }
                        });
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                }

                @Override
                public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

                }

                @Override
                public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                    mediaPlayer.reset();
                    return false;
                }

                @Override
                public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

                }
            });

            mVideoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if(mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        return;
                    }

                    try {
                        mediaPlayer.reset();
                        mediaPlayer.setDataSource(Singleton.getInstance().getVideoPaths().get(position).videoPath);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaPlayer.start();
                }
            });

        }

    }
}
