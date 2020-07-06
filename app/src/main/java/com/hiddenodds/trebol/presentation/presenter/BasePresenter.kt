package com.hiddenodds.trebol.presentation.presenter

import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebol.presentation.interfaces.IPresenter
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter: IPresenter {
    protected var disposable: CompositeDisposable = CompositeDisposable()
    var view: ILoadDataView? = null

    val context = App.appComponent.context()

    override fun destroy() {
        this.view = null
        if (!this.disposable.isDisposed ) this.disposable.dispose()
    }

    override fun showMessage(message: String) {
        this.view!!.showMessage(message)
    }

    override fun showError(error: String) {
        this.view!!.showError(error)
    }

}