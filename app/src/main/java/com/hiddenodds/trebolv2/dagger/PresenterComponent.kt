package com.hiddenodds.trebolv2.dagger

import com.hiddenodds.trebolv2.presentation.view.fragments.*
import dagger.Subcomponent

@Subcomponent(modules = [PresenterModule::class])
interface PresenterComponent {
    fun inject(signInFragment: SignInFragment)
    fun inject(menuFragment: MenuFragment)
    fun inject(otsFragment: OtsFragment)
    fun inject(notificationFragment: NotificationFragment)
    fun inject(tabBaseFragment: TabBaseFragment)
    fun inject(pdfTabFragment: PdfTabFragment)
}