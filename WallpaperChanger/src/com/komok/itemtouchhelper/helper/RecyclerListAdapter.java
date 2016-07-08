package com.komok.itemtouchhelper.helper;

import java.util.Collections;
import java.util.List;

import android.app.WallpaperInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.komok.wallpaperchanger.R;
import com.komok.wallpaperchanger.WallpaperTile;


public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private List<WallpaperTile> mWallpapers;
    private final PackageManager mPackageManager;
    private final OnStartDragListener mDragStartListener;
    private OnClickListener mViewOnClickListener;

    public RecyclerListAdapter(final Context context, OnStartDragListener dragStartListener, List<WallpaperTile> selectedTilesList) {
        mDragStartListener = dragStartListener;
        mWallpapers = selectedTilesList;
        mPackageManager = context.getPackageManager();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_wallpaper, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

	@Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
    	
    	final WallpaperTile wt = mWallpapers.get(position);
    	
    	mViewOnClickListener = new OnClickListener() {
			public void onClick(View v) {

				WallpaperInfo wi = wt.mWallpaperInfo;
				if (wi != null && wi.getSettingsActivity() != null) {

					Intent intent = new Intent();
					intent.setComponent(new ComponentName(wt.mWallpaperInfo.getPackageName(), wi.getSettingsActivity()));
					v.getContext().startActivity(intent);

				} else

					Toast.makeText(v.getContext(), v.getContext().getString(R.string.no_settings), Toast.LENGTH_LONG).show();
			}
		};
    	
        holder.textView.setText(wt.mWallpaperInfo.loadLabel(mPackageManager));
        holder.holderView.setOnClickListener(mViewOnClickListener);
        
		if (wt.mThumbnail != null) {
			holder.imageView.setImageDrawable(wt.mThumbnail);
			holder.iconView.setVisibility(View.GONE);
		} else {
			holder.iconView.setImageDrawable(wt.mWallpaperInfo.loadIcon(mPackageManager));
			holder.iconView.setVisibility(View.VISIBLE);
		}

        // Start a drag whenever the handle view it touched
        holder.imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
        
    }

    @Override
    public void onItemDismiss(int position) {
    	mWallpapers.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mWallpapers, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mWallpapers.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public final ImageView imageView;
        public final ImageView iconView;
        public final CheckBox checkBox;
        public final View holderView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            holderView = itemView;
            textView = (TextView) itemView.findViewById(R.id.wallpaper_item_label);
            imageView = (ImageView) itemView.findViewById(R.id.wallpaper_image);
            iconView = (ImageView) itemView.findViewById(R.id.wallpaper_icon);
            checkBox = (CheckBox) itemView.findViewById(R.id.myCheckBox);
            checkBox.setVisibility(View.GONE);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }
}
