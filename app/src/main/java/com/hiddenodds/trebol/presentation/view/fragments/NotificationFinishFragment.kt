package com.hiddenodds.trebol.presentation.view.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.gcacace.signaturepad.views.SignaturePad
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.persistent.file.ManageFile
import com.hiddenodds.trebol.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebol.presentation.model.EmailModel
import com.hiddenodds.trebol.presentation.model.NotificationModel
import com.hiddenodds.trebol.presentation.model.TechnicalModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.util.*


class NotificationFinishFragment: NotificationFragment(), ILoadDataView {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_serie)
    @JvmField var tvSerie: TextView? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_client)
    @JvmField var etClient: EditText? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.signatureClient)
    @JvmField var signatureClient: SignaturePad? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.bt_email)
    @JvmField var btEmail: Button? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_subtitle)
    @JvmField var tvSubtitle: TextView? = null

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.bt_pdf)
    fun savePdf() = runBlocking{
        val job = GlobalScope.async {
            pdfEndTask.manageImage = manageImage
            val client = etClient!!.text.toString()
            pdfEndTask.inflateView()
            codeNotification?.let { pdfEndTask.setData(it, client, nameFileSignature) }
            pdfEndTask.saveImage("$codeNotification$preFix")
        }
        job.join()

        requireActivity().runOnUiThread {
            viewPdf()
        }
    }




    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.bt_save)
    fun saveSignature(){
        val signatureBitmap: Bitmap? = signatureClient!!.signatureBitmap
        if (signatureBitmap != null){
            manageImage.image = signatureBitmap
            manageImage.code = nameFileSignature
            manageImage.addFileToGallery(requireActivity())

        }else{
            requireContext().toast(requireContext().getString(R.string.image_not_found))
        }
    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.bt_clear)
    fun clearSignature(){
        signatureClient!!.clear()
        manageImage.deleteSignatureStore(nameFileSignature)

    }

    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.bt_email)
    fun buildEmail(){
        val emailModel = bluidEmailModel()
        if (etClient!!.text.toString().isNotEmpty() &&
                emailModel.clip.isNotEmpty() &&
                signatureClient!!.signatureBitmap != null){
            if (notificationModel!!.state != "1"){
                GlobalScope.async {
                    updateState("1")
                }
            }
            executeEmail(emailModel)
        }else{
            requireContext().toast(requireContext().getString(R.string.input_error))
        }

    }

    companion object Factory {
        private const val inputNotification = "notify_"
        private const val inputTechnical = "technical_"
        fun newInstance(arg1: String? = null, arg2: String? = null):
                NotificationFinishFragment = NotificationFinishFragment().apply{
            this.arguments = Bundle().apply {
                this.putString(inputNotification, arg1)
                this.putString(inputTechnical, arg2)
            }

        }
    }

    private val codeNotification: String? by lazy { this.requireArguments()
            .getString(inputNotification) }
    private val codeTechnical: String? by lazy { this.requireArguments()
            .getString(inputTechnical) }

    private var technicalModel: TechnicalModel? = null
    private var notificationModel: NotificationModel? = null
    private val preFix = "end"
    private var nameFileSignature = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root: View = inflater.inflate(R.layout.view_end_install,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onStart() {
        super.onStart()
        technicalPresenter.view = this
        updateFieldNotificationPresenter.view = this
        signaturePresenter.view = this
        btEmail!!.visibility = View.INVISIBLE
        GlobalScope.async {
            codeTechnical?.let { technicalPresenter.executeGetTechnical(it) }
        }


    }

    override fun showMessage(message: String) {
        if (message == requireContext().getString(R.string.change_field)){
            requireContext().toast(requireContext().getString(R.string.change_good))

        }else{
            requireContext().toast(message)
        }
    }

    override fun showError(message: String) {
        requireContext().toast(message)
    }

    override fun <T> executeTask(obj: T) {
        if (obj != null){
            if (obj is TechnicalModel){
                this.technicalModel = obj
                setTechnical()
            }else if (obj is String && obj.isNotEmpty()){
                this.nameFileSignature = obj
                setSignature(obj)
            }
        }
    }

    private fun setTechnical(){
        val listNotification = ArrayList(this
                .technicalModel!!.notifications)
        GlobalScope.async {
            notificationModel = codeNotification?.let { getNotification(it, listNotification) }
            requireActivity().runOnUiThread {
                setControls(notificationModel)

            }
            if (notificationModel!!.businessName.isNotEmpty()){

                signaturePresenter.executeGetNameFile(notificationModel!!
                        .businessName.trim())

            }
        }
    }

    private fun viewPdf(){
        if (ManageFile.isFileExist("$codeNotification$preFix.pdf")){
            val pdfViewFragment = PdfViewFragment
                    .newInstance(codeNotification + preFix,
                            codeTechnical, bluidEmailModel())
            requireActivity().supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.flContent, pdfViewFragment,
                            pdfViewFragment.javaClass.simpleName)
                    .commit()
        }else{
            requireContext().toast(requireContext().getString(R.string.file_not_found))
        }
    }

    private fun setSignature(codeNotification: String){
        manageImage.code = codeNotification
        val bitmap = manageImage.getFileOfGallery(requireActivity())
        if (bitmap != null){
            requireActivity().runOnUiThread {
                signatureClient!!.signatureBitmap = bitmap
            }
        }
    }

    private fun updateState(value: String){
        when(value){
            "0" -> {
                updateFieldNotificationPresenter
                        .updateNotification(notificationModel!!.id,
                                "state", "0")
                updateFieldNotificationPresenter
                        .updateNotification(notificationModel!!.id,
                                "dateEnd", "")

            }
            "1" -> {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val dateEnd = dateFormat.format(Date())
                updateFieldNotificationPresenter
                        .updateNotification(notificationModel!!.id,
                                "state", "1")
                updateFieldNotificationPresenter
                        .updateNotification(notificationModel!!.id,
                                "dateEnd", dateEnd)
            }
        }

    }

    private fun bluidEmailModel(): EmailModel {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateWork = sdf.format(Date())
        var whoFor = ""
        if (notificationModel!!.customer != null){
            whoFor = notificationModel!!.customer!!.email
        }
        val emailModel = EmailModel()
        emailModel.title = "OT Nº: ${notificationModel!!.code}"
        emailModel.whoOf = technicalModel!!.email
        emailModel.whoFor = whoFor
        emailModel.whoCopy = "instalaciones@trebolgroup.com; ${technicalModel!!.email}"
        emailModel.subject = "DFI OT Nº: ${notificationModel!!.code}, " +
                "Cliente: ${notificationModel!!.businessName}, " +
                "Máquina Nº: ${tvSerie!!.text}"
        emailModel.message = "Estimado cliente, adjunto le enviamos copia " +
                "del documento firmado de aceptación de fin de instalación.\n" +
                "En el día de: $dateWork .\n" +
                "Reciba un Cordial Saludo.\n" +
                "Trebol Group Providers S.L."
        emailModel.client = etClient!!.text.toString()
        emailModel.clip = notificationModel!!.code + preFix + ".pdf"
        return emailModel
    }


    private fun setControls(notify: NotificationModel?){
        if (notify != null){
            tvSerie!!.text = notify.series
        }
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateWork = sdf.format(Date())
        tvSubtitle!!.text = String.format("Habiendo procedido la empresa TREBOL GROUP PROVIDERS S.L. a la instalación de la " +
                "impresora/máquina cuyo número de instalación es %s, en fecha %s, afirmo y acredito:\n" +
                "Que la misma ha sido debida, total y adecuadamente instalada.\n" +
                "Así mismo, certifico mediante la presente, que tras la instalación de la misma, se ha procedido a " +
                "la comprobación de su correcto funcionamiento ante mi persona, habiendo podido comprobar su " +
                "eficaz y correcto funcionamiento.\n" +
                "Y, en prueba de lo antedicho, firmo la presente certificación.\n\n" +
                "Fdo: ", codeNotification, dateWork)
    }
    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}