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
import com.example.dajeffri.shoppinglist.StoreItemJoin;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DeleteItemTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void deleteItemTest() {
        DataRepository dataRepository = new DataRepository(mActivityTestRule.getActivity().getApplication());
        dataRepository.populateDB();

        ViewInteraction appCompatTextView = onView(
                allOf(ViewMatchers.withId(R.id.store_name), withText("Store1"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.item_name), withText("Item23"),
                        childAtPosition(
                                allOf(withId(R.id.item_constraint),
                                        childAtPosition(
                                                withId(R.id.items_recycler),
                                                6)),
                                0),
                        isDisplayed()));
        appCompatTextView2.perform(longClick());

        pressBack();

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.item_name), withText("Item18"),
                        childAtPosition(
                                allOf(withId(R.id.item_constraint),
                                        childAtPosition(
                                                withId(R.id.items_recycler),
                                                4)),
                                0),
                        isDisplayed()));
        appCompatTextView3.perform(longClick());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.item_name), withText("Item22"),
                        childAtPosition(
                                allOf(withId(R.id.item_constraint),
                                        childAtPosition(
                                                withId(R.id.items_recycler),
                                                6)),
                                0),
                        isDisplayed()));
        appCompatTextView4.perform(longClick());

        Item testItem = dataRepository.getItem("Item23");
        assertNull(testItem);

        StoreItemJoin testStoreItemJoin = dataRepository.getStoreItemJoin("Item23");
        assertNull(testStoreItemJoin);

        testItem = dataRepository.getItem("Item18");
        assertNull(testItem);

        testStoreItemJoin = dataRepository.getStoreItemJoin("Item18");
        assertNull(testStoreItemJoin);

        testItem = dataRepository.getItem("Item22");
        assertNull(testItem);

        testStoreItemJoin = dataRepository.getStoreItemJoin("Item22");
        assertNull(testStoreItemJoin);

        testItem = dataRepository.getItem("Item0");
        assertNotNull(testItem);
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
