package com.personal.hubal.fabfragmentmenu.ui.activity;

import android.app.Fragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.personal.hubal.fabfragmentmenu.ui.fragment.CategoryFragment;
import com.personal.hubal.fabfragmentmenu.R;
import com.personal.hubal.fabfragmentmenu.listener.CategoryTransitionListener;
import com.personal.hubal.fabfragmentmenu.ui.fragment.ObjectFragment;

public class MainActivity extends AppCompatActivity implements CategoryTransitionListener {

    private FloatingActionButton mMenuFAB;

    private View.OnClickListener mMenuFABClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openFragment(CategoryFragment.newInstance(), R.id.categoryContainer);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        mMenuFAB = findViewById(R.id.menuFloatingActionButton);
        mMenuFAB.setOnClickListener(mMenuFABClickListener);
    }

    @Override
    public void openObjectFragment(String category) {
        openFragment(ObjectFragment.newInstance(category), R.id.objectContainer);
    }

    public void openFragment(Fragment fragment, int containerId) {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.animator.slide_in_left,
                        R.animator.slide_in_right,
                        R.animator.slide_in_left,
                        R.animator.slide_in_right)
                .add(containerId, fragment)
                .addToBackStack("")
                .commit();
    }


}
