package nano.yallam.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import nano.yallam.bakingapp.model.Ingredient;
import nano.yallam.bakingapp.model.Step;

public class StepsDetailsActivity extends AppCompatActivity implements StepsListAdapter.OnStepClickListener {

    private boolean mTwoPane;

    public static String STEP_VIDEO_URL_KEY = "step_video_url";
    public static String STEP_DESCRIPTION_KEY = "step_description_url";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps_details);

        if (findViewById(R.id.step_details_scroll_view) != null) {
            mTwoPane = true;

            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
                fragmentManager.beginTransaction()
                        .add(R.id.step_details_container, stepDetailsFragment)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }

    @Override
    public void onStepSelected(ArrayList<Step> steps, int position) {
        String stepVideoUrl = steps.get(position).getVideoURL();
        String stepDescription = steps.get(position).getDescription();

        if (mTwoPane) {
            StepDetailsFragment stepDetailsFragment = new StepDetailsFragment();
            stepDetailsFragment.setVideoURL(stepVideoUrl);
            stepDetailsFragment.setStepDescription(stepDescription);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_details_container, stepDetailsFragment)
                    .commit();
        } else {
            Intent intent = new Intent(StepsDetailsActivity.this, StepDetailsActivity.class);
            intent.putExtra(STEP_VIDEO_URL_KEY, stepVideoUrl);
            intent.putExtra(STEP_DESCRIPTION_KEY, stepDescription);
            startActivity(intent);
        }
    }
}
