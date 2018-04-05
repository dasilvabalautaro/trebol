package com.hiddenodds.trebolv2.presentation.view.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.github.gcacace.signaturepad.views.SignaturePad
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.presentation.model.EmailModel
import com.hiddenodds.trebolv2.presentation.model.NotificationModel
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import com.hiddenodds.trebolv2.tools.Variables
import kotlinx.coroutines.experimental.async
import java.text.SimpleDateFormat
import java.util.*


class NotificationFinishFragment: NotificationFragment(), ILoadDataView {
    @BindView(R.id.tv_serie)
    @JvmField var tvSerie: TextView? = null
    @BindView(R.id.et_client)
    @JvmField var etClient: EditText? = null
    @BindView(R.id.signatureClient)
    @JvmField var signatureClient: SignaturePad? = null
    @OnClick(R.id.bt_pdf)
    fun savePdf(){
        pdfEndTask.manageImage = this.manageImage
        async {
            pdfEndTask.inflateView()
            pdfEndTask.setData(notificationModel!!.code, etClient!!.text.toString())
            pdfEndTask.saveImage(notificationModel!!.code + PRE_FIX)
        }
    }

    @OnClick(R.id.bt_save)
    fun saveSignature(){
        val signatureBitmap: Bitmap? = signatureClient!!.signatureBitmap
        if (signatureBitmap != null){
            manageImage.image = signatureBitmap
            manageImage.code = notificationModel!!.code
            manageImage.addFileToGallery(activity)

        }else{
            context.toast(context.getString(R.string.image_not_found))
        }
    }

    @OnClick(R.id.bt_clear)
    fun clearSignature(){
        signatureClient!!.clear()
        manageImage.deleteSignatureStore(notificationModel!!.code)

    }

    @OnClick(R.id.bt_email)
    fun buildEmail(){
        val emailModel = bluidEmailModel()
        if (etClient!!.text.toString().isNotEmpty() &&
                emailModel.clip.isNotEmpty() &&
                signatureClient!!.signatureBitmap != null){
            if (notificationModel!!.state != "1"){
                async {
                    updateState("1")
                }
            }
            executeEmail(emailModel)
        }else{
            context.toast(context.getString(R.string.input_error))
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

    private val codeNotification: String by lazy { this.arguments.getString(inputNotification) }
    private val codeTechnical: String by lazy { this.arguments.getString(inputTechnical) }

    private var technicalModel: TechnicalModel? = null
    private var notificationModel: NotificationModel? = null
    private val PRE_FIX = "end"

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_end_install,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        technicalPresenter.view = this
        updateFieldNotificationPresenter.view = this

        async {
            technicalPresenter.executeGetTechnical(codeTechnical)
        }


    }

    override fun showMessage(message: String) {
        if (message == context.getString(R.string.change_field)){
            val tech = technicalModel!!.code
            Variables.changeTechnical = tech
            println("Change technical : $tech")

        }else{
            context.toast(message)
        }
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun <T> executeTask(obj: T) {
        if (obj != null){
            this.technicalModel = (obj as TechnicalModel)
            val listNotification = ArrayList((obj as TechnicalModel).notifications)
            async {
                notificationModel = getNotification(codeNotification, listNotification)
                activity.runOnUiThread({
                    setControls(notificationModel)
                })
                setSignature(notificationModel!!.code)
            }

        }
    }

    private fun setSignature(codeNotification: String){
        val bitmap = manageImage.getSignatureStore(codeNotification)
        if (bitmap != null){
            activity.runOnUiThread({
                signatureClient!!.signatureBitmap = bitmap
            })
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
        emailModel.clip = notificationModel!!.code + PRE_FIX + ".pdf"
        return emailModel
    }


    private fun setControls(notify: NotificationModel?){
        if (notify != null){
            tvSerie!!.text = notify.series
        }
    }
    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}