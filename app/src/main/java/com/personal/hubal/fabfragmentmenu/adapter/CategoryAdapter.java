package com.personal.hubal.fabfragmentmenu.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.personal.hubal.fabfragmentmenu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubal on 1/11/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<String> mCategoryList = new ArrayList<>();

    public SelectCategoryListener mCategoryListener;

    public CategoryAdapter(SelectCategoryListener selectCategoryListener) {
        mCategoryListener = selectCategoryListener;
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.onBind(position, mCategoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCategoryList.size();
    }

    public void setCategoryList(List<String> categoryList) {
        mCategoryList = categoryList;
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView mNameTextView;

        private String mCategory;

        private View.OnClickListener mCategoryClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategoryListener.onCategorySelected(mCategory);
            }
        };

        CategoryViewHolder(View itemView) {
            super(itemView);

            mNameTextView = itemView.findViewById(R.id.categoryNameTextView);
            itemView.setOnClickListener(mCategoryClickListener);
        }

        void onBind(int position, String category) {
            mCategory = category;

            mNameTextView.setText(category);
        }
    }

    public interface SelectCategoryListener {
        void onCategorySelected(String category);
    }
}
