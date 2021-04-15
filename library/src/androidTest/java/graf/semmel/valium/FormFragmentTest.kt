package graf.semmel.valium

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grafsemmel.valium.R
import graf.semmel.valium.fragments.FormFragment
import graf.semmel.valium.helper.formFragmentRobot
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FormFragmentTest {

    private lateinit var scenario: FragmentScenario<FormFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_MaterialComponents)
    }

    @Test
    fun formIsInvalidOnStart() {
        formFragmentRobot {
            submitButtonShouldBeDisabled()
        }
    }

    @Test
    fun formIsInvalidWhenOneInputFieldIsInvalid() {
        formFragmentRobot {
            setInput1Valid()
            submitButtonShouldBeDisabled()
        }
    }

    @Test
    fun formIsInvalidWhenInputIsTooShort() {
        formFragmentRobot {
            setInput1Valid()
            setInput2TooShort()
            setInput3TooShort()
            submitButtonShouldBeDisabled()
        }
    }

    @Test
    fun formIsValidWhenFieldIsNotRequired() {
        formFragmentRobot {
            setInput1Valid()
            setInput2Valid()
            submitButtonShouldBeEnabled()
        }
    }

    @Test
    fun formIsValid() {
        formFragmentRobot {
            setInput1Valid()
            setInput2Valid()
            setInput3Valid()
            submitButtonShouldBeEnabled()
        }
    }
}