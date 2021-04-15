package com.grafsemmel.valium

import graf.semmel.valium.validator.Validators
import org.junit.Assert.*
import org.junit.Test

class DefaultValidatorsTest {

    @Test
    fun testLengthValidator() {
        assertTrue(Validators.LengthValidator(4).validate("1234"))
        assertTrue(Validators.LengthValidator(0).validate(""))
        assertFalse(Validators.LengthValidator(4).validate(""))
        assertFalse(Validators.LengthValidator(4).validate("12345"))
    }

    @Test
    fun testMaxLengthValidator() {
        assertTrue(Validators.MaxLengthValidator(4).validate("1234"))
        assertTrue(Validators.MaxLengthValidator(4).validate("123"))
        assertTrue(Validators.MaxLengthValidator(4).validate(""))
        assertTrue(Validators.MaxLengthValidator(0).validate(""))
        assertFalse(Validators.MaxLengthValidator(4).validate("12345"))
    }

    @Test
    fun testMinLengthValidator() {
        assertTrue(Validators.MinLengthValidator(4).validate("1234"))
        assertTrue(Validators.MinLengthValidator(4).validate("12345"))
        assertTrue(Validators.MinLengthValidator(0).validate(""))
        assertFalse(Validators.MinLengthValidator(4).validate("123"))
        assertFalse(Validators.MinLengthValidator(4).validate(""))
    }

    @Test
    fun testRangeValidator() {
        assertTrue(Validators.RangeValidator(4, 6).validate("1234"))
        assertTrue(Validators.RangeValidator(4, 6).validate("12345"))
        assertTrue(Validators.RangeValidator(4, 6).validate("123456"))
        assertTrue(Validators.RangeValidator(0, 0).validate(""))
        assertTrue(Validators.RangeValidator(4, 4).validate("1234"))
        assertFalse(Validators.RangeValidator(4, 6).validate("123"))
        assertFalse(Validators.RangeValidator(4, 6).validate("1234567"))
        assertFalse(Validators.RangeValidator(4, 4).validate("12345"))
        assertFalse(Validators.RangeValidator(4, 4).validate("123"))
    }

    @Test
    fun testNotBlankValidator() {
        assertTrue(Validators.NotBlankValidator().validate("1234"))
        assertTrue(Validators.NotBlankValidator().validate("1"))
        assertTrue(Validators.NotBlankValidator().validate(" 1"))
        assertTrue(Validators.NotBlankValidator().validate(" 1 "))
        assertTrue(Validators.NotBlankValidator().validate("1 "))
        assertFalse(Validators.NotBlankValidator().validate(""))
        assertFalse(Validators.NotBlankValidator().validate(" "))
    }

    @Test
    fun startsWithValidator() {
        assertTrue(Validators.StartsWithValidator("1").validate("1234"))
        assertTrue(Validators.StartsWithValidator("").validate("1234"))
        assertTrue(Validators.StartsWithValidator("1234").validate("1234"))
        assertFalse(Validators.StartsWithValidator().validate("1234"))
        assertFalse(Validators.StartsWithValidator("1234").validate(""))
        assertFalse(Validators.StartsWithValidator("1234").validate("123"))
        assertFalse(Validators.StartsWithValidator("1").validate("2"))
    }

    @Test
    fun startsMotWithValidator() {
        assertTrue(Validators.StartsWithValidator("5", mustNot = true).validate("1234"))
        assertTrue(Validators.StartsWithValidator("12345", mustNot = true).validate("1234"))
        assertTrue(Validators.StartsWithValidator("2", mustNot = true).validate("1234"))
        assertTrue(Validators.StartsWithValidator("2", "3", mustNot = true).validate("1234"))
        assertTrue(Validators.StartsWithValidator("1234", mustNot = true).validate(""))
        assertFalse(Validators.StartsWithValidator().validate("1234"))
        assertFalse(Validators.StartsWithValidator("1", mustNot = true).validate("1234"))
        assertFalse(Validators.StartsWithValidator("", mustNot = true).validate("1234"))
        assertFalse(Validators.StartsWithValidator("1234", mustNot = true).validate("1234"))
        assertFalse(Validators.StartsWithValidator("123", mustNot = true).validate("1234"))
    }

    @Test
    fun endsWithValidator() {
        assertTrue(Validators.EndsWithValidator("4").validate("1234"))
        assertTrue(Validators.EndsWithValidator("34").validate("1234"))
        assertTrue(Validators.EndsWithValidator("").validate("1234"))
        assertTrue(Validators.EndsWithValidator("1234").validate("1234"))
        assertFalse(Validators.EndsWithValidator().validate("1234"))
        assertFalse(Validators.EndsWithValidator("1234").validate(""))
        assertFalse(Validators.EndsWithValidator("1234").validate("123"))
        assertFalse(Validators.EndsWithValidator("1").validate("2"))
    }

