package com.hiddenodds.trebolv2.presentation.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.tools.ChangeFormat
import com.hiddenodds.trebolv2.tools.Variables


class ConfigurationFragment: NotificationFragment(), ILoadDataView {
    @BindView(R.id.et_server)
    @JvmField var etServer: EditText? = null
    @BindView(R.id.et_database)
    @JvmField var etDatabase: EditText? = null
    @BindView(R.id.et_user)
    @JvmField var etUser: EditText? = null
    @BindView(R.id.et_password)
    @JvmField var etPassword: EditText? = null
    @BindView(R.id.pb_connect)
    @JvmField var pbConnect: ProgressBar? = null
    @OnClick(R.id.bt_save)
    fun saveConfigurationServer(){
        if (validateInput()){
            Variables.sqlServer = etServer!!.text.toString()
            Variables.database = etDatabase!!.text.toString()
            Variables.user = etUser!!.text.toString()
            Variables.password = etPassword!!.text.toString()
            pbConnect!!.visibility = View.VISIBLE
            verifyConnectServerPresenter.verifyConnect()
        }else{
            context.toast(context.resources
                    .getString(R.string.input_error))
        }
    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.view_configuration,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        verifyConnectServerPresenter.view = this
    }

    private fun validateInput(): Boolean{
        return etServer!!.text.isNotEmpty() &&
                etDatabase!!.text.isNotEmpty() &&
                etUser!!.text.isNotEmpty() &&
                etPassword!!.text.isNotEmpty()
    }

    override fun showMessage(message: String) {
        pbConnect!!.visibility = View.INVISIBLE
        ChangeFormat.changeVariablesConnect(context)
        context.toast(message)
    }

    override fun showError(message: String) {
        pbConnect!!.visibility = View.INVISIBLE
        ChangeFormat.refreshVariablesconnect(context)
        context.toast(message)
    }

    override fun <T> executeTask(obj: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}