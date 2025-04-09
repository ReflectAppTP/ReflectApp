package com.example.reflect

import com.example.reflect.common.Utils
import org.junit.Test
import org.junit.Assert.*

class EmailValidationTest {
    @Test
    fun `is email1 valid`() {
        assertTrue(Utils.isEmailValid("dmitriy_abdullaev@bk.ru"))
    }

    @Test
    fun `is email2 valid`() {
        assertFalse(Utils.isEmailValid("dmitriy_abdullaevbk.ru"))
    }

    @Test
    fun `is email3 valid`() {
        assertFalse(Utils.isEmailValid("@bk.ru"))
    }

    @Test
    fun `is email4 valid`() {
        assertFalse(Utils.isEmailValid("dmitriy_abdullaev@b"))
    }

    @Test
    fun `is email5 valid`() {
        assertTrue(Utils.isEmailValid("d@b.r"))
    }
}