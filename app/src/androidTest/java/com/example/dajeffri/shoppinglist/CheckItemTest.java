package com.example.dajeffri.shoppinglist;


import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.dajeffri.shoppinglist.R;
import com.example.dajeffri.shoppinglist.DataRepository;
import com.example.dajeffri.shoppinglist.Item;
import com.example.dajeffri.shoppinglist.MainActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class CheckItemTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void checkItemTest() {
        DataRepository dataRepository = new DataRepository(mActivityTestRule.getActivity().getApplication());
        dataRepository.populateDB();

        ViewInteraction appCompatTextView = onView(
                allOf(ViewMatchers.withId(R.id.store_name), withText("Store0"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.item_name), withText("Item12"),
                        childAtPosition(
                                allOf(withId(R.id.item_constraint),
                                        childAtPosition(
                                                withId(R.id.items_recycler),
                                                2)),
                                0),
                        isDisplayed()));
        appCompatTextView2.perform(click());

        pressBack();

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.item_name), withText("Item13"),
                        childAtPosition(
                                allOf(withId(R.id.item_constraint),
                                        childAtPosition(
                                                withId(R.id.items_recycler),
                                                2)),
                                0),
                        isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.item_name), withText("Item19"),
                        childAtPosition(
                                allOf(withId(R.id.item_constraint),
                                        childAtPosition(
                                                withId(R.id.items_recycler),
                                                4)),
                                0),
                        isDisplayed()));
        appCompatTextView4.perform(click());

        Item testItem = dataRepository.getItem("Item12");
        assertTrue(testItem.getChecked());

        testItem = dataRepository.getItem("Item13");
        assertTrue(testItem.getChecked());

        testItem = dataRepository.getItem("Item19");
        assertTrue(testItem.getChecked());

        testItem = dataRepository.getItem("Item0");
        assertFalse(testItem.getChecked());

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
