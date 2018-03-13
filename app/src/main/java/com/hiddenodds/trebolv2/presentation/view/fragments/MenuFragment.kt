package com.hiddenodds.trebolv2.presentation.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.dagger.PresenterModule
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.presentation.presenter.MaterialRemotePresenter
import com.hiddenodds.trebolv2.presentation.presenter.NotificationRemotePresenter
import com.hiddenodds.trebolv2.presentation.view.activities.MainActivity
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject


class MenuFragment: Fragment(), ILoadDataView {
    @BindView(R.id.pb_download)
    @JvmField var pbDownload: ProgressBar? = null
    @BindView(R.id.btn_update_water)
    @JvmField var btUpdateWater: Button? = null
    @BindView(R.id.btn_get)
    @JvmField var btGetNotification: Button? = null
    @BindView(R.id.btn_update_thinks)
    @JvmField var btGetDataGeneral: Button? = null
    @BindView(R.id.btn_ots)
    @JvmField var btShowOTS: Button? = null

    @OnClick(R.id.btn_update_water)
    fun updateDataInWater(){

    }

    @OnClick(R.id.btn_get)
    fun updateDataNotification(){
        setViewForTransferData()
        launch {
            notificationRemotePresenter.executeDownloadNotification()
        }
    }

    @OnClick(R.id.btn_update_thinks)
    fun updateDataGeneral(){
        setViewForTransferData()
        launch {
            materialRemotePresenter.executeQueryRemote()
        }

    }

    @OnClick(R.id.btn_ots)
    fun viewOTS(){
        val fragmentOtsFragment = OtsFragment()
        (context as MainActivity).addFragment(fragmentOtsFragment)
    }

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}

    @Inject
    lateinit var materialRemotePresenter: MaterialRemotePresenter
    @Inject
    lateinit var notificationRemotePresenter: NotificationRemotePresenter


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
        notificationRemotePresenter.view = this
        activity.theme.applyStyle(R.style.AppTheme, true)
    }

    override fun showMessage(message: String) {
        context.toast(message)
    }

    override fun showError(message: String) {
        pbDownload!!.visibility = View.INVISIBLE
        enabledButton(true)
        context.toast(message)
    }

    override fun <T> executeTask(obj: T) {
        pbDownload!!.visibility = View.INVISIBLE
        enabledButton(true)
    }

    private fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun enabledButton(value: Boolean){
        btGetDataGeneral!!.isEnabled = value
        btGetNotification!!.isEnabled = value
        btShowOTS!!.isEnabled = value
        btUpdateWater!!.isEnabled = value
    }

    private fun setViewForTransferData(){
        enabledButton(false)
        context.toast(context.resources.getString(R.string.wait_task))
        pbDownload!!.visibility = View.VISIBLE
    }
}