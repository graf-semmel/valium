package graf.semmel.valium

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grafsemmel.valium.R
import graf.semmel.valium.fragments.ShowErrorsFragment
import graf.semmel.valium.helper.onShowErrorsFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShowErrorsFragmentTest {

    private lateinit var scenario: FragmentScenario<ShowErrorsFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_MaterialComponents)
    }

    @Test
    fun input1ShouldShowErrorsAlways() {
        onShowErrorsFragment {
            inputLayout1.shouldShowNoError()
            input1.setValid()
            inputLayout1.shouldShowNoError()
            input1.setTooShort()
            inputLayout1.shouldShowTooShortError()
            input1.setValid()
            inputLayout1.shouldShowNoError()
        }
    }

    @Test
    fun input2ShouldShowErrorsAfterFocusedOnce() {
        onShowErrorsFragment {
            inputLayout2.shouldShowNoError()
            input2.setTooShort()
            inputLayout2.shouldShowTooShortError()
            input2.setValid()
            inputLayout2.shouldShowNoError()
            input2.setTooShort()
            inputLayout2.shouldShowTooShortError()
        }
    }

    @Test
    fun input3ShouldShowErrorsAfterFocusLost() {
        onShowErrorsFragment {
            input3.clickView() // set focus
            inputLayout3.shouldShowNoError()
            input3.setTooShort()
            inputLayout3.shouldShowNoError()
            input3.setValid()
            inputLayout3.shouldShowNoError()
            input3.setTooShort()
            inputLayout3.shouldShowNoError()
            input1.clickView() // set focus
            inputLayout3.shouldShowTooShortError()
        }
    }

    @Test
    fun input4ShouldShowOnSubmit() {
        onShowErrorsFragment {
            inputLayout4.shouldShowNoError()
            input4.setTooShort()
            inputLayout4.shouldShowNoError()
            input4.setValid()
            inputLayout4.shouldShowNoError()
            input4.setTooShort()
            inputLayout4.shouldShowNoError()
            closeKeyboard()
            submitButton.clickView()
            inputLayout4.shouldShowTooShortError()
        }
    }

}