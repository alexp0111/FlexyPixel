package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.alexp0111.core.CommonSizeConstants
import ru.alexp0111.core.matrix.MatrixConverter
import ru.alexp0111.core.viewmodel.MVIViewModel
import ru.alexp0111.flexypixel.business.global_state.GlobalStateHandler
import ru.alexp0111.flexypixel.business.global_state.GlobalStateHandlerFactory
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.converter.IUpperAbstractionLevelConverter
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelEffect
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelIntent
import ru.alexp0111.flexypixel.ui.upperAbstractionLevel.model.UpperAbstractionLevelUiState
import ru.alexp0111.local_repo.database.schemes.SavedSchemeRepository
import ru.alexp0111.local_repo.database.schemes.data.UserSavedScheme
import javax.inject.Inject

internal class UpperAbstractionLevelViewModel @Inject constructor(
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
            is UpperAbstractionLevelIntent.InitScheme -> initScheme(intent.schemeId, intent.preview, intent.createdAt)
            is UpperAbstractionLevelIntent.TitleChanged -> updateTitle(intent.newTitle)
            is UpperAbstractionLevelIntent.CardSizeMeasured -> initSegmentBitmaps(intent.size)
            UpperAbstractionLevelIntent.RefreshSegmentInfo -> refreshSegmentBitmaps()
            UpperAbstractionLevelIntent.Save -> saveScheme()
            is UpperAbstractionLevelIntent.CardClicked -> goToDisplayLevel(intent.x, intent.y)
        }

    private fun initScheme(schemeId: String?, preview: String?, createdAt: String?) {
        globalStateHandler = GlobalStateHandler.getInstance(globalStateHandlerFactory)
        launch {
            setState { copy(isLoading = true) }
            globalStateHandler?.init(schemeId, preview, createdAt)
            refreshSegmentBitmaps()
            setState { copy(isLoading = false, title = globalStateHandler?.title.orEmpty()) }
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
        val allSchemesTitles = databaseRepository.getAllSchemes()
        return currentTitle.isNotEmpty()
                && currentTitle.isNotBlank()
                && !conflictWithExistingScheme(allSchemesTitles, currentTitle)
    }

    private fun conflictWithExistingScheme(schemes: List<UserSavedScheme>, title: String) =
        schemes.any { it.title == title && it.id != globalStateHandler?.schemeId }

    private fun goToDisplayLevel(segmentX: Int, segmentY: Int) {
        val segmentNumber = MatrixConverter.XYtoIndex(segmentX, segmentY, CommonSizeConstants.SEGMENTS_MATRIX_SIDE)
        router.navigateTo(Screens.DisplayLevelScreen(segmentNumber))
    }
}