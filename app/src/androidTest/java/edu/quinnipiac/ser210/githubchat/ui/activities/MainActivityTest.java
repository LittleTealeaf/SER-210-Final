package edu.quinnipiac.ser210.githubchat.ui.activities;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.not;

import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import edu.quinnipiac.ser210.githubchat.R;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void testCreateChat() {
        onView(withId(R.id.frag_home_fab_create)).perform(click());
        onView(withId(R.id.frag_create_search_filter)).perform(click());
        onView(withId(R.id.frag_create_search_filter)).perform(typeSearchViewText("Testing"));
        onView(withId(R.id.frag_create_fab_confirm)).check(matches(not(isEnabled())));
        onView(withId(R.id.frag_create_search_filter)).perform(typeSearchViewText("Testing/Testing"));
        onView(withId(R.id.frag_create_fab_confirm)).check(matches(isEnabled()));
        onView(withId(R.id.frag_create_fab_confirm)).perform(click());
        onView(withId(R.id.frag_chat_edittext_insert)).perform(typeText("This is a testing message"));
        onView(withId(R.id.frag_chat_button_send)).perform(click());
        onView(withId(R.id.frag_chat_edittext_insert)).check(matches(withText("")));
        onView(withId(R.id.menu_toolbar_info)).perform(click());

        onView(isRoot()).perform(pressBack());





//        onView(withId(R.id.frag_create_search_filter)).perform(typeText("Testing/Testing"));

    }


    public static ViewAction typeSearchViewText(final String text){
        return new ViewAction(){
            @Override
            public Matcher<View> getConstraints() {
                //Ensure that only apply if it is a SearchView and if it is visible.

                return allOf(isDisplayed(),isAssignableFrom(SearchView.class));
            }

            @Override
            public String getDescription() {
                return "Change view text";
            }

            @Override
            public void perform(UiController uiController, View view) {
                ((SearchView) view).setQuery(text,false);
            }
        };
    }

}