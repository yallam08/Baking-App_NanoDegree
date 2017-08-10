package nano.yallam.bakingapp;

import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;

@RunWith(AndroidJUnit4.class)
public class StepsDetailsActivityTest {
    @Rule public ActivityTestRule<StepsDetailsActivity> mStepsDetailsActivityTestRule
            = new ActivityTestRule<>(StepsDetailsActivity.class);

    @Test
    public void clickStepItem_ShowsStepDetails() {
        //perform click on a step item
//        onView(withId(R.id.steps_rv)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //check that the step video and description are visible
//        onView(withId(R.id.playerView)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
//        onView(withId(R.id.tv_step_description)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
    }
}
