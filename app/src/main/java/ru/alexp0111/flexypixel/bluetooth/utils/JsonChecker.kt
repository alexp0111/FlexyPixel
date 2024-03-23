package ru.alexp0111.flexypixel.bluetooth.utils

import java.util.Stack

object JsonChecker {

    fun extractRightBracketSequence(collectedResult: String): String? {
        val dict = mapOf('{' to '}')
        val actingSymbols = setOf('{', '}')
        val stack = Stack<Char>()
        for (i in collectedResult.indices) {
            if (collectedResult[i] !in actingSymbols) {
                continue
            }

            if (stack.isNotEmpty() && dict[stack.peek()] == collectedResult[i]) {
                stack.pop()
            } else {
                stack.push(collectedResult[i])
            }

            if (stack.isEmpty()) {
                return collectedResult.take(i + 1)
            }
        }
        return null
    }

}