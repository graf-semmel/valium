package graf.semmel.valium.helper

import android.view.KeyEvent
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import java.io.IOException

open class BaseRobot {

    // to reduce side effects (focus changes, ..) while replacing text in edit text fields; in seconds
    private val WAIT_AFTER_REPLACING_TEXT = 0.2f

    fun setChecked(resId: Int, checked: Boolean): ViewInteraction =
        onView(withId(resId)).perform(setChecked(checked))

    fun isChecked(resId: Int): ViewInteraction =
        onView(withId(resId)).check(matches(isChecked()))

    fun fillEditText(resId: Int, text: String): ViewInteraction =
        onView(withId(resId)).perform(
            ViewActions.replaceText(text),
        ).also { sleep(WAIT_AFTER_REPLACING_TEXT) }

    fun typeEditText(resId: Int, char: Char): ViewInteraction =
        onView(withId(resId)).perform(
            ViewActions.typeText(char.toString()),
        )

    fun backSpaceEditText(resId: Int): ViewInteraction =
        onView(withId(resId)).perform(
            ViewActions.click(),
            ViewActions.pressKey(KeyEvent.KEYCODE_DEL),
        )

    fun clickView(resId: Int): ViewInteraction =
        onView((withId(resId))).perform(ViewActions.click())

    fun textView(resId: Int): ViewInteraction = onView(withId(resId))

    fun matchText(viewInteraction: ViewInteraction, text: String): ViewInteraction = viewInteraction
        .check(matches(withText(text)))

    fun matchText(resId: Int, text: String): ViewInteraction = matchText(textView(resId), text)

    fun hasFocus(resId: Int): ViewInteraction = onView((withId(resId))).check(matches(hasFocus()))

    fun clickBack() {
        Espresso.pressBack()
    }

    /**
     * This method works like a charm
     *
     * SAMPLE CMD OUTPUT:
     * mShowRequested=true mShowExplicitlyRequested=true mShowForced=false mInputShown=true
     */
    fun isKeyboardShown(): Boolean {
        val checkKeyboardCmd = "dumpsys input_method | grep mInputShown"
        try {
            return UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
                .executeShellCommand(checkKeyboardCmd).contains("mInputShown=true")
        } catch (e: IOException) {
            throw RuntimeException("Keyboard check failed", e)
        }
    }

    fun sleep(seconds: Float) = Thread.sleep((seconds * 1000).toLong())

    fun closeKeyboard() {
        Espresso.closeSoftKeyboard()
    }

    fun hasTextInputLayoutErrorText(textInputLayoutId: Int, errorStringResId: Int) {
        onView(withId(textInputLayoutId)).check(matches(hasTextInputLayoutErrorText(errorStringResId)))
    }
}

