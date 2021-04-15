package graf.semmel.valium

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.grafsemmel.valium.R
import graf.semmel.valium.fragments.ContactDetailsFragment
import graf.semmel.valium.helper.onContactDetailsForm
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContactDetailsFragmentTest {

    private lateinit var scenario: FragmentScenario<ContactDetailsFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_MaterialComponents)
    }

    @Test
    fun formIsInvalidOnStart() {
        onContactDetailsForm {
            submitButtonShouldBeDisabled()
        }
    }

    @Test
    fun formIsInvalidWhenOneInputFieldIsInvalid() {
        onContactDetailsForm() {
            setFirstName()
            submitButtonShouldBeDisabled()
        }
    }

    @Test
    fun formIsValid() {
        onContactDetailsForm {
            setFirstName()
            setLastName()
            setEmail()
            submitButtonShouldBeEnabled()
        }
    }

    @Test
    fun formShowsDifferentErrorMessages() {
        onContactDetailsForm {
            sleep(2f)
            setFirstNameToShort()
            setFirstName()
            setLastName()
            setWrongEmail()
            setEmail()
            submitButtonShouldBeEnabled()
        }
    }
}