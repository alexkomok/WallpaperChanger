package com.komok.itemtouchhelper.helper;

import java.util.List;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.komok.wallpaperchanger.R;
import com.komok.wallpaperchanger.Tile;


public class RecyclerListAdapter extends AbstractRecyclerListAdapter<Tile>
        implements ItemTouchHelperAdapter {



	public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener, List<Tile> selectedTilesList) {
		super(context, dragStartListener, selectedTilesList);
	}

	@Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
    	
    	final Tile tile = mTile.get(position);
    	
    	mViewOnClickListener = new OnClickListener() {
			public void onClick(View v) {

/*				WallpaperInfo wi = tile.mWallpaperInfo;
				if (wi != null && wi.getSettingsActivity() != null) {

					Intent intent = new Intent();
					intent.setComponent(new ComponentName(tile.mWallpaperInfo.getPackageName(), wi.getSettingsActivity()));
					v.getContext().startActivity(intent);

				} else*/

					Toast.makeText(v.getContext(), v.getContext().getString(R.string.no_settings), Toast.LENGTH_LONG).show();
			}
		};
    	
        holder.textView.setText(tile.mLabel);
        holder.holderView.setOnClickListener(mViewOnClickListener);
		holder.imageView.setImageDrawable(tile.mThumbnail);

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



}
