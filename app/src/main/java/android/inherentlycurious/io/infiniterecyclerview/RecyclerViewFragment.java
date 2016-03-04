package android.inherentlycurious.io.infiniterecyclerview;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Himanshu Likhyani <himanshu.950@gmail.com>
 */
public class RecyclerViewFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private String[] data = new String[] {"January", "February", "March", "January", "February", "March", "January", "February", "March", "January", "February", "March" };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new RecyclerViewAdapter(getActivity(), R.layout.item_list_load_more, R.layout.item_empty_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_recycler_view, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.case_list_view);
        return root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private class RecyclerViewAdapter extends InfiniteRecyclerViewAdapter {

        public RecyclerViewAdapter(Context context, int moreResourceId, int emptyResourceId) {
            super(context, moreResourceId, emptyResourceId);
        }

        class ItemViewHolder extends RecyclerView.ViewHolder {
            private TextView year;

            public ItemViewHolder(View v) {
                super(v);
                year =(TextView) v.findViewById(R.id.year);
            }

        }

        @Override
        protected RecyclerView.ViewHolder getViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext())
                    .inflate(R.layout.item_recycler_view, parent, false);
            return new ItemViewHolder(view);
        }

        @Override
        protected void doBindViewHolder(RecyclerView.ViewHolder viewholder, int position) {
            ItemViewHolder holder = (ItemViewHolder) viewholder;
            holder.year.setText(data[position]);
        }

        @Override
        public int getViewType(int position) {
            return 0; // As of now just one view type, therefore returning 0
        }

        @Override
        protected void onMoreRequested() {
            Toast.makeText(getContext(), "OnMoreRequested", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected int getCount() {
            return data.length;
        }
    }
}
