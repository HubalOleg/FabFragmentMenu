package com.personal.hubal.fabfragmentmenu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by hubal on 1/15/2018.
 */

public class SwipeRevealLayout extends ViewGroup {

    private static final int DEFAULT_MIN_FLING_VELOCITY = 300; // dp per second
    private static final int DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT = 1; // dp

    private View mMainView;
    private View mSecondaryView;

    private Rect mRectMainClose = new Rect();
    private Rect mRectMainOpen = new Rect();
    private Rect mRectSecClose = new Rect();
    private Rect mRectSecOpen = new Rect();

    private int mMinDistRequestDisallowParent = DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT;

    private volatile boolean mAborted = false;
    private volatile boolean mIsScrolling = false;
    private volatile boolean mLockDrag = false;

    private int mMinFlingVelocity = DEFAULT_MIN_FLING_VELOCITY;

    private int mLastMainLeft = 0;
    private int mLastMainTop = 0;

    private ViewDragHelper mDragHelper;
    private GestureDetectorCompat mGestureDetector;

    private SwipeListener mSwipeListener;

    private int mOnLayoutCount = 0;

    public boolean isOpenBeforeInit = false;

    public interface SwipeListener {
        void onClosed(SwipeRevealLayout view);

        void onOpened(SwipeRevealLayout view);

        void onSlide(SwipeRevealLayout view, float slideOffset);
    }

    public static class SimpleSwipeListener implements SwipeListener {
        @Override
        public void onClosed(SwipeRevealLayout view) {
        }

        @Override
        public void onOpened(SwipeRevealLayout view) {
        }

        @Override
        public void onSlide(SwipeRevealLayout view, float slideOffset) {
        }
    }

    public SwipeRevealLayout(Context context) {
        super(context);
        init(context, null);
    }

    public SwipeRevealLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SwipeRevealLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        mDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        mGestureDetector.onTouchEvent(ev);

        boolean settling = mDragHelper.getViewDragState() == ViewDragHelper.STATE_SETTLING;
        boolean idleAfterScrolled = mDragHelper.getViewDragState() == ViewDragHelper.STATE_IDLE
                && mIsScrolling;

