package ru.alexp0111.flexypixel

import org.junit.Assert
import org.junit.Test
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

        Assert.assertEquals("{\"status\":\"OK\"}", JsonChecker.extractRightBracketSequence(correctStringAsIs))
        Assert.assertEquals("{\"status\":\"OK\"}", JsonChecker.extractRightBracketSequence(correctStringWithPostfixClosed))
        Assert.assertEquals("{\"status\":\"OK\"}", JsonChecker.extractRightBracketSequence(correctStringWithPostfixOpened))
        Assert.assertEquals("{\"status\":\"OK\"}", JsonChecker.extractRightBracketSequence(correctStringWithPostfixRight))

        Assert.assertNull(JsonChecker.extractRightBracketSequence(incorrectStringWithPrefixClosed))
        Assert.assertNull(JsonChecker.extractRightBracketSequence(incorrectStringWithPrefixOpened))

        Assert.assertNull(JsonChecker.extractRightBracketSequence(incorrectStringAsIs))
        Assert.assertNull(JsonChecker.extractRightBracketSequence(incorrectStringWithPostfixClosed))
        Assert.assertNull(JsonChecker.extractRightBracketSequence(incorrectStringWithPostfixOpened))
        Assert.assertNull(JsonChecker.extractRightBracketSequence(incorrectStringWithPostfixRight))
    }

}