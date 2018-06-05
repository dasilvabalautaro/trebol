package com.hiddenodds.trebol.presentation.view.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.dagger.PresenterModule
import com.hiddenodds.trebol.presentation.components.ItemTabAdapter
import com.hiddenodds.trebol.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebol.presentation.model.*
import com.hiddenodds.trebol.presentation.presenter.GetMaintenancePresenter
import com.hiddenodds.trebol.presentation.presenter.SignaturePresenter
import com.hiddenodds.trebol.presentation.presenter.TechnicalPresenter
import com.hiddenodds.trebol.presentation.presenter.UpdateFieldMaintenancePresenter
import com.hiddenodds.trebol.tools.ChangeFormat
import com.hiddenodds.trebol.tools.ManageImage
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.set



abstract class TabBaseFragment: Fragment(), ILoadDataView {
    companion object Factory{
        var maintenanceModel: MaintenanceModel? = null
        var technicalModel: TechnicalModel? = null
        var outState: Bundle? = null
        var codeNotify: String? = null
        var codeTech: String? = null
        var messageLoad = "NO"
        var observableMessageLoad: Subject<String> = PublishSubject.create()
        val mapImage: LinkedHashMap<String, ProxyBitmap> = LinkedHashMap()
        val pdfGuideModel = PdfGuideModel()
        var notificationModel: NotificationModel? = null
        var nameFileSignature = ""
    }

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}

    @Inject
    lateinit var getMaintenancePresenter: GetMaintenancePresenter
    @Inject
    lateinit var updateFieldMaintenancePresenter: UpdateFieldMaintenancePresenter
    @Inject
    lateinit var manageImage: ManageImage
    @Inject
    lateinit var technicalPresenter: TechnicalPresenter
    @Inject
    lateinit var signaturePresenter: SignaturePresenter

    protected val YES = "YES"
    protected var disposable: CompositeDisposable = CompositeDisposable()


    init {
        observableMessageLoad
                .subscribe { messageLoad }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        getMaintenancePresenter.view = this
        updateFieldMaintenancePresenter.view = this
        technicalPresenter.view = this
        signaturePresenter.view = this
    }

    override fun <T> executeTask(obj: T) {
        if (obj != null){
            if (obj is MaintenanceModel){
                maintenanceModel = obj
                setCodeNotification()
                messageLoad = YES
                observableMessageLoad.onNext(messageLoad)
            }
            if (obj is TechnicalModel){
                technicalModel = obj
                notificationModel = getNotification(maintenanceModel!!.codeNotify)
                executeGetFileSignature(notificationModel!!.businessName)
            }
            if (obj is String && obj.isNotEmpty()){
                nameFileSignature = obj
            }

        }
    }

    private fun setCodeNotification(){
        if (maintenanceModel!!.codeNotify.isEmpty()){
            maintenanceModel!!.codeNotify = codeNotify!!

            async {
                updateFieldMaintenancePresenter
                        .updateMaintenance(maintenanceModel!!.id,
                                "codeNotify",
                                codeNotify!!)
            }
        }
    }

    protected fun executeGetMaintenance(){
        launch(CommonPool) {
            println("Codigo notification: $codeNotify")
            if (codeNotify != null){
                getMaintenancePresenter.executeGet(codeNotify!!)
            }

        }

    }

    protected fun saveTableToBitmap(sufix: String,
                          rvVerification: RecyclerView) {
        var heightAllItems = 0

        if (rvVerification.adapter.itemCount > 0) {
            val holderSize = rvVerification
                    .findViewHolderForAdapterPosition(0)
            val heightHolder = holderSize.itemView.measuredHeight
            heightAllItems = heightHolder * rvVerification.adapter.itemCount

            val bigBitmap = Bitmap.createBitmap(rvVerification.measuredWidth,
                    heightAllItems, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bigBitmap)
            var iHeight = 0
            val paint = Paint()
            for (i in 0 until rvVerification.adapter.itemCount) {
                val holder = rvVerification
                        .findViewHolderForAdapterPosition(i)

                holder.itemView.isDrawingCacheEnabled = true
                holder.itemView.buildDrawingCache()
                val bitmap = holder.itemView
                        .drawingCache.copy(Bitmap.Config.ARGB_8888, true)
                println("${bitmap.height} item: $i")
                canvas.drawBitmap(bitmap,
                        0f, iHeight.toFloat(),
                        paint)
                iHeight += bitmap.height
                bitmap.recycle()
            }

            val proxyBitmap = ProxyBitmap(bigBitmap)

            mapImage[sufix] = proxyBitmap
        }else{
            println("Items vacios")
        }
    }

    protected fun executeGetTechnical(){
        async(CommonPool) {
            if (codeTech != null){
                technicalPresenter.executeGetTechnical(codeTech!!)
            }

        }

    }

    private fun executeGetFileSignature(nameClient: String){
        if (nameClient.isNotEmpty()){
            async {
                signaturePresenter.executeGetNameFile(nameClient.trim())
            }


        }

    }

    protected fun sendUpdate(nameField: String, value: String){
        async {
            updateFieldMaintenancePresenter
                    .updateMaintenance(maintenanceModel!!.id,
                            nameField,
                            value)
        }
    }
    @Throws(NullPointerException::class)
    abstract fun buildListOfData(): ArrayList<GuideModel>
    abstract fun updateField(nameField: String, value: String): Boolean

    protected fun setupRecyclerView(rvVerification: RecyclerView){
        rvVerification.isNestedScrollingEnabled = false
        rvVerification.setHasFixedSize(true)
        rvVerification.layoutManager = LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeFormat.addDecorationRecycler(rvVerification, context)
        }

    }


    @Throws(NullPointerException::class)
    protected fun setDataToControl(adapter: ItemTabAdapter,
                                   rvVerification: RecyclerView){

         async {
            val list = buildListOfData()
            adapter.setObjectList(list)
            activity.runOnUiThread({
                rvVerification.scrollToPosition(0)
            })
        }


    }

    protected fun isCreateImage(rvVerification: RecyclerView?,
                                flagChange: Boolean, sufix: String):Boolean{
        if (rvVerification == null) return false
        return if (!mapImage.containsKey(sufix)){
            true
        }else{
            flagChange
        }
    }

    private fun getNotification(codeNotify: String):
            NotificationModel? {

        if (technicalModel != null){
            val list = technicalModel!!.notifications

            if (list.isNotEmpty()){
                val sortedList = list.sortedWith(compareBy({ it.code }))

                sortedList.forEach { notify: NotificationModel ->
                    if (notify.code == codeNotify){
                        pdfGuideModel.client = notify.businessName
                        pdfGuideModel.series = notify.series
                        pdfGuideModel.part = notify.code
                        return notify
                    }
                }
            }

        }

        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        getMaintenancePresenter.destroy()
    }

    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun showMessage(message: String) {
        context.toast(message)
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}