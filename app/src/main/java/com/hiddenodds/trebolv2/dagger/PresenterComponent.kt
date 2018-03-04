package com.hiddenodds.trebolv2.dagger

import com.hiddenodds.trebolv2.presentation.view.fragments.MenuFragment
import com.hiddenodds.trebolv2.presentation.view.fragments.SignInFragment
import dagger.Subcomponent

@Subcomponent(modules = [PresenterModule::class])
interface PresenterComponent {
    fun inject(signInFragment: SignInFragment)
    fun inject(menuFragment: MenuFragment)
}