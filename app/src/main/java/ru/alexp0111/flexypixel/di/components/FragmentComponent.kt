package ru.alexp0111.flexypixel.di.components

import androidx.fragment.app.Fragment

interface FragmentComponent {

    companion object {
        fun from(fragment: Fragment) : FragmentComponent {
            return AppComponent.from(fragment.requireActivity().applicationContext)
        }
    }
}