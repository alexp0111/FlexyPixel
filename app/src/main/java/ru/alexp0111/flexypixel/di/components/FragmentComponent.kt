package ru.alexp0111.flexypixel.di.components

import androidx.fragment.app.Fragment
import ru.alexp0111.flexypixel.ui.start.StartFragment

interface FragmentComponent {

    fun inject(fragment: StartFragment)

    companion object {
        fun from(fragment: Fragment) : FragmentComponent {
            return AppComponent.from(fragment.requireActivity().applicationContext)
        }
    }
}