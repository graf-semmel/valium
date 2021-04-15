package com.grafsemmel.valium

import graf.semmel.valium.validator.Validators
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.Test

class AlphanumericValidatorsTest {

    private val alphaNumericValidator = Validators.AlphaNumericValidator()

    @Test
    fun alphanumericCharactersShouldBeValid() {
        alphaNumericValidator.run {
            assertThat(validate("ABC123"), `is`(true))
        }
    }

    @Test
    fun allowedSpecialCharactersShouldBeValid() {
        alphaNumericValidator.run {
            assertThat(validate("/.-_‘, "), `is`(true))
        }
    }

    @Test
    fun nonAlphaNumericCharactersShouldNotBeValid() {
        alphaNumericValidator.run {
            listOf("à", "á", "â", "ä", "æ", "ã", "å", "ā").onEach {
                assertThat("testing $it", validate(it), `is`(false))
            }
        }
    }

    @Test
    fun forbiddenSpecialCharactersShouldNotBeValid() {
        alphaNumericValidator.run {
            listOf("!", "@", "#", "$", "%", "^", "&", "*", "(", ")", "=", "+").onEach {
                assertThat(validate(it), `is`(false))
            }
        }
    }
}