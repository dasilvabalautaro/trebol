package com.hiddenodds.trebolv2.presentation.view.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import butterknife.OnTextChanged
import com.github.gcacace.signaturepad.views.SignaturePad
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.persistent.file.ManageFile
import com.hiddenodds.trebolv2.presentation.components.ItemTabAdapter
import com.hiddenodds.trebolv2.presentation.model.EmailModel
import com.hiddenodds.trebolv2.presentation.model.GuideModel
import com.hiddenodds.trebolv2.presentation.model.PdfGuideModel
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.coroutines.experimental.async
import java.text.SimpleDateFormat
import java.util.*


class TabObservationFragment: TabBaseFragment() {
    val SUFIX_TECHNICAL = "_t"
    val SUFIX_CLIENT = "_c"
    private val sufix = "_t3"
    private var adapter: ItemTabAdapter? = null
    private var flagChange = false

    @BindView(R.id.sv_tab)
    @JvmField var svTab: NestedScrollView? = null
    @BindView(R.id.rv_verification)
    @JvmField var rvVerification: RecyclerView? = null
    @BindView(R.id.et_report)
    @JvmField var etReport: EditText? = null
    @BindView(R.id.sp_technical)
    @JvmField var spTechnical: SignaturePad? = null
    @BindView(R.id.sp_client)
    @JvmField var spClient: SignaturePad? = null
    @OnTextChanged(R.id.et_report)
    fun changeReportTechical(){
        if (maintenanceModel != null){
            val value = etReport!!.text.toString()
            val fieldName = "reportTechnical"
            if (maintenanceModel!!.reportTechnical != etReport!!.text.toString()){
                async {
                    sendUpdate(fieldName, value)
                    pdfGuideModel.observations = value
                }
                maintenanceModel!!.reportTechnical = etReport!!.text.toString()

            }
        }
    }

    @OnClick(R.id.bt_save_technical)
    fun saveSignatureTechnical(){

        val signatureBitmap: Bitmap? = spTechnical!!.signatureBitmap
        if (signatureBitmap != null){
            pdfGuideModel.signatureTechnical = maintenanceModel!!.id + SUFIX_TECHNICAL
            manageImage.image = signatureBitmap
            manageImage.code = maintenanceModel!!.id + SUFIX_TECHNICAL
            manageImage.addFileToGallery(activity)

        }else{
            context.toast(context.getString(R.string.image_not_found))
        }
    }

    @OnClick(R.id.bt_clear_technical)
    fun clearSignatureTechnical(){
        spTechnical!!.clear()
        val file = maintenanceModel!!.id + SUFIX_TECHNICAL
        manageImage.deleteSignatureStore(file)
        pdfGuideModel.signatureTechnical = ""
    }

    @OnClick(R.id.bt_save_client)
    fun saveSignatureClient(){
        val signatureBitmap: Bitmap? = spClient!!.signatureBitmap
        if (signatureBitmap != null){
            pdfGuideModel.signatureClient = nameFileSignature
            manageImage.image = signatureBitmap
            manageImage.code = nameFileSignature
            manageImage.addFileToGallery(activity)

        }else{
            context.toast(context.getString(R.string.image_not_found))
        }
    }

    @OnClick(R.id.bt_clear_client)
    fun clearSignatureClient(){
        spClient!!.clear()
        val file = nameFileSignature
        manageImage.deleteSignatureStore(file)
        pdfGuideModel.signatureClient = ""
    }
    @OnClick(R.id.bt_email)
    fun callEmail(){
        if (notificationModel != null){
            executeEmail(buildEmailModel())
        }else{
            println("notificationModel not exist")
        }

    }

    @OnClick(R.id.bt_pdf)
    fun savePdf(){
        setTableToBitmap()
        Thread.sleep(1000)
        if (mapImage.size == 4){
            pdfGuideModel.mapImage = mapImage
            pdfGuideModel.id = maintenanceModel!!.id
            executePdfForm(pdfGuideModel)

        }else{
            context.toast("Datos incompletos. Verifique las tablas y las firmas.")
        }

    }

    @OnClick(R.id.bt_view_pdf)
    fun viewPdf(){
        if (ManageFile.isFileExist("${maintenanceModel!!.id}.pdf")){
            val pdfViewFragment = PdfViewFragment
                    .newInstance(maintenanceModel!!.id, "tech")
            activity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flContent, pdfViewFragment,
                            pdfViewFragment.javaClass.simpleName)
                    .addToBackStack(null)
                    .commit()
        }else{
            context.toast(context.getString(R.string.file_not_found))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater!!.inflate(R.layout.view_tab_observations,
                container,false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ButterKnife.bind(this, view!!)
        setupRecyclerView(rvVerification!!)
        setAdapter()
    }

    override fun onStart() {
        super.onStart()
        val message = manageImage.observableMessage.map { m -> m }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    context.toast(s)
                })

