package ru.alexp0111.flexypixel.ui.upperAbstractionLevel

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.alexp0111.flexypixel.data.model.PanelConfiguration
import ru.alexp0111.flexypixel.navigation.Screens
import ru.alexp0111.flexypixel.ui.GlobalStateHandler
import ru.alexp0111.flexypixel.ui.GlobalStateHandlerFactory

@AssistedFactory
interface UpperAbstractionLevelViewModelFactory {
    fun create(schemeId: Int?): UpperAbstractionLevelViewModel
}

class UpperAbstractionLevelViewModel @AssistedInject constructor(
    @Assisted private val schemeId: Int?,
    globalStateHandlerFactory: GlobalStateHandlerFactory,
    private val router: Router,
) : ViewModel() {

    private val globalStateHandler =
        GlobalStateHandler.getInstance(globalStateHandlerFactory, schemeId)

    val segmentImagesBitmapList: MutableStateFlow<List<Bitmap?>> =
        MutableStateFlow(MutableList(PanelConfiguration.MAX_SIZE) { null })

    fun getSegmentsImages() {
        viewModelScope.launch(Dispatchers.IO) {
            segmentImagesBitmapList.update {
                globalStateHandler.getSegmentsBitmapImages()
            }
        }
    }

    fun goToDisplayLevel(segmentNumber: Int) {
        router.navigateTo(Screens.DisplayLevelScreen(segmentNumber))
    }
}