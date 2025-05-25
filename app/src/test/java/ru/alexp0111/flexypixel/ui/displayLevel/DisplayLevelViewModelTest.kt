package ru.alexp0111.flexypixel.ui.displayLevel

import com.github.terrakok.cicerone.Router
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import ru.alexp0111.core.model.DrawingColor
import ru.alexp0111.core.model.main.PanelMetaData
import ru.alexp0111.flexypixel.business.global_state.GlobalStateHandler
import ru.alexp0111.flexypixel.business.global_state.GlobalStateHandlerFactory
import ru.alexp0111.flexypixel.ui.displayLevel.converter.IDisplayLevelConverter
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelIntent
import ru.alexp0111.flexypixel.ui.displayLevel.model.DisplayLevelUiState
import ru.alexp0111.flexypixel.ui.displayLevel.model.PanelUiModel
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
internal class DisplayLevelViewModelTest {

    private val stateHandler: GlobalStateHandler = mockk(relaxed = true)
    private val factory: GlobalStateHandlerFactory = mockk()
    private val router: Router = mockk()
    private val displayLevelConverter: IDisplayLevelConverter = mockk()


    private val segmentNumber = 0
    private val panelsAmount = 1
    private val panelMetaDataSet: Set<PanelMetaData> = mockk()
    private val panelImagesMap: MutableMap<Int, List<DrawingColor>?> = mockk()
    private val uiModels: Set<PanelUiModel> = mockk(relaxed = true)

    private lateinit var viewModel: DisplayLevelViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
        viewModel = spyk(DisplayLevelViewModel(factory, router, displayLevelConverter)) {
            every { globalStateHandler } returns stateHandler
        }
    }

    @AfterEach
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain()
    }

    @Test
    fun loadConfiguration() = runTest {
        // mock
        coEvery { stateHandler.getPanelsConfiguration(segmentNumber) } returns panelMetaDataSet
        coEvery { stateHandler.getPanelsImages(segmentNumber) } returns panelImagesMap
        every {
            displayLevelConverter.convertOrderedPanelsWithPixelsToUiMatrix(panelMetaDataSet, panelImagesMap)
        } returns uiModels
        every { stateHandler.checkIfSelectedAreaSquare() } returns false
        every { stateHandler.getPanelsAmount() } returns panelsAmount

        // action
        viewModel.handleIntent(DisplayLevelIntent.InitSegment(segmentNumber))

        // verify & assert
        coVerifyOrder {
            stateHandler.getPanelsConfiguration(segmentNumber)
            stateHandler.getPanelsImages(segmentNumber)
            displayLevelConverter.convertOrderedPanelsWithPixelsToUiMatrix(panelMetaDataSet, panelImagesMap)
            stateHandler.checkIfSelectedAreaSquare()
            stateHandler.getPanelsAmount()
        }
        assertEquals(viewModel.uiState.value,
            DisplayLevelUiState(
                segmentNumber = segmentNumber,
                panels = uiModels,
            ))
    }

}