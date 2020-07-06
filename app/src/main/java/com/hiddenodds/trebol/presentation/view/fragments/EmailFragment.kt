package com.hiddenodds.trebol.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebol.presentation.model.EmailModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class EmailFragment: NotificationFragment(), ILoadDataView {
    @BindView(R.id.tv_title)
    @JvmField var tvTitle: TextView? = null
    @BindView(R.id.tv_client)
    @JvmField var tvClient: TextView? = null
    @BindView(R.id.tv_of)
    @JvmField var tvOf: TextView? = null
    @BindView(R.id.tv_cc)
    @JvmField var tvCc: TextView? = null
    @BindView(R.id.et_for)
    @JvmField var etFor: EditText? = null
    @BindView(R.id.tv_subject)
    @JvmField var tvSubject: TextView? = null
    @BindView(R.id.tv_clip)
    @JvmField var tvClip: TextView? = null
    @BindView(R.id.et_message)
    @JvmField var etMessage: EditText? = null
    @OnClick(R.id.bt_send_email)
    fun sendEmail(){

        emailModel.whoFor = etFor!!.text.toString()
        sendEmailPresenter.emailModel = emailModel
        sendEmailPresenter.executeSendEmail()

    }

    companion object Factory {
        private const val inputEmailModel = "model_"
        fun newInstance(arg1: EmailModel? = null):
                EmailFragment = EmailFragment().apply{
            this.arguments = Bundle().apply {
                this.putSerializable(inputEmailModel, arg1)
            }

        }
    }

    private val emailModel: EmailModel by lazy { this.arguments!!
            .getSerializable(inputEmailModel) as EmailModel}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater.inflate(R.layout.view_mail,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        sendEmailPresenter.view = this
        sendFtpPresenter.view = this
        setDataControl()
    }

    private fun setDataControl(){
        tvTitle!!.text = emailModel.title
        tvCc!!.text = emailModel.whoCopy
        tvClient!!.text = emailModel.client
        tvClip!!.text = emailModel.clip
        tvOf!!.text = emailModel.whoOf
        tvSubject!!.text = emailModel.subject
        etFor!!.setText(emailModel.whoFor)
        etMessage!!.setText(emailModel.message)

    }

    override fun onResume() {
        super.onResume()
        //Execute FTP
        sendFtpPresenter.nameFile = emailModel.clip
        if (sendFtpPresenter.nameFile.isNotEmpty()){
            GlobalScope.launch {
                sendFtpPresenter.executeSendFile()
            }

        }
    }
    override fun showMessage(message: String) {
        context!!.toast(message)
    }

    override fun showError(message: String) {
        context!!.toast(message)
    }

    override fun <T> executeTask(obj: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}