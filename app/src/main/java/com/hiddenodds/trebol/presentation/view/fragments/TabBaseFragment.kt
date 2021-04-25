package com.hiddenodds.trebol.presentation.view.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import kotlinx.coroutines.*
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
        var observableMessageLoad: Subject<ArrayList<String>> = PublishSubject.create()
        //var observableMessageMaintenance: Subject<ArrayList<String>> = PublishSubject.create()
        val mapImage: LinkedHashMap<String, ProxyBitmap> = LinkedHashMap()
        val pdfGuideModel = PdfGuideModel()
        var notificationModel: NotificationModel? = null
        var nameFileSignature = ""
    }

    val Fragment.app: App
        get() = requireActivity().application as App

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

    private val YES = "YES"
    protected var disposable: CompositeDisposable = CompositeDisposable()

    private lateinit var ltaMaintenance: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        getMaintenancePresenter.view = this
        updateFieldMaintenancePresenter.view = this
        technicalPresenter.view = this
        signaturePresenter.view = this
    }

    private fun listVerification(): ArrayList<String>{
        val values: ArrayList<String> = ArrayList()
        values.add(maintenanceModel!!.verification1)
        values.add(maintenanceModel!!.verification2)
        values.add(maintenanceModel!!.verification3)
        values.add(maintenanceModel!!.verification4)
        values.add(maintenanceModel!!.verification5)
        values.add(maintenanceModel!!.verification6)
        values.add(maintenanceModel!!.verification7)
        values.add(maintenanceModel!!.verification8)
        values.add(maintenanceModel!!.verification9)
        values.add(maintenanceModel!!.verification10)
        values.add(maintenanceModel!!.verification11)
        values.add(maintenanceModel!!.verification12)
        values.add(maintenanceModel!!.verification13)
        values.add(maintenanceModel!!.verification14)
        return values
    }

    private fun listMaintenance(): ArrayList<String>{
        val values: ArrayList<String> = ArrayList()
        values.add(maintenanceModel!!.maintenance1)
        values.add(maintenanceModel!!.maintenance2)
        values.add(maintenanceModel!!.maintenance3)
        values.add(maintenanceModel!!.maintenance4)
        values.add(maintenanceModel!!.maintenance5)
        values.add(maintenanceModel!!.maintenance6)
        values.add(maintenanceModel!!.maintenance7)
        values.add(maintenanceModel!!.maintenance8)
        values.add(maintenanceModel!!.maintenance9)
        values.add(maintenanceModel!!.maintenance10)
        values.add(maintenanceModel!!.maintenance11)
        values.add(maintenanceModel!!.maintenance12)
        values.add(maintenanceModel!!.maintenance13)
        values.add(maintenanceModel!!.maintenance14)
        return values
    }
    override fun <T> executeTask(obj: T) {
        if (obj != null){
            if (obj is MaintenanceModel){
                maintenanceModel = obj
                setCodeNotification()
                messageLoad = YES
                val ltaVerify = listVerification()
                ltaMaintenance = listMaintenance()
                observableMessageLoad.onNext(ltaVerify)
                //observableMessageMaintenance.onNext(ltaMaintenance)
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

            GlobalScope.async {
                updateFieldMaintenancePresenter
                        .updateMaintenance(maintenanceModel!!.id,
                                "codeNotify",
                                codeNotify!!)
            }
        }
    }

    protected fun executeGetMaintenance(){
         GlobalScope.launch {
            println("Codigo notification: $codeNotify")
            if (codeNotify != null){
                getMaintenancePresenter.executeGet(codeNotify!!)
            }

        }

    }

    //@Suppress("DEPRECATION")
    protected fun saveTableToBitmap(sufix: String,
                                    rvVerification: RecyclerView) {
        var heightAllItems: Int

        if (rvVerification.adapter!!.itemCount > 0) {
            val holderSize = rvVerification
                    .findViewHolderForAdapterPosition(0)
            val heightHolder = holderSize!!.itemView.measuredHeight
            heightAllItems = heightHolder * rvVerification.adapter!!.itemCount

            val bigBitmap = Bitmap.createBitmap(rvVerification.measuredWidth,
                    heightAllItems, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bigBitmap)
            var iHeight = 0
            val paint = Paint()
            for (i in 0 until rvVerification.adapter!!.itemCount) {
                val holder = rvVerification
                        .findViewHolderForAdapterPosition(i)

                /*holder!!.itemView.isDrawingCacheEnabled = true
                holder.itemView.buildDrawingCache()
                val bitmap = holder.itemView
                        .drawingCache.copy(Bitmap.Config.ARGB_8888, true)
*/
                val bitmapItem: Bitmap = Bitmap.createBitmap(holder!!.itemView.width,
                        holder.itemView.height, Bitmap.Config.ARGB_8888)
                val canvasItem = Canvas(bitmapItem)
                holder.itemView.draw(canvasItem)

                println("${bitmapItem.height} item: $i")
                canvas.drawBitmap(bitmapItem,
                        0f, iHeight.toFloat(),
                        paint)
                iHeight += bitmapItem.height
                bitmapItem.recycle()
            }

            val proxyBitmap = ProxyBitmap(bigBitmap)

            mapImage[sufix] = proxyBitmap
        }else{
            println("Items vacios")
        }
    }

    protected fun executeGetTechnical(){
        GlobalScope.async {
            if (codeTech != null){
                technicalPresenter.executeGetTechnical(codeTech!!)
            }

        }

    }

    private fun executeGetFileSignature(nameClient: String){
        if (nameClient.isNotEmpty()){
            GlobalScope.async {
                signaturePresenter.executeGetNameFile(nameClient.trim())
            }


        }

    }

    protected fun sendUpdate(nameField: String, value: String){
        GlobalScope.async {
            updateFieldMaintenancePresenter
                    .updateMaintenance(maintenanceModel!!.id,
                            nameField,
                            value)
        }
    }
    @Throws(NullPointerException::class)
    abstract fun buildListOfData(): ArrayList<GuideModel>
    @Throws(NullPointerException::class)
    abstract fun buildListOfData(arrayTemp: ArrayList<String>): ArrayList<GuideModel>
    abstract fun updateField(nameField: String, value: String): Boolean

    protected fun setupRecyclerView(rvVerification: RecyclerView){
        rvVerification.isNestedScrollingEnabled = false
        rvVerification.setHasFixedSize(true)
        rvVerification.layoutManager = LinearLayoutManager(requireActivity(),
                LinearLayoutManager.VERTICAL, false)
        ChangeFormat.addDecorationRecycler(rvVerification, requireContext())

    }


    @Throws(NullPointerException::class)
    protected fun setDataToControl(adapter: ItemTabAdapter,
                                   rvVerification: RecyclerView){
         GlobalScope.async {
            val list = buildListOfData()
            adapter.setObjectList(list)
            requireActivity().runOnUiThread {
                rvVerification.scrollToPosition(0)

            }
         }
    }

    @Throws(NullPointerException::class)
    protected fun setDataToControl(adapter: ItemTabAdapter,
                                   rvVerification: RecyclerView,
                                   arrayTemp: ArrayList<String>){
        GlobalScope.async {
            val list = buildListOfData(arrayTemp)
            adapter.setObjectList(list)
            requireActivity().runOnUiThread {
                rvVerification.scrollToPosition(0)

            }
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
                val sortedList = list.sortedWith(compareBy { it.code })

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
        requireContext().toast(message)
    }

    override fun showMessage(message: String) {
        requireContext().toast(message)
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}