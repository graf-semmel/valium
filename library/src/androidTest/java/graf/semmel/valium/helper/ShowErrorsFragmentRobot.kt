package graf.semmel.valium.helper

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.grafsemmel.valium.R

class ShowErrorsFragmentRobot : BaseRobot() {

    val submitButton: ViewInteraction get() = onView(withId(R.id.btn_submit))

    val input1: ViewInteraction get() = onView(withId(R.id.et_1_show_errors))
    val input2: ViewInteraction get() = onView(withId(R.id.et_2_show_errors))
    val input3: ViewInteraction get() = onView(withId(R.id.et_3_show_errors))
    val input4: ViewInteraction get() = onView(withId(R.id.et_4_show_errors))

    fun ViewInteraction.setValid() = fillEditText("123")
    fun ViewInteraction.setTooShort() = fillEditText("12")

    val inputLayout1: ViewInteraction get() = onView(withId(R.id.etl_1_show_errors))
    val inputLayout2: ViewInteraction get() = onView(withId(R.id.etl_2_show_errors))
    val inputLayout3: ViewInteraction get() = onView(withId(R.id.etl_3_show_errors))
    val inputLayout4: ViewInteraction get() = onView(withId(R.id.etl_4_show_errors))

    fun ViewInteraction.shouldShowRequiredError() = hasErrorTextRes(R.string.validation_error_is_blank)
    fun ViewInteraction.shouldShowTooShortError() = hasErrorText("Input must be at least 3 signs")
    fun ViewInteraction.shouldShowNoError() = hasNoError()
}

fun onShowErrorsFragment(action: ShowErrorsFragmentRobot.() -> Unit) = ShowErrorsFragmentRobot().apply(action)