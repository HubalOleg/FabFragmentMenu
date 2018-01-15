package com.personal.hubal.fabfragmentmenu.adapter;

import android.content.ClipData;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
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

public class ObjectAdapter extends RecyclerView.Adapter<ObjectAdapter.ObjectViewHolder> {

    private static final String TAG = "ObjectAdapter";

    private List<Drawable> mObjectList = new ArrayList<>();

    private View mDraggingView;

    public ObjectAdapter() {
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

    public void stopDragAndDrop() {
        if (mDraggingView != null)
            mDraggingView.cancelDragAndDrop();
    }

    public class ObjectViewHolder extends RecyclerView.ViewHolder {

        private ImageView mImage;
        private int mPosition;

        private View.OnLongClickListener mObjectLongClickListener = new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipData dragData = ClipData.newPlainText("", "");

                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(view);

                view.startDragAndDrop(dragData, myShadow, null, 0);
                view.setOnDragListener(mOnDragListener);

                return true;
            }
        };

        private View.OnDragListener mOnDragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent dragEvent) {
                switch (dragEvent.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        mDraggingView = view;
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        mDraggingView = null;
                        break;
                }
                return false;
            }
        };

        ObjectViewHolder(View itemView) {
            super(itemView);

            mImage = itemView.findViewById(R.id.objectImageView);
            mImage.setOnLongClickListener(mObjectLongClickListener);
        }

        void onBind(int position, Drawable image) {
            mPosition = position;

            mImage.setImageDrawable(image);
        }
    }
}
