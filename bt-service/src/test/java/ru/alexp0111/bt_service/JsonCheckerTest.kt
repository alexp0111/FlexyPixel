package ru.alexp0111.bt_service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import ru.alexp0111.bt_service.utils.JsonChecker
import java.util.stream.Stream

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JsonCheckerTest {

    @ParameterizedTest
    @MethodSource("jsonSource")
    fun `Check extracting function`(expectedResult: String?, incomingMessage: String) {
        assertEquals(expectedResult, JsonChecker.extractRightBracketSequence(incomingMessage))
    }

    private fun jsonSource(): Stream<Arguments> = Stream.of(
        Arguments.of("{\"status\":\"OK\"}", "{\"status\":\"OK\"}"),
        Arguments.of("{\"status\":\"OK\"}", "{\"status\":\"OK\"}}"),
        Arguments.of("{\"status\":\"OK\"}", "{\"status\":\"OK\"}{"),
        Arguments.of("{\"status\":\"OK\"}", "{\"status\":\"OK\"}{}"),
        Arguments.of(null, "}{\"status\":\"OK\"}"),
        Arguments.of(null, "{{\"status\":\"OK\"}"),
        Arguments.of(null, "\"status\":\"OK\"}"),
        Arguments.of(null, "tus\":\"OK\"}"),
        Arguments.of(null, "OK\"}{"),
        Arguments.of(null, "us\":\"OK\"}{}"),
    )

}