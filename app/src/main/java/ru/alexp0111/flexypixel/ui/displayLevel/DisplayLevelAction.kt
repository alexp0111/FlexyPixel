package ru.alexp0111.flexypixel.ui.displayLevel
sealed interface DisplayLevelAction{

}

interface DisplayLevelActionConsumer {
    fun consumeAction(action: DisplayLevelAction)
}