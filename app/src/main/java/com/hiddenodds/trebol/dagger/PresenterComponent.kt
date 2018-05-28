package com.hiddenodds.trebol.dagger

import com.hiddenodds.trebol.presentation.view.fragments.*
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