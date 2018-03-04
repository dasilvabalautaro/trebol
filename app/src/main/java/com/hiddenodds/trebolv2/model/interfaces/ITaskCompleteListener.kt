package com.hiddenodds.trebolv2.model.interfaces


interface ITaskCompleteListener {
    fun onSaveFailed(error: String)
    fun onSaveSucceeded()
    fun onDeleteCompleted()
    fun onDeleteFailed(error: String)
    fun onUpdateCompleted()
    fun onUpdateFailed(error: String)
}