        try {
            setDataToControl(adapter!!, rvVerification!!)
            setDataControlLocal()
        }catch (ne: NullPointerException){
            println(ne.message)
        }

    }
    fun setTableToBitmap() {
        if (rvVerification != null) rvVerification!!.clearFocus()
        if (isCreateImage(rvVerification, flagChange, sufix)){

            async {
                saveTableToBitmap(sufix, rvVerification!!)
                pdfGuideModel.nameKnow = sufix
            }

            flagChange = false

        }
    }

    private fun setAdapter(){
        adapter = ItemTabAdapter{
            flagChange = updateField(it.nameField, it.value)
        }

        rvVerification!!.adapter = adapter
    }

    @Throws(NullPointerException::class)
    private fun setDataControlLocal(){
        val fileTechnical = maintenanceModel!!.id + SUFIX_TECHNICAL
        val fileClient = nameFileSignature
        async {
            if (setSignature(fileTechnical, spTechnical!!)){
                pdfGuideModel.signatureTechnical = fileTechnical
                println("File Technical: $fileTechnical")
            }
            if (setSignature(fileClient, spClient!!)){
                pdfGuideModel.signatureClient = fileClient
                println("File Technical: $fileClient")
            }
        }


        etReport!!.setText(maintenanceModel!!.reportTechnical)
        if (etReport!!.text.toString().isNotEmpty()){
            pdfGuideModel.observations = etReport!!.text.toString()
        }

    }

    override fun buildListOfData(): ArrayList<GuideModel>{

        val lbl = listOf("security", "documentation",
                "knowPrint", "nextHours")

        val items: ArrayList<GuideModel> = ArrayList()
        val labels = context
                .resources.getStringArray(R.array.lbl_observations)
        val values: ArrayList<String> = ArrayList()
        values.add(maintenanceModel!!.security)
        values.add(maintenanceModel!!.documentation)
        values.add(maintenanceModel!!.knowPrint)
        values.add(maintenanceModel!!.nextHours)

        for (i in labels.indices){
            val guideModel = GuideModel()
            guideModel.description = labels[i]
            guideModel.value = values[i]
            guideModel.nameField = lbl[i]
            if (i == 3){
                guideModel.free = 1
            }
            items.add(guideModel)
        }

        return items
    }

    override fun updateField(nameField: String, value: String): Boolean{
        var flag = false
        if (maintenanceModel != null){
            when(nameField){
                maintenanceModel!!::security.name -> {
                    if (maintenanceModel!!.security != value){
                        flag = true
                        maintenanceModel!!.security = value
                    }

                }
                maintenanceModel!!::documentation.name -> {
                    if (maintenanceModel!!.documentation != value){
                        flag = true
                        maintenanceModel!!.documentation = value
                    }
                }
                maintenanceModel!!::knowPrint.name -> {
                    if (maintenanceModel!!.knowPrint != value){
                        flag = true
                        maintenanceModel!!.knowPrint = value
                    }
                }
                maintenanceModel!!::nextHours.name -> {
                    if (maintenanceModel!!.nextHours != value){
                        flag = true
                        maintenanceModel!!.nextHours = value
                    }
                }
            }

            if (flag){
                sendUpdate(nameField, value)
            }

        }
        return flag
    }

    private fun setSignature(codeNotification: String,
                             signature: SignaturePad): Boolean{
        var result = false
        manageImage.code = codeNotification
        val bitmap = manageImage.getFileOfGallery(activity)
        if (bitmap != null){
            activity.runOnUiThread({
                signature.signatureBitmap = bitmap
            })
            result = true
        }
        return result
    }

    private fun buildEmailModel(): EmailModel {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateWork = sdf.format(Date())
        var whoFor = ""
        if (notificationModel!!.customer != null){
            whoFor = notificationModel!!.customer!!.email
        }
        val emailModel = EmailModel()
        emailModel.title = "GUÍA DE MANTENIMIENTO"
        emailModel.whoOf = "servicio.tecnico@trebolgroup.com"
        emailModel.whoFor = whoFor
        emailModel.whoCopy = "servicio.tecnico@trebolgroup.com; ${technicalModel!!.email}"
        emailModel.subject = "GDM ${notificationModel!!.code}, " +
                "Cliente: ${notificationModel!!.businessName}, " +
                "Máquina: ${notificationModel!!.machine}"
        emailModel.message = "Estimado cliente, adjunto le enviamos el " +
                " documento de Guía de mantenimiento, correspondiente " +
                " a la revisión efectuada en sus instalaciones el día $dateWork .\n" +
                "Reciba un Cordial Saludo.\n" +
                "Trebol Group Providers S.L."
        emailModel.client = notificationModel!!.businessName
        emailModel.clip = maintenanceModel!!.id + ".pdf"
        return emailModel
    }

    private fun executeEmail(emailModel: EmailModel){
        val emailFragment = EmailFragment.newInstance(emailModel)
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, emailFragment,
                        emailFragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }

    private fun executePdfForm(pdfGuideModel: PdfGuideModel){
        val pdfTabFragment = PdfTabFragment
                .newInstance(pdfGuideModel, buildEmailModel())
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, pdfTabFragment,
                        pdfTabFragment.javaClass.simpleName)
                .addToBackStack(null)
                .commit()
    }

}