package com.hiddenodds.trebol.presentation.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.dagger.PresenterModule
import com.hiddenodds.trebol.model.persistent.caching.CachingLruRepository
import com.hiddenodds.trebol.model.persistent.file.FileManager
import com.hiddenodds.trebol.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebol.presentation.presenter.*
import com.hiddenodds.trebol.presentation.view.activities.MainActivity
import com.hiddenodds.trebol.tools.Variables
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.jetbrains.anko.alert
import javax.inject.Inject


class MenuFragment: Fragment(), ILoadDataView {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.pb_download)
    @JvmField var pbDownload: ProgressBar? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_update_water)
    @JvmField var btUpdateWater: Button? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_get)
    @JvmField var btGetNotification: Button? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_update_thinks)
    @JvmField var btGetDataGeneral: Button? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_ots)
    @JvmField var btShowOTS: Button? = null


    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_update_water)
    fun updateDataInWater(){
        setViewForTransferData()
        updateDataRemoteWaterPresenter.executeUpdateRemoteWater()
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_get)
    fun updateDataNotification(){
        requireActivity().alert(R.string.lbl_delete_data) {
            title = "Alerta"
            positiveButton(R.string.lbl_confirm) {
                setViewForTransferData()
                GlobalScope.async {
                    notificationDownloadPresenter.executeDownloadNotification()
                }

            }

            neutralPressed(R.string.lbl_cancel){}

        }.show()

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_update_thinks)
    fun updateDataGeneral(){
        requireActivity().alert(R.string.lbl_update_notification_data) {
            title = "Alerta"
            positiveButton(R.string.lbl_confirm) {
                setViewForTransferData()
                materialRemotePresenter.executeQueryRemote()
            }

            neutralPressed(R.string.lbl_cancel){}

        }.show()

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_ots)
    fun viewOTS(){
        CachingLruRepository.instance.getLru().evictAll()
        val fragmentOtsFragment = OtsFragment()
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, fragmentOtsFragment,
                        fragmentOtsFragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }

    val Fragment.app: App
        get() = requireActivity().application as App

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

    @Inject
    lateinit var fileManager: FileManager

    private var optionExecute = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root: View = inflater.inflate(R.layout.view_menu,
                container,false)
        ButterKnife.bind(this, root)

        return root
    }

    override fun onStart() {
        super.onStart()
        materialRemotePresenter.view = this
        addNotificationToTechnicalPresenter.view = this
        notificationDownloadPresenter.view = this
        customerDownloadPresenter.view = this
        saveNotificationPresenter.view = this
        saveCustomerPresenter.view = this
        updateDataRemoteWaterPresenter.view = this
        requireActivity().theme.applyStyle(R.style.AppTheme, true)

    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).displayHome(false)
    }

    override fun showMessage(message: String) {
        requireActivity().toast(message)
    }

    override fun showError(message: String) {
        enableView()
        requireActivity().toast(message)
    }

    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun processDownloadNotification(option: Int){
        var message = ""

        if (this.optionExecute != option){
            this.optionExecute = option
            when(option){
                1 -> {
                    Thread.sleep(3000)
                    message = requireContext().resources.getString(R.string.notification_download)
                    customerDownloadPresenter.executeDownloadCustomer()

                }
                2 -> {
                    message = requireContext().resources.getString(R.string.customer_download)
                    saveNotificationPresenter.executeSaveNotification()


                }
                3 -> {
                    message = requireContext().resources.getString(R.string.notification_save)
                    saveCustomerPresenter.executeGetCustomer()


                }

                4 -> {
                    message = requireContext().resources.getString(R.string.customer_save)
                    addNotificationToTechnicalPresenter.executeAddNotification()

                }

                5 -> {
                    message = requireContext().resources.getString(R.string.add_notifications) + "\n" +
                            requireContext().getString(R.string.lbl_finish_process)

                    //clearPresenter()
                    enableView()
                }

                6 -> {
                    //clearPresenter()
                    enableView()
                    message = requireContext().resources.getString(R.string.error_download)

                }
                7 -> {
                    message = requireContext().resources.getString(R.string.add_notifications) + "\n" +
                            requireContext().getString(R.string.lbl_finish_process)

                    enableView()
                }
                8 -> {
                    message = requireContext().getString(R.string.download_complete)
                    enableView()
                }
            }
            requireActivity().runOnUiThread {
                requireContext().toast(message)
            }

        }

    }

    private fun clearPresenter(){
        saveNotificationPresenter.destroy()
        saveCustomerPresenter.destroy()
        addNotificationToTechnicalPresenter.destroy()
        notificationDownloadPresenter.destroy()
        customerDownloadPresenter.destroy()
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

    override fun onDestroy() {
        super.onDestroy()
        if (Variables.endApp){
            clearWaterPresenter()
            clearPresenter()
            (requireActivity() as MainActivity).handleBackPressInThisActivity()
        }else{
            Variables.endApp = true
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
        this.optionExecute = 0
        enabledButton(false)
        requireContext().toast(requireContext().resources.getString(R.string.wait_task))
        requireActivity().runOnUiThread {
            pbDownload!!.visibility = View.VISIBLE
        }
    }

    private fun enableView(){
        requireActivity().runOnUiThread {
            pbDownload!!.visibility = View.INVISIBLE
            enabledButton(true)
        }

    }

}