    @Test
    fun endsMotWithValidator() {
        assertTrue(Validators.EndsWithValidator("5", mustNot = true).validate("1234"))
        assertTrue(Validators.EndsWithValidator("12345", mustNot = true).validate("1234"))
        assertTrue(Validators.EndsWithValidator("2", mustNot = true).validate("1234"))
        assertTrue(Validators.EndsWithValidator("2", "3", mustNot = true).validate("1234"))
        assertTrue(Validators.EndsWithValidator("1234", mustNot = true).validate(""))
        assertFalse(Validators.EndsWithValidator().validate("1234"))
        assertFalse(Validators.EndsWithValidator("4", mustNot = true).validate("1234"))
        assertFalse(Validators.EndsWithValidator("", mustNot = true).validate("1234"))
        assertFalse(Validators.EndsWithValidator("1234", mustNot = true).validate("1234"))
        assertFalse(Validators.EndsWithValidator("234", mustNot = true).validate("1234"))
    }

    @Test
    fun containsWithValidator() {
        assertTrue(Validators.ContainsValidator("4").validate("1234"))
        assertTrue(Validators.ContainsValidator("34").validate("1234"))
        assertTrue(Validators.ContainsValidator("").validate("1234"))
        assertTrue(Validators.ContainsValidator("1234").validate("1234"))
        assertFalse(Validators.ContainsValidator().validate("1234"))
        assertFalse(Validators.ContainsValidator("1234").validate(""))
        assertFalse(Validators.ContainsValidator("1234").validate("123"))
        assertFalse(Validators.ContainsValidator("1").validate("2"))
    }

    @Test
    fun containsMotWithValidator() {
        assertTrue(Validators.ContainsValidator("5", mustNot = true).validate("1234"))
        assertTrue(Validators.ContainsValidator("222", mustNot = true).validate("1234"))
        assertTrue(Validators.ContainsValidator("13", mustNot = true).validate("1234"))
        assertTrue(Validators.ContainsValidator("22", "0", mustNot = true).validate("1234"))
        assertTrue(Validators.ContainsValidator("1234", mustNot = true).validate(""))
        assertFalse(Validators.ContainsValidator().validate("1234"))
        assertFalse(Validators.ContainsValidator("4", mustNot = true).validate("1234"))
        assertFalse(Validators.ContainsValidator("", mustNot = true).validate("1234"))
        assertFalse(Validators.ContainsValidator("1234", mustNot = true).validate("1234"))
        assertFalse(Validators.ContainsValidator("23", "4", mustNot = true).validate("1234"))
    }

    @Test
    fun testEmailValidator() {
        assertTrue(Validators.EmailValidator().validate("hans.zimmer@monese.com"))
        assertTrue(Validators.EmailValidator().validate("a@b.c"))
        assertFalse(Validators.EmailValidator().validate(""))
        assertFalse(Validators.EmailValidator().validate(" "))
        assertFalse(Validators.EmailValidator().validate("a"))
        assertFalse(Validators.EmailValidator().validate("a.b"))
        assertFalse(Validators.EmailValidator().validate("a@b"))
        assertFalse(Validators.EmailValidator().validate("a@b."))
        assertFalse(Validators.EmailValidator().validate("a@.c"))
        assertFalse(Validators.EmailValidator().validate("@a.c"))
        assertFalse(Validators.EmailValidator().validate("a@@b.c"))
    }

    @Test
    fun testIllegalArgumentsException() {
        val calls = listOf(
            Runnable { assertFalse(Validators.MinLengthValidator(-1).validate("")) },
            Runnable { assertFalse(Validators.MaxLengthValidator(-1).validate("")) },
            Runnable { assertFalse(Validators.LengthValidator(-1).validate("")) },
            Runnable { assertFalse(Validators.RangeValidator(-1, 0).validate("")) },
            Runnable { assertFalse(Validators.RangeValidator(0, -1).validate("")) },
            Runnable { assertFalse(Validators.RangeValidator(4, 3).validate("")) }
        )
        calls.forEachIndexed { index, runnable ->
            try {
                runnable.run()
                fail("Expected an IllegalArgumentException to be thrown for call number $index")
            } catch (illegalArgumentException: IllegalArgumentException) {
            }
        }
    }
}