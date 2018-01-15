package com.personal.hubal.fabfragmentmenu;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by hubal on 1/15/2018.
 */

public class ScrollLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollEnabled = true;

    public ScrollLinearLayoutManager(Context context) {
        super(context);
    }

    public void setScrollEnabled(boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollEnabled && super.canScrollHorizontally();
    }
}
