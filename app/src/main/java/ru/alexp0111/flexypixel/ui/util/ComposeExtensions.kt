package ru.alexp0111.flexypixel.ui.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import ru.alexp0111.flexypixel.ui.theme.AppTheme

fun Fragment.composeView(
    context: Context = requireContext(),
    apply: ComposeView.() -> Unit,
): View = context.composeView(apply, this)

fun Context.composeView(
    apply: ComposeView.() -> Unit,
    lifecycleOwner: LifecycleOwner,
    width: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    height: Int = ViewGroup.LayoutParams.MATCH_PARENT,
): View =
    ComposeView(this).apply {
        rootView?.setViewTreeLifecycleOwner(lifecycleOwner)
        layoutParams = ViewGroup.LayoutParams(width, height)
    }.apply(apply)

fun ComposeView.setContentAndStrategy(
    strategy: ViewCompositionStrategy = ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed,
    content: @Composable () -> Unit,
) {
    setViewCompositionStrategy(strategy)
    setContent {
        AppTheme {
            content.invoke()
        }
    }
}