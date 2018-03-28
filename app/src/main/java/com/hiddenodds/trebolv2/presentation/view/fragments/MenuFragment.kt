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
import com.hiddenodds.trebolv2.presentation.presenter.*
import kotlinx.coroutines.experimental.async
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
        setViewForTransferData()
        async {
            updateDataRemoteWaterPresenter.executeUpdateRemoteWater()
        }

    }

    @OnClick(R.id.btn_get)
    fun updateDataNotification(){
        setViewForTransferData()
        notificationDownloadPresenter.executeDownloadNotification()
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
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, fragmentOtsFragment,
                        fragmentOtsFragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()

    }

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}

    @Inject
    lateinit var materialRemotePresenter: MaterialRemotePresenter

    @Inject
    lateinit var notificationDownloadPresenter: NotificationDownloadPresenter
    @Inject
    lateinit var customerDownloadPresenter: CustomerDownloadPresenter
    @Inject
    lateinit var saveNotificationPresenter: SaveNotificationPresenter
    @Inject
    lateinit var saveCustomerPresenter: SaveCustomerPresenter
    @Inject
    lateinit var addNotificationToTechnicalPresenter: AddNotificationToTechnicalPresenter
    @Inject
    lateinit var updateDataRemoteWaterPresenter: UpdateDataRemoteWaterPresenter

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
        addNotificationToTechnicalPresenter.view = this
        notificationDownloadPresenter.view = this
        customerDownloadPresenter.view = this
        saveNotificationPresenter.view = this
        saveCustomerPresenter.view = this
        updateDataRemoteWaterPresenter.view = this
        activity.theme.applyStyle(R.style.AppTheme, true)
    }

    override fun showMessage(message: String) {
        context.toast(message)
    }

    override fun showError(message: String) {
        clearPresenter()
        clearWaterPresenter()
        enableView()
        context.toast(message)
    }

    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun processDownloadNotification(option: Int){
        var message = ""

        when(option){
            1 -> {
                Thread.sleep(3000)
                message = context.resources.getString(R.string.notification_download)
                customerDownloadPresenter.executeDownloadCustomer()
            }
            2 -> {
                message = context.resources.getString(R.string.customer_download)
                saveNotificationPresenter.executeSaveNotification()

            }
            3 -> {
                message = context.resources.getString(R.string.notification_save)
                saveCustomerPresenter.executeGetCustomer()

            }

            4 -> {
                message = context.resources.getString(R.string.customer_save)
                addNotificationToTechnicalPresenter.executeAddNotification()
            }

            5 -> {
                message = context.resources.getString(R.string.add_notifications) +
                        " End process."
                clearPresenter()
                enableView()
            }

            6 -> {
                clearPresenter()
                enableView()
                message = context.resources.getString(R.string.error_download)

            }
            7 -> {
                message = context.resources.getString(R.string.add_notifications) +
                        " End process."

                clearWaterPresenter()
                enableView()
            }
        }
        activity.runOnUiThread({
            context.toast(message)
        })
    }

    private fun clearPresenter(){
        saveNotificationPresenter.destroy()
        saveCustomerPresenter.destroy()
        addNotificationToTechnicalPresenter.destroy()
    }

    private fun clearWaterPresenter(){
        updateDataRemoteWaterPresenter.destroy()
    }

    override fun <T> executeTask(obj: T) {
        if (obj != null){
            val option = (obj as Int)
            processDownloadNotification(option)
        }else{
            enableView()
        }

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
        activity.runOnUiThread({
            pbDownload!!.visibility = View.VISIBLE
        })
    }

    private fun enableView(){
        activity.runOnUiThread({
            pbDownload!!.visibility = View.INVISIBLE
            enabledButton(true)
        })

    }

}