package com.hiddenodds.trebolv2.presentation.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import butterknife.ButterKnife
import butterknife.OnClick
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.dagger.PresenterModule
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.presentation.presenter.MaterialRemotePresenter
import com.hiddenodds.trebolv2.presentation.presenter.TypeNotificationRemotePresenter
import javax.inject.Inject


class MenuFragment: Fragment(), ILoadDataView {
    @OnClick(R.id.btn_update_water)
    fun updateDataInWater(){

    }
    @OnClick(R.id.btn_get)
    fun updateDataNotification(){

    }
    @OnClick(R.id.btn_update_thinks)
    fun updateDataGeneral(){

        typeNotificationRemotePresenter.executeQueryRemote()
        materialRemotePresenter.executeQueryRemote()
    }
    @OnClick(R.id.btn_ots)
    fun viewOTS(){

    }

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}

    @Inject
    lateinit var materialRemotePresenter: MaterialRemotePresenter
    @Inject
    lateinit var typeNotificationRemotePresenter: TypeNotificationRemotePresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_menu,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        materialRemotePresenter.view = this
        typeNotificationRemotePresenter.view = this
        activity.theme.applyStyle(R.style.AppTheme, true)
    }

    override fun showMessage(message: String) {
        context.toast(message)
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun executeTask() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}