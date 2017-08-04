package nano.yallam.bakingapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;
import android.view.Window;
import android.view.WindowManager;

public class StepDetailsActivity extends AppCompatActivity {

    String STEP_DETAILS_FRAGMENT_TAG = "step_details_frag_tag";
    StepDetailsFragment mStepDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //hide title and action bar, giving room for the recipe video to take up the whole screen
        if (isLandscape()) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getSupportActionBar().hide();
            getWindow().setFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN
            );
        }
        setContentView(R.layout.activity_step_details);

        if (savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            mStepDetailsFragment = new StepDetailsFragment();

            if (getIntent().hasExtra(StepsDetailsActivity.STEP_VIDEO_URL_KEY)) {
                mStepDetailsFragment.setVideoURL(
                        getIntent().getStringExtra(StepsDetailsActivity.STEP_VIDEO_URL_KEY)
                );
            }
            if (getIntent().hasExtra(StepsDetailsActivity.STEP_DESCRIPTION_KEY)) {
                mStepDetailsFragment.setStepDescription(
                        getIntent().getStringExtra(StepsDetailsActivity.STEP_DESCRIPTION_KEY)
                );
            }

            if (isLandscape()) {
                mStepDetailsFragment.setFullscreenVideo(true);
            }

            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, mStepDetailsFragment, STEP_DETAILS_FRAGMENT_TAG)
                    .commit();
        } else {
            mStepDetailsFragment = (StepDetailsFragment) getSupportFragmentManager().findFragmentByTag(STEP_DETAILS_FRAGMENT_TAG);
            if (isLandscape()) {
                mStepDetailsFragment.setFullscreenVideo(true);
            }
        }
    }

    private boolean isLandscape() {
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE))
                .getDefaultDisplay();
        int orientation = display.getRotation();

        return  (orientation == Surface.ROTATION_90 || orientation == Surface.ROTATION_270);
    }

}
