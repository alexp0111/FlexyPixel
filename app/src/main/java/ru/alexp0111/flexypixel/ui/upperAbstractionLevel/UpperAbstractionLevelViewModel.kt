package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.alexp0111.core.matrix.MatrixConverter
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.database.schemes.SavedSchemeRepository
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.GlobalStateHandler
import ru.alexp0111.flexypixel.ui.GlobalStateHandlerFactory
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.SEGMENT_MATRIX_SIDE
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.SegmentUiState
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelEffect
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelIntent
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelUiState
import javax.inject.Inject

class UpperAbstractionLevelViewModel @Inject constructor(
    private val router: Router,
    private val databaseRepository: SavedSchemeRepository,
    private val globalStateHandlerFactory: GlobalStateHandlerFactory,
) : MVIViewModel<UpperAbstractionLevelIntent, UpperAbstractionLevelUiState, UpperAbstractionLevelEffect>() {

    private var globalStateHandler: GlobalStateHandler? = null

    override fun setInitialState(): UpperAbstractionLevelUiState {
        return UpperAbstractionLevelUiState()
    }

    override fun handleIntent(intent: UpperAbstractionLevelIntent) =
        when (intent) {
            is UpperAbstractionLevelIntent.InitScheme -> initScheme(intent.schemeId)
            is UpperAbstractionLevelIntent.TitleChanged -> updateTitle(intent.newTitle)
            UpperAbstractionLevelIntent.Save -> saveScheme()
            is UpperAbstractionLevelIntent.CardClicked -> {
                goToDisplayLevel(MatrixConverter.XYtoIndex(intent.x, intent.y, SEGMENT_MATRIX_SIDE))
            }
        }

    private fun initScheme(schemeId: Int?) {
        globalStateHandler = GlobalStateHandler.getInstance(globalStateHandlerFactory, schemeId)
        launch {
            launch { schemeId?.let { setUpTitle(it) } }
            launch { setUpSegments() }
        }
    }

    private suspend fun setUpTitle(schemeId: Int) = withContext(Dispatchers.IO) {
        val title = databaseRepository.getSchemeById(schemeId).title
        viewModelScope.launch {
            setState { uiState.value.copy(title = title) }
        }
    }

    private suspend fun setUpSegments() = withContext(Dispatchers.IO) {
        val segments = globalStateHandler?.getSegmentsBitmapImages()
        segments?.let {
            val segmentList: MutableList<List<SegmentUiState>> = mutableListOf()
            it.windowed(SEGMENT_MATRIX_SIDE, SEGMENT_MATRIX_SIDE) { segmentRow ->
                segmentList.add(
                    segmentRow.map { bitmap ->
                        SegmentUiState(bitmap)
                    }
                )
            }
            viewModelScope.launch {
                setState { uiState.value.copy(segmentMatrix = segmentList) }
            }
        }
    }

    private fun updateTitle(newTitle: String) {
        setState { uiState.value.copy(title = newTitle) }
    }

    private fun saveScheme() {
        launch {
            val currentTitle = uiState.value.title
            if (!validTitle(currentTitle)) {
                setEffect { UpperAbstractionLevelEffect.ShowSnackBar("Title invalid or already exist") }
            } else {
                globalStateHandler?.saveStateToDatabase(currentTitle)
                setEffect { UpperAbstractionLevelEffect.ShowSnackBar("Saved successfully") }
            }
        }
    }

    private suspend fun validTitle(currentTitle: String): Boolean {
        val allSchemesTitles = withContext(Dispatchers.IO) {
            databaseRepository.getAllSchemes().first()
        }.map { it.title }
        return !allSchemesTitles.contains(currentTitle)
                && currentTitle.isNotEmpty()
                && currentTitle.isNotBlank()
    }

    private fun goToDisplayLevel(segmentNumber: Int) {
        router.navigateTo(Screens.DisplayLevelScreen(segmentNumber))
    }
}