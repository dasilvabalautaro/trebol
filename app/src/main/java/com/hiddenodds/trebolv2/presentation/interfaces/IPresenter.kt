package com.hiddenodds.trebolv2.presentation.interfaces

interface IPresenter {
    fun destroy()
    fun showMessage(message: String)
    fun showError(error: String)
    fun hearMessage()
    fun hearError()
}