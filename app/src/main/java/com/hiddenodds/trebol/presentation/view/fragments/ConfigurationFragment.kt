package com.hiddenodds.trebol.presentation.view.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebol.presentation.view.activities.MainActivity
import com.hiddenodds.trebol.tools.ChangeFormat
import com.hiddenodds.trebol.tools.Variables


class ConfigurationFragment: NotificationFragment(), ILoadDataView {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_server)
    @JvmField var etServer: EditText? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_port)
    @JvmField var etPort: EditText? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.pb_connect)
    @JvmField var pbConnect: ProgressBar? = null
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.bt_save)
    fun saveConfigurationServer(){
        if (validateInput()){
            Variables.sqlServer = etServer!!.text.toString() + ":" +
                    etPort!!.text.toString()
            pbConnect!!.visibility = View.VISIBLE
            verifyConnectServerPresenter.verifyConnect()
        }else{
            requireContext().toast(requireContext().resources
                    .getString(R.string.input_error))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root: View = inflater.inflate(R.layout.view_configuration,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onStart() {
        super.onStart()
        verifyConnectServerPresenter.view = this
    }

    private fun validateInput(): Boolean{
        return etServer!!.text.isNotEmpty() &&
                etPort!!.text.isNotEmpty()
    }

    override fun showMessage(message: String) {
        pbConnect!!.visibility = View.INVISIBLE
        ChangeFormat.changeVariablesConnect(requireContext())
        requireContext().toast(message)
    }

    override fun showError(message: String) {
        pbConnect!!.visibility = View.INVISIBLE
        ChangeFormat.refreshVariablesconnect(requireContext())
        requireContext().toast(message)
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).displayHome(false)
    }

    override fun <T> executeTask(obj: T) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}