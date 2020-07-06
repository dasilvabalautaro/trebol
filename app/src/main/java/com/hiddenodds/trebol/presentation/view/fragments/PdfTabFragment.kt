package com.hiddenodds.trebol.presentation.view.fragments

import android.content.Context
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.*
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.dagger.PresenterModule
import com.hiddenodds.trebol.presentation.model.EmailModel
import com.hiddenodds.trebol.presentation.model.PdfGuideModel
import com.hiddenodds.trebol.presentation.view.activities.MainActivity
import com.hiddenodds.trebol.tools.ManageImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class PdfTabFragment: Fragment() {
    @BindView(R.id.tv_name)
    @JvmField var tvName: TextView? = null
    @BindView(R.id.tv_number_part)
    @JvmField var tvNumberPart: TextView? = null
    @BindView(R.id.tv_number_serie)
    @JvmField var tvNumberSerie: TextView? = null
    @BindView(R.id.tv_value_date)
    @JvmField var tvValueDate: TextView? = null
    @BindView(R.id.iv_verification)
    @JvmField var ivVerification: ImageView? = null
    @BindView(R.id.iv_test)
    @JvmField var ivTest: ImageView? = null
    @BindView(R.id.iv_maintenance)
    @JvmField var ivMaintenance: ImageView? = null
    @BindView(R.id.iv_know)
    @JvmField var ivKnow: ImageView? = null
    @BindView(R.id.tv_observations)
    @JvmField var tvObservations: TextView? = null
    @BindView(R.id.iv_signature_technical)
    @JvmField var ivSignatureTechnical: ImageView? = null
    @BindView(R.id.iv_signature_client)
    @JvmField var ivSignatureClient: ImageView? = null
    @BindView(R.id.tv_name_technical)
    @JvmField var tvNameTechnical: TextView? = null
    @BindView(R.id.tv_name_client)
    @JvmField var tvNameClient: TextView? = null

    val Fragment.app: App
        get() = activity!!.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}

    @Inject
    lateinit var manageImage: ManageImage

    var disposable: CompositeDisposable = CompositeDisposable()

    companion object Factory {
        private const val inputPdfGuideModel = "model_"
        private const val inputEmailModel = "email_"
        fun newInstance(arg1: PdfGuideModel? = null, arg2: EmailModel? = null):
                PdfTabFragment = PdfTabFragment().apply{
            this.arguments = Bundle().apply {
                this.putSerializable(inputPdfGuideModel, arg1)
                this.putSerializable(inputEmailModel, arg2)
            }

        }
    }

    private val pdfGuideModel: PdfGuideModel by lazy { this.arguments!!
            .getSerializable(inputPdfGuideModel) as PdfGuideModel
    }

    private val emailModel: EmailModel by lazy { this.arguments!!
            .getSerializable(inputEmailModel) as EmailModel
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
        setHasOptionsMenu(true)

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater.inflate(R.layout.view_pdf_tab,
                container,false)
        ButterKnife.bind(this, root)
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity!! as MainActivity).displayHome(true)
        val message = manageImage.observableMessage.map { m -> m }
        disposable.add(message.observeOn(AndroidSchedulers.mainThread())
                .subscribe { s ->
                    context!!.toast(s)
                })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_email){
            emailModel.clip = pdfGuideModel.id + ".pdf"
            executeEmail(emailModel)
        }
        if (id == R.id.action_save){
            saveViewPdf()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val dateWork = sdf.format(Date())
        tvName!!.text = pdfGuideModel.client
        tvNumberPart!!.text = pdfGuideModel.part
        tvNumberSerie!!.text = pdfGuideModel.series
        tvValueDate!!.text = dateWork
        tvObservations!!.text = pdfGuideModel.observations
        tvNameClient!!.text = pdfGuideModel.clientSignature
        tvNameTechnical!!.text = pdfGuideModel.nameTechnical
        setImage()
    }

    override fun onPause() {
        super.onPause()
        activity!!.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
    }

    private fun getImageFile(name: String): Bitmap?{
        manageImage.code = name
        return manageImage.getFileOfGallery(activity!!)
    }


    private fun getImage(key: String): Bitmap?{
        var bitmap: Bitmap? = null

        if (pdfGuideModel.mapImage.containsKey(key) &&
                pdfGuideModel.mapImage[key] != null){
            bitmap = manageImage.scaleBitmap(pdfGuideModel.mapImage[key]!!.getBitmap())
        }

        return bitmap
    }

    private fun setImage(){
        GlobalScope.async {

            val bitmap = getImage(pdfGuideModel.nameVerification)
            if (bitmap != null){
                activity!!.runOnUiThread {
                    ivVerification!!.setImageBitmap(bitmap)
                }

            }
        }
        GlobalScope.async {

            val bitmap = getImage(pdfGuideModel.nameTest)
            if (bitmap != null){
                activity!!.runOnUiThread {
                    ivTest!!.setImageBitmap(bitmap)
                }

            }
        }

        GlobalScope.async {

            val bitmap = getImage(pdfGuideModel.nameMaintenance)
            if (bitmap != null){
                activity!!.runOnUiThread {
                    ivMaintenance!!.setImageBitmap(bitmap)
                }

            }
        }

        GlobalScope.async {

            val bitmap = getImage(pdfGuideModel.nameKnow)
            if (bitmap != null){
                activity!!.runOnUiThread {
                    ivKnow!!.setImageBitmap(bitmap)
                }

            }
        }
        GlobalScope.async {


            val bitmap = getImageFile(pdfGuideModel.signatureTechnical)
            if (bitmap != null){
                activity!!.runOnUiThread {
                    ivSignatureTechnical!!.setImageBitmap(bitmap)
                    println("Imagen technical: ${pdfGuideModel.signatureTechnical}")
                }

            }


            val bitmapClient = getImageFile(pdfGuideModel.signatureClient)
            if (bitmapClient != null){
                activity!!.runOnUiThread {
                    ivSignatureClient!!.setImageBitmap(bitmapClient)
                    println("Imagen Client: ${pdfGuideModel.signatureClient}")
                }

            }


        }

    }

    private fun executeEmail(emailModel: EmailModel){

        val emailFragment = EmailFragment.newInstance(emailModel)
        activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, emailFragment,
                        emailFragment.javaClass.simpleName)
                .commit()
    }

    private fun saveViewPdf(){
        val viewContent = activity!!.findViewById<View>(R.id.sv_tab_pdf)
        val viewScroll = activity!!.findViewById<ScrollView>(R.id.sv_tab_pdf)
        val viewBitmap : Bitmap = Bitmap.createBitmap(viewScroll.getChildAt(0)
                .width,
                viewScroll.getChildAt(0).height, Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(viewBitmap)
        viewContent.draw(canvas)
        GlobalScope.async {
            manageImage.image = viewBitmap
            manageImage.code = pdfGuideModel.id
            manageImage.scale = true
            manageImage.flagPdf = true
            manageImage.addFileToGallery(activity!!)

        }
    }

    fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}