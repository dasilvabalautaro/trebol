package com.hiddenodds.trebolv2.presentation.view.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.github.barteksc.pdfviewer.PDFView
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.persistent.file.ManageFile
import com.hiddenodds.trebolv2.tools.ChangeFormat


class PdfViewFragment: Fragment(){
    @BindView(R.id.pdfView)
    @JvmField var pdfView: PDFView? = null

    companion object Factory {
        private const val inputNotification = "notify_"
        private const val inputTechnical = "technical_"
        fun newInstance(arg1: String? = null, arg2: String? = null):
                PdfViewFragment = PdfViewFragment().apply{
            this.arguments = Bundle().apply {
                this.putString(inputNotification, arg1)
                this.putString(inputTechnical, arg2)
            }

        }
    }

    private val codeNotification: String by lazy {
        this.arguments.getString(inputNotification)}
    private val codeTechnical: String by lazy {
        this.arguments.getString(inputTechnical)}


    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_pdf,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ChangeFormat.deleteCacheTechnical(codeTechnical)
        pdfView!!.fromUri(ManageFile.getFile("$codeNotification.pdf")).load()

    }
}