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
class ManualTest {

    private lateinit var scenario: FragmentScenario<ShowErrorsFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_MaterialComponents)
    }

    @Test
    fun showFragmentFor60Seconds() {
        onShowErrorsFragment { sleep(60f) }
    }
}