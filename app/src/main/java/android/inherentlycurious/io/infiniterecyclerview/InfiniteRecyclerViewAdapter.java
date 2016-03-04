package android.inherentlycurious.io.infiniterecyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A RecyclerView adapter that shows a progress bar while automatically loading more items in the
 * background as the user reaches the end of the list
 *
 * @author Himanshu Likhyani <himanshu.950@gmail.com>
 */
public abstract class InfiniteRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int MODE_LIST = 1;
    public static final int MODE_PROGRESS = 2;
    public static final int MODE_EMPTY = 3;

    private static final int VIEW_TYPE_LOAD_MORE = -1; // Assuming there is a single view type default getItemViewType() implementation returns 0
    private static final int VIEW_TYPE_EMPTY = -2;

    private int mMoreResourceId;
    private int mEmptyResourceId;
    private boolean mHasMore;
    private LayoutInflater mInflater;
    private int mCurrentMode = MODE_LIST; // This is the default mode in which it shows the list

    public InfiniteRecyclerViewAdapter(Context context, int moreResourceId, int emptyResourceId) {
        mInflater = LayoutInflater.from(context);
        mMoreResourceId = moreResourceId;
        mEmptyResourceId = emptyResourceId;
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE) {
            View moreView = mInflater.inflate(mMoreResourceId, parent, false);
            return new RecyclerView.ViewHolder(moreView) {};
        }
        else if (viewType == VIEW_TYPE_EMPTY){
            View emptyView = mInflater.inflate(mEmptyResourceId, parent, false);
            emptyView.setMinimumHeight(parent.getHeight()); // This is required else the height o
            return new RecyclerView.ViewHolder(emptyView) {};
        }
        else {
            return getViewHolder(parent, viewType);
        }
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == VIEW_TYPE_LOAD_MORE) {
            onMoreRequested();
        } else if (mCurrentMode == MODE_LIST){ // Only calling this when in list mode
            doBindViewHolder(holder, position);
        }
    }

    @Override
    public final int getItemCount() {
        if (mCurrentMode == MODE_EMPTY || mCurrentMode == MODE_PROGRESS) {
            return 1;
        }

        if (mHasMore) {
            return getCount() + 1;
        }

        return getCount();
    }

    @Override
    public final int getItemViewType(int position) {
        if (mCurrentMode == MODE_EMPTY) {
            return VIEW_TYPE_EMPTY;
        }

        if (position == getCount() && mHasMore) {
            return(VIEW_TYPE_LOAD_MORE);
        }

        return getViewType(position);
    }

    public void notifyDataSetChanged(int mode) {
        mCurrentMode = mode;
        notifyDataSetChanged();
    }

    /**
     * Informs the adapter if there are more items to be loaded
     */
    public void setHasMore(boolean hasMore) {
        this.mHasMore = hasMore;
    }

    /**
     * Implement this to return your specialized view type
     */
    protected abstract int getViewType(int position);

    /**
     * Implement this to return your specialized ViewHolder
     */
    protected abstract RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType);

    /**
     * Implement this to specify your ViewHolder bindings
     */
    protected abstract void doBindViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * Implement this to return the number of items represented by this adapter
     */
    protected abstract int getCount();

    /**
     * Called when adapter has reached the end of the data set.
     * Any actions should be performed in a separate thread.
     */
    protected abstract void onMoreRequested();
}
