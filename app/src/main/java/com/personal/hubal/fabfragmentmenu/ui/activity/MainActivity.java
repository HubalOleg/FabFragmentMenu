package com.personal.hubal.fabfragmentmenu.ui.activity;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.personal.hubal.fabfragmentmenu.SwipeRevealLayout;
import com.personal.hubal.fabfragmentmenu.ui.fragment.CategoryFragment;
import com.personal.hubal.fabfragmentmenu.R;

public class MainActivity extends AppCompatActivity {

    private Button mSwipeButton;
    private SwipeRevealLayout mSwipeRevealLayout;

    private boolean isMenuOpen = false;

    private SwipeRevealLayout.SwipeListener mSwipeListener = new SwipeRevealLayout.SwipeListener() {
        @Override
        public void onClosed(SwipeRevealLayout view) {
            isMenuOpen = false;
            mSwipeButton.setText("Down");
        }

        @Override
        public void onOpened(SwipeRevealLayout view) {
            isMenuOpen = true;
            mSwipeButton.setText("Up");
        }

        @Override
        public void onSlide(SwipeRevealLayout view, float slideOffset) {

        }
    };

    private View.OnClickListener mOnSwipeButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isMenuOpen) {
                mSwipeRevealLayout.close(true);
            } else {
                mSwipeRevealLayout.open(true);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        openFragment(CategoryFragment.newInstance(), R.id.categoryContainer);
    }

    private void initUI() {
        mSwipeRevealLayout = findViewById(R.id.swipeRevealLayout);

        mSwipeRevealLayout.setSwipeListener(mSwipeListener);

        mSwipeButton = findViewById(R.id.buttonSwipe);
        mSwipeButton.setOnClickListener(mOnSwipeButtonClickListener);
    }

    public void openFragment(Fragment fragment, int containerId) {
        getFragmentManager().beginTransaction()
                .add(containerId, fragment)
                .addToBackStack("")
                .commit();
    }

}
