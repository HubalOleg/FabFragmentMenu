package com.personal.hubal.fabfragmentmenu.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.personal.hubal.fabfragmentmenu.R;
import com.personal.hubal.fabfragmentmenu.adapter.CategoryAdapter;
import com.personal.hubal.fabfragmentmenu.adapter.ObjectAdapter;
import com.personal.hubal.fabfragmentmenu.listener.CategoryTransitionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubal on 1/11/2018.
 */

public class CategoryFragment extends Fragment implements CategoryAdapter.SelectCategoryListener {

    private ConstraintLayout mContainerLayout;
    private CategoryAdapter mCategoryAdapter;
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

    public static CategoryFragment newInstance() {
        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        initUI(view);

        initCategoryListData();
        initObjectListData();

        return view;
    }

    private void initUI(View view) {
        mContainerLayout = view.findViewById(R.id.categoryFragmentContainer);
        mContainerLayout.setOnDragListener(mOnDragListener);

        // Category RecyclerView
        RecyclerView categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);

        LinearLayoutManager categoryLayoutManager = new LinearLayoutManager(getContext());
        categoryLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(categoryLayoutManager);

        mCategoryAdapter = new CategoryAdapter(CategoryFragment.this);
        categoryRecyclerView.setAdapter(mCategoryAdapter);

        // Object RecyclerView
        RecyclerView objectRecyclerView = view.findViewById(R.id.objectRecyclerView);

        LinearLayoutManager objectLayoutManager = new LinearLayoutManager(getContext());
        objectLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        objectRecyclerView.setLayoutManager(objectLayoutManager);

        mObjectAdapter = new ObjectAdapter();
        objectRecyclerView.setAdapter(mObjectAdapter);
    }

    private void initCategoryListData() {
        //TODO test data
        List<Drawable> categoryList = new ArrayList<>();
        categoryList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_dialog_map));
        categoryList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_btn_speak_now));
        categoryList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_dialog_info));
        categoryList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_input_delete));
        categoryList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_lock_idle_low_battery));
        categoryList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_lock_silent_mode));
        categoryList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_lock_power_off));
        categoryList.add(ContextCompat.getDrawable(getActivity(), android.R.drawable.ic_lock_idle_alarm));


        mCategoryAdapter.setCategoryList(categoryList);
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
    public void onCategorySelected() {

    }
}
