package com.hiddenodds.trebol.presentation.view.fragments

import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import butterknife.BindView
import butterknife.ButterKnife
import com.github.barteksc.pdfviewer.PDFView
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.persistent.file.ManageFile
import com.hiddenodds.trebol.presentation.model.EmailModel
import com.hiddenodds.trebol.presentation.view.activities.MainActivity
import com.hiddenodds.trebol.tools.ChangeFormat
import kotlinx.coroutines.experimental.async


class PdfViewFragment: Fragment(){
    @BindView(R.id.pdfView)
    @JvmField var pdfView: PDFView? = null

    companion object Factory {
        private const val inputNotification = "notify_"
        private const val inputTechnical = "technical_"
        private const val inputEmailModel = "email_"
        fun newInstance(arg1: String? = null, arg2: String? = null,
                        arg3: EmailModel? = null):
                PdfViewFragment = PdfViewFragment().apply{
            this.arguments = Bundle().apply {
                this.putString(inputNotification, arg1)
                this.putString(inputTechnical, arg2)
                this.putSerializable(PdfViewFragment.inputEmailModel, arg3)
            }

        }
    }

    private val codeNotification: String by lazy {
        this.arguments.getString(inputNotification)}
    private val codeTechnical: String by lazy {
        this.arguments.getString(inputTechnical)}

    private val emailModel: EmailModel by lazy { this.arguments
            .getSerializable(PdfViewFragment.inputEmailModel) as EmailModel
    }

    private var uri: Uri? = null
    private var itemMenuSave: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

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
        (activity as MainActivity).displayHome(false)
    }

    override fun onResume() {
        super.onResume()
        async {
            uri = ManageFile.getFile("$codeNotification.pdf")
            activity.runOnUiThread {
                pdfView!!.fromUri(uri).load()
                emailModel.clip = "$codeNotification.pdf"
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater!!.inflate(R.menu.options, menu)
        itemMenuSave = menu!!.getItem(0)
        itemMenuSave!!.isVisible = false

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId

        if (id == R.id.action_email){
            executeEmail(emailModel)
        }
        if (id == R.id.action_save){

        }

        return super.onOptionsItemSelected(item)
    }

    private fun executeEmail(emailModel: EmailModel){
        val emailFragment = EmailFragment.newInstance(emailModel)
        activity.supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, emailFragment,
                        emailFragment.javaClass.simpleName)
                .commit()
    }

}