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
import com.example.dajeffri.shoppinglist.Store;
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
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class DeleteStoreTest {

    @Rule
    public final ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void deleteStoreTest() {
        DataRepository dataRepository = new DataRepository(mActivityTestRule.getActivity().getApplication());
        dataRepository.populateDB();

        ViewInteraction appCompatImageButton = onView(
                allOf(withContentDescription("Open navigation drawer"),
                        childAtPosition(
                                allOf(ViewMatchers.withId(R.id.toolbar),
                                        childAtPosition(
                                                withClassName(is("android.support.constraint.ConstraintLayout")),
                                                0)),
                                1),
                        isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction navigationMenuItemView = onView(
                allOf(childAtPosition(
                        allOf(withId(R.id.design_navigation_view),
                                childAtPosition(
                                        withId(R.id.nav_view),
                                        0)),
                        1),
                        isDisplayed()));
        navigationMenuItemView.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withId(R.id.store_name), withText("Store8"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(R.id.store_name), withText("Store8"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView2.perform(longClick());

        pressBack();

        ViewInteraction appCompatTextView3 = onView(
                allOf(withId(R.id.store_name), withText("Store6"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView3.perform(longClick());

        ViewInteraction appCompatTextView4 = onView(
                allOf(withId(R.id.store_name), withText("Store2"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView4.perform(longClick());

        pressBack();

        ViewInteraction appCompatTextView5 = onView(
                allOf(withId(R.id.store_name), withText("Store0"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.support.v7.widget.CardView")),
                                        0),
                                0),
                        isDisplayed()));
        appCompatTextView5.perform(longClick());

        dataRepository.deleteItems();

        Store testStore = dataRepository.getStore("Store0");
        assertNull(testStore);
        StoreItemJoin testStoreItemJoin = dataRepository.getStoreItemJoin("Store0");
        assertNull(testStoreItemJoin);

        testStore = dataRepository.getStore("Store2");
        assertNull(testStore);
        testStoreItemJoin = dataRepository.getStoreItemJoin("Store2");
        assertNull(testStoreItemJoin);

        testStore = dataRepository.getStore("Store6");
        assertNull(testStore);
        testStoreItemJoin = dataRepository.getStoreItemJoin("Store6");
        assertNull(testStoreItemJoin);

        testStore = dataRepository.getStore("Store8");
        assertNull(testStore);
        testStoreItemJoin = dataRepository.getStoreItemJoin("Store8");
        assertNull(testStoreItemJoin);

        testStore = dataRepository.getStore("Store1");
        assertEquals(testStore.getStoreName(), "Store1");
        testStoreItemJoin = dataRepository.getStoreItemJoin("Item1");
        assertEquals(testStoreItemJoin.mItemName, "Item1");

        Item testItem = dataRepository.getItem("item8");
        assertNull(testItem);
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
