package com.hiddenodds.trebol.presentation.components

import android.app.Service
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.presentation.model.AssignedMaterialModel
import com.hiddenodds.trebol.presentation.model.NotificationModel
import com.hiddenodds.trebol.presentation.model.TechnicalModel
import com.hiddenodds.trebol.tools.ChangeFormat
import com.hiddenodds.trebol.tools.ManageImage
import javax.inject.Inject

class PdfNotification @Inject constructor() {
    private val context = App.appComponent.context()
    private val activity = App.appComponent.activity()
    private var adapterMaterialUse: ItemProductSelectAdapter? = null
    private var adapterMaterialOut: ItemProductSelectAdapter? = null
    private var view: View? = null
    private val pdfNotificationView = PdfNotificationView(context)
    private var codeNotification = ""
    private val SUB_FIX = "_n"
    var manageImage: ManageImage? = null

    fun inflateView(){
        val viewGroup: ViewGroup = pdfNotificationView.rootView as ViewGroup
        val inflater = context
                .getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater
                .inflate(R.layout.view_pdf_notification_frame, viewGroup,
                        false) as PdfNotificationView

        initControls()
    }

    private fun initControls(){
        setupRecyclerViewMaterialOut()
        setupRecyclerViewMaterialUse()
    }

    fun setData(notify: NotificationModel,
                technical: TechnicalModel, nameFile: String){

        this.codeNotification = notify.code
        val title = "OT Nº: " + notify.code
        (view as PdfNotificationView).lbl_title!!.text = title
        (view as PdfNotificationView).txtComercial!!.text = notify.trade
        (view as PdfNotificationView).txtMod!!.text = notify.product
        (view as PdfNotificationView).txtSerie!!.text = notify.series
        (view as PdfNotificationView).txtTinta!!.text = notify.ink
        (view as PdfNotificationView).txtPeaje!!.text = notify.peaje
        (view as PdfNotificationView).txtSat!!.text = notify.satd
        (view as PdfNotificationView).txtSatdk!!.text = notify.satdk
        (view as PdfNotificationView).txtSintomas!!.text = notify.symptom
        (view as PdfNotificationView).txtNameTech!!.text = technical.name
        (view as PdfNotificationView).txtContract!!.text = ""
        (view as PdfNotificationView).txtFinish!!.text = notify.dateEnd
        (view as PdfNotificationView).edtVsoft1!!.text = notify.vSoft1
        (view as PdfNotificationView).edtVsoft2!!.text = notify.vSoft2
        (view as PdfNotificationView).edtVsoft3!!.text = notify.vSoft3
        (view as PdfNotificationView).edtHrs!!.text = notify.hours
        (view as PdfNotificationView).edtTotalesEquipo!!.text = notify.totalTeam
        (view as PdfNotificationView).edtUltimoMnto!!.text = notify.lastAmount
        (view as PdfNotificationView).edtInformeTecnico!!.text = notify.reportTechnical
        (view as PdfNotificationView).edtObservaciones!!.text = notify.observations
        (view as PdfNotificationView).txtHrsTrabajo!!.text = notify.workHours
        (view as PdfNotificationView).spnEntrada!!.text = notify.inside
        (view as PdfNotificationView).spnSalida!!.text = notify.outside
        (view as PdfNotificationView).spnDieta!!.text = notify.diet
        setSignature(nameFile)
        adapterMaterialUse!!.setObjectList(changeIcon(notify.materialUse))
        adapterMaterialOut!!.setObjectList(changeIcon(notify.materialOut))
        (view as PdfNotificationView).rvMatUse!!.scrollToPosition(0)
        (view as PdfNotificationView).rvMatOut!!.scrollToPosition(0)

    }

    private fun changeIcon(list: ArrayList<AssignedMaterialModel>):
            ArrayList<AssignedMaterialModel>{
        val materialChange: ArrayList<AssignedMaterialModel> = ArrayList(list)
        for (i in materialChange.indices){
            materialChange[i].change = 3
        }
        return materialChange
    }

    private fun setSignature(codeNotify: String){
        manageImage!!.code = codeNotify
        val bitmap = manageImage!!.getFileOfGallery(activity)
        if (bitmap != null){
            (view as PdfNotificationView).signatureClient!!.setImageBitmap(bitmap)
        }

    }

    private fun setupRecyclerViewMaterialUse(){
        (view as PdfNotificationView).rvMatUse!!.setHasFixedSize(true)
        (view as PdfNotificationView).rvMatUse!!
                .layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeFormat.addDecorationRecycler((view as PdfNotificationView)
                    .rvMatUse!!, context)
        }
        adapterMaterialUse = ItemProductSelectAdapter{
            println(it.id)
        }
        (view as PdfNotificationView).rvMatUse!!.adapter = adapterMaterialUse
    }

    private fun setupRecyclerViewMaterialOut(){
        (view as PdfNotificationView).rvMatOut!!.setHasFixedSize(true)
        (view as PdfNotificationView).rvMatOut!!
                .layoutManager = LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ChangeFormat.addDecorationRecycler((view as PdfNotificationView)
                    .rvMatOut!!, context)
        }
        adapterMaterialOut = ItemProductSelectAdapter{
            println(it.id)
        }
        (view as PdfNotificationView).rvMatOut!!.adapter = adapterMaterialOut
    }

    fun saveImage(){
        view!!.measure(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)

        val viewSv: View = view!!.findViewById(R.id.sv_pdf_notification)
        val svScroll: ScrollView = view!!.findViewById(R.id.sv_pdf_notification)


        val nameFile = this.codeNotification

        val viewBitmap : Bitmap = Bitmap.createBitmap(svScroll.getChildAt(0).measuredWidth,
                svScroll.getChildAt(0).measuredHeight, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(viewBitmap)
        view!!.layout(viewSv.left, viewSv.top, viewSv.right, viewSv.bottom)
        view!!.draw(canvas)

        manageImage!!.image = viewBitmap
        manageImage!!.code = nameFile
        manageImage!!.flagPdf = true
        manageImage!!.addFileToGallery(activity)

    }



}