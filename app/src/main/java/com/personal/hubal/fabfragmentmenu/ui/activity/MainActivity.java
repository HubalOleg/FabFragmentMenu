package com.personal.hubal.fabfragmentmenu.ui.activity;

import android.app.Fragment;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;

import com.personal.hubal.fabfragmentmenu.ui.fragment.CategoryFragment;
import com.personal.hubal.fabfragmentmenu.R;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout mConstraintLayout;

    private ConstraintSet mVisibleConstraint;
    private ConstraintSet mHideConstraint;

    private boolean isMenuOpen = true;

    private View.OnClickListener mOnSwipeButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Transition transition = new AutoTransition();
            transition.setDuration(300);
            transition.setInterpolator(new AccelerateDecelerateInterpolator());

            TransitionManager.beginDelayedTransition(mConstraintLayout, transition);
            ConstraintSet constraintSet = isMenuOpen ? mHideConstraint : mVisibleConstraint;
            constraintSet.applyTo(mConstraintLayout);
            isMenuOpen = !isMenuOpen;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initConstraints();
        openFragment(CategoryFragment.newInstance(), R.id.categoryContainer);
    }

    private void initUI() {
        mConstraintLayout = findViewById(R.id.constraintLayout);

        FrameLayout categoryContainer = findViewById(R.id.categoryContainer);
        categoryContainer.setClickable(true);

        Button swipeButton = findViewById(R.id.buttonSwipe);
        swipeButton.setOnClickListener(mOnSwipeButtonClickListener);

    }

    private void initConstraints() {
        mVisibleConstraint = new ConstraintSet();
        mVisibleConstraint.clone(mConstraintLayout);

        mHideConstraint = new ConstraintSet();
        mHideConstraint.clone(mConstraintLayout);
        mHideConstraint.addToVerticalChain(R.id.categoryContainer, -1, R.id.constraintLayout);
    }

    public void openFragment(Fragment fragment, int containerId) {
        getFragmentManager().beginTransaction()
                .add(containerId, fragment)
                .commit();
    }

}
