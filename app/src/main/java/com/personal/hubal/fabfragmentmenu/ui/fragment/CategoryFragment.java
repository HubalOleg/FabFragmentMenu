package com.personal.hubal.fabfragmentmenu.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.personal.hubal.fabfragmentmenu.R;
import com.personal.hubal.fabfragmentmenu.adapter.CategoryAdapter;
import com.personal.hubal.fabfragmentmenu.listener.CategoryTransitionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubal on 1/11/2018.
 */

public class CategoryFragment extends Fragment implements CategoryAdapter.SelectCategoryListener {

    private CategoryTransitionListener mCategoryTransitionListener;

    private CategoryAdapter mCategoryAdapter;

    public static CategoryFragment newInstance() {
        Bundle args = new Bundle();

        CategoryFragment fragment = new CategoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCategoryTransitionListener = (CategoryTransitionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CategoryTransitionListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        initUI(view);

        initCategoryListData();

        return view;
    }

    private void initUI(View view) {
        RecyclerView categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        categoryRecyclerView.setLayoutManager(layoutManager);

        mCategoryAdapter = new CategoryAdapter(CategoryFragment.this);
        categoryRecyclerView.setAdapter(mCategoryAdapter);
    }

    private void initCategoryListData() {
        //TODO test data
        List<String> categoryList = new ArrayList<>();
        categoryList.add("Category 1");
        categoryList.add("Category 2");
        categoryList.add("Category 3");
        categoryList.add("Category 4");
        categoryList.add("Category 5");

        mCategoryAdapter.setCategoryList(categoryList);
    }

    @Override
    public void onCategorySelected(String category) {
        mCategoryTransitionListener.openObjectFragment(category);
    }
}
