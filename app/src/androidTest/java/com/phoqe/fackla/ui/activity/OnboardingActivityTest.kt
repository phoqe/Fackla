package com.phoqe.fackla.ui.activity


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import androidx.test.runner.AndroidJUnit4
import com.phoqe.fackla.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class OnboardingActivityTest {

    @Rule
    @JvmField
    var activityRule = ActivityTestRule(OnboardingActivity::class.java)

    @Rule
    @JvmField
    var permsRule =
        GrantPermissionRule.grant(
            "android.permission.ACCESS_FINE_LOCATION"
        )

    @Test
    fun onboardingActivityTest() {
        val imageView = onView(
            allOf(
                withId(R.id.iv_image),
                withContentDescription(R.string.onboarding_intro_empty_state_content_desc),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.tv_title), withText(R.string.onboarding_intro_screen_title),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.tv_title), withText(R.string.onboarding_intro_screen_title),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText(R.string.onboarding_intro_screen_title)))

        val textView3 = onView(
            allOf(
                withId(R.id.tv_body),
                withText(R.string.onboarding_intro_screen_body),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView3.check(matches(isDisplayed()))

        val textView4 = onView(
            allOf(
                withId(R.id.tv_body),
                withText(R.string.onboarding_intro_screen_body),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView4.check(matches(withText(R.string.onboarding_intro_screen_body)))

        val button = onView(
            allOf(
                withId(R.id.btn_get_started), withText(R.string.onboarding_intro_button),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

        val materialButton = onView(
            allOf(
                withId(R.id.btn_get_started), withText(R.string.onboarding_intro_button),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton.perform(click())

        val imageView2 = onView(
            allOf(
                withId(R.id.iv_image),
                withContentDescription(R.string.onboarding_mock_location_content_desc),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        imageView2.check(matches(isDisplayed()))

        val textView5 = onView(
            allOf(
                withId(R.id.tv_title), withText(R.string.onboarding_mock_location_title),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView5.check(matches(isDisplayed()))

        val textView6 = onView(
            allOf(
                withId(R.id.tv_title), withText(R.string.onboarding_mock_location_title),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView6.check(matches(withText(R.string.onboarding_mock_location_title)))

        val textView7 = onView(
            allOf(
                withId(R.id.tv_body),
                withText(R.string.onboarding_mock_location_body),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView7.check(matches(isDisplayed()))

        val textView8 = onView(
            allOf(
                withId(R.id.tv_body),
                withText(R.string.onboarding_mock_location_body),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView8.check(matches(withText(R.string.onboarding_mock_location_body)))

        val button2 = onView(
            allOf(
                withId(R.id.btn_select_mock_loc_app), withText(R.string.onboarding_mock_location_button),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        button2.check(matches(isDisplayed()))

        val textView9 = onView(
            allOf(
                withText(R.string.onboarding_mock_location_caption),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView9.check(matches(isDisplayed()))

        val textView10 = onView(
            allOf(
                withText(R.string.onboarding_mock_location_caption),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView10.check(matches(withText(R.string.onboarding_mock_location_caption)))

        val materialButton2 = onView(
            allOf(
                withId(R.id.btn_select_mock_loc_app), withText(R.string.onboarding_mock_location_button),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton2.perform(click())

        val imageView3 = onView(
            allOf(
                withId(R.id.iv_image),
                withContentDescription(R.string.onboarding_location_permission_empty_state_content_desc),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        imageView3.check(matches(isDisplayed()))

        val textView11 = onView(
            allOf(
                withId(R.id.tv_title), withText(R.string.onboarding_location_permission_title),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView11.check(matches(isDisplayed()))

        val textView12 = onView(
            allOf(
                withId(R.id.tv_title), withText(R.string.onboarding_location_permission_title),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView12.check(matches(withText(R.string.onboarding_location_permission_title)))

        val textView13 = onView(
            allOf(
                withId(R.id.tv_body),
                withText(R.string.onboarding_location_permission_body),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView13.check(matches(isDisplayed()))

        val textView14 = onView(
            allOf(
                withId(R.id.tv_body),
                withText(R.string.onboarding_location_permission_body),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView14.check(matches(withText(R.string.onboarding_location_permission_body)))

        val button3 = onView(
            allOf(
                withId(R.id.btn_review_permission), withText(R.string.onboarding_location_permission_button),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        button3.check(matches(isDisplayed()))

        val textView15 = onView(
            allOf(
                withText(R.string.onboarding_location_permission_caption),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView15.check(matches(isDisplayed()))

        val textView16 = onView(
            allOf(
                withText(R.string.onboarding_location_permission_caption),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView16.check(matches(withText(R.string.onboarding_location_permission_caption)))

        val materialButton3 = onView(
            allOf(
                withId(R.id.btn_review_permission), withText(R.string.onboarding_location_permission_button),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.LinearLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val imageView4 = onView(
            allOf(
                withId(R.id.iv_image), withContentDescription(R.string.onboarding_end_empty_state_content_desc),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        imageView4.check(matches(isDisplayed()))

        val textView17 = onView(
            allOf(
                withId(R.id.tv_title), withText(R.string.onboarding_end_title),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView17.check(matches(isDisplayed()))

        val textView18 = onView(
            allOf(
                withId(R.id.tv_title), withText(R.string.onboarding_end_title),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView18.check(matches(withText(R.string.onboarding_end_title)))

        val textView19 = onView(
            allOf(
                withId(R.id.tv_body),
                withText(R.string.onboarding_end_body),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView19.check(matches(isDisplayed()))

        val textView20 = onView(
            allOf(
                withId(R.id.tv_body),
                withText(R.string.onboarding_end_body),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView20.check(matches(withText(R.string.onboarding_end_body)))

        val button4 = onView(
            allOf(
                withId(R.id.btn_start), withText(R.string.onboarding_end_button),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        button4.check(matches(isDisplayed()))

        val materialButton4 = onView(
            allOf(
                withId(R.id.btn_start), withText(R.string.onboarding_end_button),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("android.widget.FrameLayout")),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialButton4.perform(click())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
