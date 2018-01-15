package com.personal.hubal.fabfragmentmenu.ui.activity;

import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.personal.hubal.fabfragmentmenu.ui.fragment.CategoryFragment;
import com.personal.hubal.fabfragmentmenu.R;

public class MainActivity extends AppCompatActivity {

    private Button mSwipeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
    }

    private void initUI() {
        mSwipeButton = findViewById(R.id.buttonSwipe);
        mSwipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFragmentOpen()) {
                    getFragmentManager().popBackStack();
                } else {
                    openFragment(CategoryFragment.newInstance(), R.id.categoryContainer);
                }
            }
        });
    }

    public void openFragment(Fragment fragment, int containerId) {
        getFragmentManager().beginTransaction()
                .add(containerId, fragment)
                .addToBackStack("")
                .commit();
    }

    private boolean isFragmentOpen() {
        return getFragmentManager().getBackStackEntryCount() > 0;
    }


}
