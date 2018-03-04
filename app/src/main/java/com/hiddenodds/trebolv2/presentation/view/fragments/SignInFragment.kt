package com.hiddenodds.trebolv2.presentation.view.fragments

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.dagger.PresenterModule
import com.hiddenodds.trebolv2.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebolv2.presentation.presenter.SaveCustomerPresenter
import com.hiddenodds.trebolv2.presentation.presenter.TechnicalMasterPresenter
import com.hiddenodds.trebolv2.presentation.presenter.TechnicalRemotePresenter
import com.hiddenodds.trebolv2.presentation.view.activities.MainActivity
import com.hiddenodds.trebolv2.tools.Constants
import com.hiddenodds.trebolv2.tools.PreferenceHelper
import com.hiddenodds.trebolv2.tools.PreferenceHelper.get
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject

class SignInFragment: Fragment(), ILoadDataView {
    @BindView(R.id.edt_user)
    @JvmField var edt_user: EditText? = null
    @BindView(R.id.edt_password)
    @JvmField var edt_password: EditText? = null
    @OnClick(R.id.btn_ok)
    fun getTechnicalMaster(){
        if (validateInput()){
            technicalMasterPresenter
                    .executeGetTechnicalMaster(edt_user?.text!!.trim().toString(),
                            edt_password?.text!!.trim().toString())
        }else{
            context.toast(context.resources
                    .getString(R.string.input_error))
        }
    }

    val Fragment.app: App
        get() = activity.application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}

    @Inject
    lateinit var saveCustomerPresenter: SaveCustomerPresenter
    @Inject
    lateinit var technicalRemotePresenter: TechnicalRemotePresenter
    @Inject
    lateinit var technicalMasterPresenter: TechnicalMasterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

    }

    override fun onCreateView(inflater: LayoutInflater?,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val root: View = inflater!!.inflate(R.layout.sign_in,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        technicalRemotePresenter.view = this
        technicalMasterPresenter.view = this
    }

    override fun onResume() {
        super.onResume()
        val prefs = PreferenceHelper.customPrefs(context,
                Constants.PREFERENCE_TREBOL)
        val techKey: Boolean? = prefs[Constants.TECHNICAL_DB, false]
        if (techKey == null || !techKey){
            launch{
                technicalRemotePresenter.executeQueryRemote()
            }

        }

    }


    override fun showMessage(message: String) {
        context.toast(message)
    }

    override fun showError(message: String) {
        context.toast(message)
    }

    override fun executeTask() {
        val fragmentMenu = MenuFragment()
        (context as MainActivity).addFragment(fragmentMenu)
    }

    private fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun validateInput(): Boolean{
        return (!edt_user!!.text.isNullOrEmpty() &&
                !edt_password!!.text.isNullOrEmpty())
    }
}