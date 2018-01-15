package com.personal.hubal.fabfragmentmenu.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.personal.hubal.fabfragmentmenu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubal on 1/11/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Drawable> mCategoryList = new ArrayList<>();

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

    public void setCategoryList(List<Drawable> categoryList) {
        mCategoryList = categoryList;
        notifyDataSetChanged();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        private ImageView mCategoryImageView;

        private View.OnClickListener mCategoryClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCategoryListener.onCategorySelected();
            }
        };

        CategoryViewHolder(View itemView) {
            super(itemView);

            mCategoryImageView = itemView.findViewById(R.id.categoryImageView);
            itemView.setOnClickListener(mCategoryClickListener);
        }

        void onBind(int position, Drawable category) {
            mCategoryImageView.setImageDrawable(category);
        }
    }

    public interface SelectCategoryListener {
        void onCategorySelected();
    }
}
