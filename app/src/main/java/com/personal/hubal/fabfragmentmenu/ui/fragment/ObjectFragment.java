package com.personal.hubal.fabfragmentmenu.ui.fragment;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.personal.hubal.fabfragmentmenu.R;
import com.personal.hubal.fabfragmentmenu.adapter.ObjectAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubal on 1/11/2018.
 */

public class ObjectFragment extends Fragment implements ObjectAdapter.SelectObjectListener {

    private static final String TAG = "ObjectFragment";

    private static final String BUNDLE_CATEGORY = "BUNDLE_CATEGORY";

    private TextView mTitleTextView;
    private ImageView mBackButton;

    private String mCategory;

    private View.OnClickListener mOnBackClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getActivity().onBackPressed();
        }
    };
    private ObjectAdapter mObjectAdapter;
    private View.OnDragListener mOnDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_EXITED:
                    mObjectAdapter.stopDragAndDrop();
                    break;
            }
            return true;
        }
    };



    public static ObjectFragment newInstance(String category) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_CATEGORY, category);

        ObjectFragment fragment = new ObjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCategory = getArguments().getString(BUNDLE_CATEGORY);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_objects, container, false);

        initUI(view);
        initObjectListData();

        return view;
    }

    private void initUI(View view) {
        mTitleTextView = view.findViewById(R.id.titleTextView);
        mTitleTextView.setText(mCategory);

        mBackButton = view.findViewById(R.id.backButton);
        mBackButton.setOnClickListener(mOnBackClickListener);

        RecyclerView objectRecyclerView = view.findViewById(R.id.objectRecyclerView);

        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        objectRecyclerView.setLayoutManager(layoutManager);
        objectRecyclerView.setOnDragListener(mOnDragListener);

        mObjectAdapter = new ObjectAdapter(ObjectFragment.this);
        objectRecyclerView.setAdapter(mObjectAdapter);
    }

    private void initObjectListData() {
        //TODO test data
        List<Drawable> objectList = new ArrayList<>();
        objectList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_dialog_map));
        objectList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_btn_speak_now));
        objectList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_dialog_info));
        objectList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_input_delete));
        objectList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_lock_idle_low_battery));
        objectList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_lock_silent_mode));
        objectList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_lock_power_off));
        objectList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_lock_idle_alarm));


        mObjectAdapter.setObjectList(objectList);
    }

    @Override
    public void onObjectSelected(int position) {
        Log.d("SelectObjectListener", "ObjectSelected " + position);
    }
}
