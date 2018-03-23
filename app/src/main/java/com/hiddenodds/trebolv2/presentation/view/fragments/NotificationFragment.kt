package com.hiddenodds.trebolv2.presentation.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.dagger.PresenterModule
import com.hiddenodds.trebolv2.presentation.model.MaterialModel
import com.hiddenodds.trebolv2.presentation.presenter.MaterialPresenter
import com.hiddenodds.trebolv2.presentation.presenter.TechnicalPresenter
import javax.inject.Inject

abstract class NotificationFragment: Fragment() {

    companion object Factory{
        var listProduct: ArrayList<MaterialModel> = ArrayList()
    }

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}

    @Inject
    lateinit var technicalPresenter: TechnicalPresenter
    @Inject
    lateinit var materialPresenter: MaterialPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}