package ru.alexp0111.flexypixel

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import ru.alexp0111.flexypixel.bluetooth.utils.JsonChecker

class JsonCheckerTest {

    @Test
    fun `Check extracting function`() {
        val correctStringAsIs = "{\"status\":\"OK\"}"
        val correctStringWithPostfixClosed = "{\"status\":\"OK\"}}"
        val correctStringWithPostfixOpened = "{\"status\":\"OK\"}{"
        val correctStringWithPostfixRight = "{\"status\":\"OK\"}{}"

        val incorrectStringWithPrefixClosed = "}{\"status\":\"OK\"}"
        val incorrectStringWithPrefixOpened = "{{\"status\":\"OK\"}"

        val incorrectStringAsIs = "\"status\":\"OK\"}"
        val incorrectStringWithPostfixClosed = "tus\":\"OK\"}"
        val incorrectStringWithPostfixOpened = "OK\"}{"
        val incorrectStringWithPostfixRight = "us\":\"OK\"}{}"

        assertEquals("{\"status\":\"OK\"}", JsonChecker.extractRightBracketSequence(correctStringAsIs))
        assertEquals("{\"status\":\"OK\"}", JsonChecker.extractRightBracketSequence(correctStringWithPostfixClosed))
        assertEquals("{\"status\":\"OK\"}", JsonChecker.extractRightBracketSequence(correctStringWithPostfixOpened))
        assertEquals("{\"status\":\"OK\"}", JsonChecker.extractRightBracketSequence(correctStringWithPostfixRight))

        assertNull(JsonChecker.extractRightBracketSequence(incorrectStringWithPrefixClosed))
        assertNull(JsonChecker.extractRightBracketSequence(incorrectStringWithPrefixOpened))

        assertNull(JsonChecker.extractRightBracketSequence(incorrectStringAsIs))
        assertNull(JsonChecker.extractRightBracketSequence(incorrectStringWithPostfixClosed))
        assertNull(JsonChecker.extractRightBracketSequence(incorrectStringWithPostfixOpened))
        assertNull(JsonChecker.extractRightBracketSequence(incorrectStringWithPostfixRight))
    }

}