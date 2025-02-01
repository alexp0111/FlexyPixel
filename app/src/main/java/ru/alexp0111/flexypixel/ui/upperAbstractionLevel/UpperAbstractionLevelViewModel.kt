package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.matrix.MatrixConverter
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.database.schemes.SavedSchemeRepository
import ru.alexp0111.flexypixel.database.schemes.data.UserSavedScheme
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.GlobalStateHandler
import ru.alexp0111.flexypixel.ui.GlobalStateHandlerFactory
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.converter.IUpperAbstractionLevelConverter
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelEffect
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelIntent
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelUiState
import javax.inject.Inject

class UpperAbstractionLevelViewModel @Inject constructor(
    private val router: Router,
    private val databaseRepository: SavedSchemeRepository,
    private val upperAbstractionLevelConverter: IUpperAbstractionLevelConverter,
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
            is UpperAbstractionLevelIntent.CardSizeMeasured -> initSegmentBitmaps(intent.size)
            UpperAbstractionLevelIntent.RefreshSegmentInfo -> refreshSegmentBitmaps()
            UpperAbstractionLevelIntent.Save -> saveScheme()
            is UpperAbstractionLevelIntent.CardClicked -> {
                goToDisplayLevel(
                    MatrixConverter.XYtoIndex(
                        intent.x,
                        intent.y,
                        CommonSizeConstants.SEGMENTS_MATRIX_SIDE
                    )
                )
            }
        }

    private fun initScheme(schemeId: Int?) {
        globalStateHandler = GlobalStateHandler.getInstance(globalStateHandlerFactory, schemeId)
        launch { schemeId?.let { setUpTitle(it) } }
    }

    private suspend fun setUpTitle(schemeId: Int) = withContext(Dispatchers.IO) {
        val title = databaseRepository.getSchemeById(schemeId).title
        viewModelScope.launch {
            setState { uiState.value.copy(title = title) }
        }
    }

    private fun initSegmentBitmaps(size: Int) {
        if (uiState.value.cardSizePx == size) return
        setState { uiState.value.copy(cardSizePx = size) }
        refreshSegmentBitmaps()
    }

    private fun refreshSegmentBitmaps() {
        val size = uiState.value.cardSizePx
        if (size == 0) return
        launch(Dispatchers.IO) {
            val segments = globalStateHandler?.getSegmentsBitmapImages(size) ?: return@launch
            val segmentMatrix = upperAbstractionLevelConverter.convertSegmentListToMatrix(segments)
            viewModelScope.launch {
                setState { uiState.value.copy(segmentMatrix = segmentMatrix) }
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
        }.map { it }
        return currentTitle.isNotEmpty()
                && currentTitle.isNotBlank()
                && !conflictWithExistingScheme(allSchemesTitles, currentTitle)
    }

    private fun conflictWithExistingScheme(schemes: List<UserSavedScheme>, title: String) =
        schemes.any { it.title == title && it.id != globalStateHandler?.schemeId }

    private fun goToDisplayLevel(segmentNumber: Int) {
        router.navigateTo(Screens.DisplayLevelScreen(segmentNumber))
    }
}