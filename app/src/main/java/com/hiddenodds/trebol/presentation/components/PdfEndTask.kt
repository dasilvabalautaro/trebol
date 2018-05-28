package com.hiddenodds.trebol.presentation.components

import android.app.Service
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ScrollView
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.tools.ManageImage
import javax.inject.Inject


class PdfEndTask @Inject constructor() {
    private val context = App.appComponent.context()
    private val activity = App.appComponent.activity()
    private var view: View? = null
    private val pdfEndTaskView = PdfEndTaskView(context)

    var manageImage: ManageImage? = null

    fun inflateView(){
        val viewGroup: ViewGroup = pdfEndTaskView.rootView as ViewGroup
        val inflater = context
                .getSystemService(Service.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        view = inflater
                .inflate(R.layout.view_pdf_end_task_frame, viewGroup,
                        false) as PdfEndTaskView
    }

    fun setData(codeNotify: String, client: String, nameFile: String){
        (view as PdfEndTaskView).lblContent!!.text = buildContentText(codeNotify,
                client)
        setSignature(nameFile)
    }

    private fun buildContentText(codeInstalation: String, client: String): String {
        return String.format("Habiendo procedido la empresa TREBOL GROUP PROVIDERS S.L. a la instalación de la " +
                "impresora/máquina cuyo número de instalación es %s, afirmo y acredito:\n" +
                "Que la misma ha sido debida, total y adecuadamente instalada.\n" +
                "Así mismo, certifico mediante la presente, que tras la instalación de la misma, se ha procedido a " +
                "la comprobación de su correcto funcionamiento ante mi persona, habiendo podido comprobar su " +
                "eficaz y correcto funcionamiento.\n" +
                "Y, en prueba de lo antedicho, firmo la presente certificación.\n\n" +
                "Fdo: %s", codeInstalation, client)
    }

    private fun setSignature(codeNotify: String){
        manageImage!!.code = codeNotify
        val bitmap = manageImage!!.getFileOfGallery(activity)
        if (bitmap != null){
            (view as PdfEndTaskView).signatureClient!!.setImageBitmap(bitmap)
        }

    }

    fun saveImage(nameFile: String){
        view!!.measure(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)

        val viewSv: View = view!!.findViewById(R.id.sv_pdf_end_task)
        val svScroll: ScrollView = view!!.findViewById(R.id.sv_pdf_end_task)
        val viewBitmap : Bitmap = Bitmap.createBitmap(svScroll.getChildAt(0).measuredWidth,
                svScroll.getChildAt(0).measuredHeight, Bitmap.Config.RGB_565)
        val canvas = Canvas(viewBitmap)
        view!!.layout(viewSv.left, viewSv.top, viewSv.right, viewSv.bottom)
        view!!.draw(canvas)

        manageImage!!.image = viewBitmap
        manageImage!!.code = nameFile
        manageImage!!.flagPdf = true
        manageImage!!.addFileToGallery(activity)

    }


}