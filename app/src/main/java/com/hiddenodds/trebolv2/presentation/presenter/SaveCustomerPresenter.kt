package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.domain.interactor.SaveCustomerUseCase
import javax.inject.Inject

class SaveCustomerPresenter @Inject constructor(private val saveCustomerUseCase:
                                                SaveCustomerUseCase){

    fun dispose(){
        saveCustomerUseCase.dispose()
    }
}