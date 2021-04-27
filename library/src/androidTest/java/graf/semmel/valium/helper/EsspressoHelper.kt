package graf.semmel.valium.helper

import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.google.android.material.textfield.TextInputLayout
import org.hamcrest.*
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`

fun hasItemCount(expectedCount: Int) = ViewAssertion { view, noViewFoundException ->
    if (noViewFoundException != null) {
        throw noViewFoundException
    }
    val recyclerView = view as RecyclerView
    val adapter = recyclerView.adapter
    assertThat(adapter!!.itemCount, `is`(expectedCount))
}

fun withChildViewCount(count: Int, childMatcher: Matcher<View>): BoundedMatcher<View, ViewGroup> {
    return object : BoundedMatcher<View, ViewGroup>(ViewGroup::class.java) {
        override fun matchesSafely(viewGroup: ViewGroup): Boolean {
            val matchCount = viewGroup.children
                .filter { childMatcher.matches(it) }
                .count()
            return matchCount == count
        }

        override fun describeTo(description: Description) {
            description.appendText("with child count $count")
        }
    }
}

fun setChecked(checked: Boolean): ViewAction? = object : ViewAction {
    override fun getConstraints(): BaseMatcher<View> {
        return object : BaseMatcher<View>() {
            override fun matches(item: Any): Boolean {
                return CoreMatchers.isA(Checkable::class.java).matches(item)
            }

            override fun describeMismatch(item: Any?, mismatchDescription: Description?) {}
            override fun describeTo(description: Description?) {}
        }
    }

    override fun getDescription(): String? {
        return null
    }

    override fun perform(uiController: UiController?, view: View) {
        (view as Checkable).isChecked = checked
    }
}

fun clickOnChild(@IdRes id: Int): ViewAction? = object : ViewAction {
    override fun getConstraints(): Matcher<View> {
        return allOf(isDisplayed(), isAssignableFrom(View::class.java))
    }

    override fun getDescription(): String? {
        return "Perform click on child view."
    }

    override fun perform(uiController: UiController?, view: View) {
        view.findViewById<View>(id).performClick()
    }
}

fun hasTextInputLayoutErrorText(expectedErrorText: String): Matcher<View> {
    val viewErrorText = "<empty>"

    return object : TypeSafeMatcher<View>() {
        override fun matchesSafely(view: View?): Boolean {
            if (view !is TextInputLayout) return false
            val viewErrorText = view.error?.toString()
            return expectedErrorText == viewErrorText
        }

        override fun describeTo(description: Description?) {
            description?.appendText("expected error was: $expectedErrorText but actual error is:$viewErrorText")
        }
    }
}

fun hasTextInputLayoutErrorText(@StringRes expectedErrorTextRes: Int): Matcher<View> {

    return object : TypeSafeMatcher<View>() {
        private var viewErrorText: String? = null
        private var resolvedExpectedError: String? = null

        override fun matchesSafely(view: View?): Boolean {
            if (view !is TextInputLayout) return false
            resolvedExpectedError = view.context.getString(expectedErrorTextRes)
            viewErrorText = view.error.toString()
            return resolvedExpectedError == viewErrorText
        }

        override fun describeTo(description: Description?) {
            description?.appendText("expected error was: $resolvedExpectedError but actual error is:$viewErrorText")
        }
    }
}

fun hasNoTextInputLayoutErrorText(): Matcher<View> {

    return object : TypeSafeMatcher<View>() {

        override fun matchesSafely(view: View?): Boolean {
            if (view !is TextInputLayout) return false
            return view.error == null
        }

        override fun describeTo(description: Description?) {
            description?.appendText("expected error should be null")
        }
    }
}