        return settling || idleAfterScrolled;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // get views
        if (getChildCount() >= 2) {
            mSecondaryView = getChildAt(0);
            mMainView = getChildAt(1);
        } else if (getChildCount() == 1) {
            mMainView = getChildAt(0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        mAborted = false;

        for (int index = 0; index < getChildCount(); index++) {
            final View child = getChildAt(index);

            final int minLeft = getPaddingLeft();
            final int maxRight = Math.max(r - getPaddingRight() - l, 0);
            final int minTop = getPaddingTop();
            final int maxBottom = Math.max(b - getPaddingBottom() - t, 0);

            int measuredChildHeight = child.getMeasuredHeight();
            int measuredChildWidth = child.getMeasuredWidth();

            // need to take account if child size is match_parent
            final LayoutParams childParams = child.getLayoutParams();
            boolean matchParentHeight = false;
            boolean matchParentWidth = false;

            if (childParams != null) {
                matchParentHeight = (childParams.height == LayoutParams.MATCH_PARENT);
                matchParentWidth = (childParams.width == LayoutParams.MATCH_PARENT);
            }

            if (matchParentHeight) {
                measuredChildHeight = maxBottom - minTop;
                childParams.height = measuredChildHeight;
            }

            if (matchParentWidth) {
                measuredChildWidth = maxRight - minLeft;
                childParams.width = measuredChildWidth;
            }

            int left = Math.min(getPaddingLeft(), maxRight);
            int top = Math.min(getPaddingTop(), maxBottom);
            int right = Math.min(measuredChildWidth + getPaddingLeft(), maxRight);
            int bottom = Math.min(measuredChildHeight + getPaddingTop(), maxBottom);

            child.layout(left, top, right, bottom);
        }

        mSecondaryView.offsetTopAndBottom(-mSecondaryView.getHeight());

        initRects();

        if (isOpenBeforeInit) {
            open(false);
        } else {
            close(false);
        }

        mLastMainLeft = mMainView.getLeft();
        mLastMainTop = mMainView.getTop();

        mOnLayoutCount++;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getChildCount() < 2) {
            throw new RuntimeException("Layout must have two children");
        }

        final LayoutParams params = getLayoutParams();

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        int desiredWidth = 0;
        int desiredHeight = 0;

        // first find the largest child
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            desiredWidth = Math.max(child.getMeasuredWidth(), desiredWidth);
            // get height of all child
            desiredHeight += child.getMeasuredHeight();
        }
        // create new measure spec using the largest child width
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(desiredWidth, widthMode);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, heightMode);

        final int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
        final int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final LayoutParams childParams = child.getLayoutParams();

            if (childParams != null) {
                if (childParams.height == LayoutParams.MATCH_PARENT) {
                    child.setMinimumHeight(measuredHeight);
                }

                if (childParams.width == LayoutParams.MATCH_PARENT) {
                    child.setMinimumWidth(measuredWidth);
                }
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            desiredWidth = Math.max(child.getMeasuredWidth(), desiredWidth);
            desiredHeight = Math.max(child.getMeasuredHeight(), desiredHeight);
        }

        // taking accounts of padding
        desiredWidth += getPaddingLeft() + getPaddingRight();
        desiredHeight += getPaddingTop() + getPaddingBottom();

        // adjust desired width
        if (widthMode == MeasureSpec.EXACTLY) {
            desiredWidth = measuredWidth;
        } else {
            if (params.width == LayoutParams.MATCH_PARENT) {
                desiredWidth = measuredWidth;
            }

            if (widthMode == MeasureSpec.AT_MOST) {
                desiredWidth = (desiredWidth > measuredWidth) ? measuredWidth : desiredWidth;
            }
        }

        // adjust desired height
        if (heightMode == MeasureSpec.EXACTLY) {
            desiredHeight = measuredHeight;
        } else {
            if (params.height == LayoutParams.MATCH_PARENT) {
                desiredHeight = measuredHeight;
            }

            if (heightMode == MeasureSpec.AT_MOST) {
                desiredHeight = (desiredHeight > measuredHeight) ? measuredHeight : desiredHeight;
            }
        }

        setMeasuredDimension(desiredWidth, desiredHeight);
    }

    @Override
    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void open(boolean animation) {
        isOpenBeforeInit = true;
        mAborted = false;

        if (animation) {
            mDragHelper.smoothSlideViewTo(mMainView, mRectMainOpen.left, mRectMainOpen.top);
        } else {
            mDragHelper.abort();

            mMainView.layout(
                    mRectMainOpen.left,
                    mRectMainOpen.top,
                    mRectMainOpen.right,
                    mRectMainOpen.bottom
            );

            mSecondaryView.layout(
                    mRectSecOpen.left,
                    mRectSecOpen.top,
                    mRectSecOpen.right,
                    mRectSecOpen.bottom
            );
        }

        ViewCompat.postInvalidateOnAnimation(SwipeRevealLayout.this);
    }

    public void close(boolean animation) {
        isOpenBeforeInit = false;
        mAborted = false;

        if (animation) {
            mDragHelper.smoothSlideViewTo(mMainView, mRectMainClose.left, mRectMainClose.top);

        } else {
            mDragHelper.abort();

            mMainView.layout(
                    mRectMainClose.left,
                    mRectMainClose.top,
                    mRectMainClose.right,
                    mRectMainClose.bottom
            );

            mSecondaryView.layout(
                    mRectSecClose.left,
                    mRectSecClose.top,
                    mRectSecClose.right,
                    mRectSecClose.bottom
            );
        }

        ViewCompat.postInvalidateOnAnimation(SwipeRevealLayout.this);
    }

    public void setMinFlingVelocity(int velocity) {
        mMinFlingVelocity = velocity;
    }

    public int getMinFlingVelocity() {
        return mMinFlingVelocity;
    }

    public void setSwipeListener(SwipeListener listener) {
        mSwipeListener = listener;
    }

    public void setLockDrag(boolean lock) {
        mLockDrag = lock;
    }

    public boolean isDragLocked() {
        return mLockDrag;
    }

    protected void abort() {
        mAborted = true;
        mDragHelper.abort();
    }

    protected boolean shouldRequestLayout() {
        return mOnLayoutCount < 2;
    }

    private int getMainOpenLeft() {
        return mRectMainClose.left;
    }

    private int getMainOpenTop() {
        return mRectMainClose.top + mSecondaryView.getHeight();
    }

    private int getSecOpenLeft() {
        return mRectSecClose.left - mSecondaryView.getWidth();
    }

    private int getSecOpenTop() {
        return mRectSecClose.top + mSecondaryView.getHeight();
    }

    private void initRects() {
        // close position of main view
        mRectMainClose.set(
                mMainView.getLeft(),
                mMainView.getTop(),
                mMainView.getRight(),
                mMainView.getBottom()
        );

        // close position of secondary view
        mRectSecClose.set(
                mSecondaryView.getLeft(),
                mSecondaryView.getTop(),
                mSecondaryView.getRight(),
                mSecondaryView.getBottom()
        );

        // open position of the main view
        mRectMainOpen.set(
                getMainOpenLeft(),
                getMainOpenTop(),
                getMainOpenLeft() + mMainView.getWidth(),
                getMainOpenTop() + mMainView.getHeight()
        );

        // open position of the secondary view
        mRectSecOpen.set(
                getSecOpenLeft(),
                getSecOpenTop(),
                getSecOpenLeft() + mSecondaryView.getWidth(),
                getSecOpenTop() + mSecondaryView.getHeight()
        );
    }

    private void init(Context context, AttributeSet attrs) {
        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback);
        mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL);

        mGestureDetector = new GestureDetectorCompat(context, mGestureListener);
    }

    private final GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
        boolean hasDisallowed = false;

        @Override
        public boolean onDown(MotionEvent e) {
            mIsScrolling = false;
            hasDisallowed = false;
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            mIsScrolling = true;
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            mIsScrolling = true;

            if (getParent() != null) {
                boolean shouldDisallow;

                if (!hasDisallowed) {
                    shouldDisallow = getDistToClosestEdge() >= mMinDistRequestDisallowParent;
                    if (shouldDisallow) {
                        hasDisallowed = true;
                    }
                } else {
                    shouldDisallow = true;
                }

                // disallow parent to intercept touch event so that the layout will work
                // properly on RecyclerView or view that handles scroll gesture.
                getParent().requestDisallowInterceptTouchEvent(shouldDisallow);
            }

            return false;
        }
    };

    private int getDistToClosestEdge() {
        final int pivotBottom = mRectMainClose.top + mSecondaryView.getHeight();

        return Math.min(
                mMainView.getBottom() - pivotBottom,
                pivotBottom - mMainView.getTop()
        );
    }

    private int getHalfwayPivotHorizontal() {
        return mRectMainClose.right - mSecondaryView.getWidth() / 2;
    }

    private int getHalfwayPivotVertical() {
        return mRectMainClose.top + mSecondaryView.getHeight() / 2;
    }

    private final ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            mAborted = false;

            if (mLockDrag)
                return false;

            mDragHelper.captureChildView(mMainView, pointerId);
            return false;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            return Math.max(
                    Math.min(top, mRectMainClose.top + mSecondaryView.getHeight()),
                    mRectMainClose.top
            );
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            return child.getLeft();
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            final boolean velUpExceeded = pxToDp((int) yvel) <= -mMinFlingVelocity;
            final boolean velDownExceeded = pxToDp((int) yvel) >= mMinFlingVelocity;

            final int pivotVertical = getHalfwayPivotVertical();

            if (velUpExceeded) {
                close(true);
            } else if (velDownExceeded) {
                open(true);
            } else {
                if (mMainView.getTop() < pivotVertical) {
                    close(true);
                } else {
                    open(true);
                }
            }
        }

        @Override
        public void onEdgeDragStarted(int edgeFlags, int pointerId) {
            super.onEdgeDragStarted(edgeFlags, pointerId);

            if (mLockDrag) {
                return;
            }

            boolean edgeStartBottom = edgeFlags == ViewDragHelper.EDGE_BOTTOM;

            if (edgeStartBottom) {
                mDragHelper.captureChildView(mMainView, pointerId);
            }
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            mSecondaryView.offsetTopAndBottom(dy);

            boolean isMoved = (mMainView.getLeft() != mLastMainLeft) || (mMainView.getTop() != mLastMainTop);
            if (mSwipeListener != null && isMoved) {
                if (mMainView.getLeft() == mRectMainClose.left && mMainView.getTop() == mRectMainClose.top) {
                    mSwipeListener.onClosed(SwipeRevealLayout.this);
                } else if (mMainView.getLeft() == mRectMainOpen.left && mMainView.getTop() == mRectMainOpen.top) {
                    mSwipeListener.onOpened(SwipeRevealLayout.this);
                } else {
                    mSwipeListener.onSlide(SwipeRevealLayout.this, getSlideOffset());
                }
            }

            mLastMainLeft = mMainView.getLeft();
            mLastMainTop = mMainView.getTop();
            ViewCompat.postInvalidateOnAnimation(SwipeRevealLayout.this);
        }

        private float getSlideOffset() {
            return (float) (mMainView.getTop() - mRectMainClose.top) / mSecondaryView.getHeight();
        }
    };

    private int pxToDp(int px) {
        Resources resources = getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
