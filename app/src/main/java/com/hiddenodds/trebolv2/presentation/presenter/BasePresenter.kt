package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.domain.interfaces.IHearMessage
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.presentation.interfaces.IPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter: IPresenter {
    private var disposable: CompositeDisposable = CompositeDisposable()
    var iHearMessage: IHearMessage? = null
    var view: ILoadDataView? = null

    val context = App.appComponent.context()

    override fun destroy() {
        this.view = null
        if (!this.disposable.isDisposed ) this.disposable.dispose()
    }

    override fun hearError() {
        val hear = iHearMessage!!.hearError()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { e ->
                    showError(e.message)
                })
    }

    override fun hearMessage() {
        val hear = iHearMessage!!.hearMessage()
        disposable.add(hear.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    showMessage(s)
                })
    }

    override fun showMessage(message: String) {
        this.view!!.showMessage(message)
    }

    override fun showError(error: String) {
        this.view!!.showError(error)
    }

}