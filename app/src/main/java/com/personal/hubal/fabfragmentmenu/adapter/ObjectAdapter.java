package com.personal.hubal.fabfragmentmenu.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.personal.hubal.fabfragmentmenu.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hubal on 1/11/2018.
 */

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ObjectViewHolder> {

    private List<Drawable> mObjectList = new ArrayList<>();

    public SelectObjectListener mSelectObjectListener;

    public ObjectAdapter(SelectObjectListener selectObjectListener) {
        mSelectObjectListener = selectObjectListener;
    }

    @Override
    public ObjectAdapter.ObjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_object, parent, false);

        return new ObjectAdapter.ObjectViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ObjectAdapter.ObjectViewHolder holder, int position) {
        holder.onBind(position, mObjectList.get(position));
    }

    @Override
    public int getItemCount() {
        return mObjectList.size();
    }

    public void setObjectList(List<Drawable> objectList) {
        mObjectList = objectList;
        notifyDataSetChanged();
    }

    public class ObjectViewHolder extends RecyclerView.ViewHolder {

        private ImageView mObject;
        private int mPosition;

        private View.OnClickListener mObjectClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectObjectListener.onObjectSelected(mPosition);
            }
        };

        ObjectViewHolder(View itemView) {
            super(itemView);

            mObject = itemView.findViewById(R.id.objectImageView);
            itemView.setOnClickListener(mObjectClickListener);
        }

        void onBind(int position, Drawable image) {
            mPosition = position;

            mObject.setImageDrawable(image);
        }
    }

    public interface SelectObjectListener {
        void onObjectSelected(int position);
    }
}
