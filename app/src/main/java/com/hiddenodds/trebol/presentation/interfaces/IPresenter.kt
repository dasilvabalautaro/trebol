package com.hiddenodds.trebol.presentation.interfaces

interface IPresenter {
    fun destroy()
    fun showMessage(message: String)
    fun showError(error: String)
}