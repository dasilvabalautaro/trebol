package com.hiddenodds.trebolv2.presentation.interfaces

interface ILoadDataView {
    fun showMessage(message: String)
    fun showError(message: String)
    fun executeTask()
}