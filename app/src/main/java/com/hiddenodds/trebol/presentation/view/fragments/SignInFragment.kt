package com.hiddenodds.trebol.presentation.view.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.dagger.PresenterModule
import com.hiddenodds.trebol.presentation.interfaces.ILoadDataView
import com.hiddenodds.trebol.presentation.model.TechnicalModel
import com.hiddenodds.trebol.presentation.presenter.TechnicalMasterPresenter
import com.hiddenodds.trebol.presentation.presenter.TechnicalRemotePresenter
import com.hiddenodds.trebol.presentation.presenter.TypeNotificationRemotePresenter
import com.hiddenodds.trebol.tools.ChangeFormat
import com.hiddenodds.trebol.tools.Constants
import com.hiddenodds.trebol.tools.PreferenceHelper
import com.hiddenodds.trebol.tools.PreferenceHelper.get
import com.hiddenodds.trebol.tools.Variables
import io.realm.Realm
import io.realm.RealmConfiguration
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class SignInFragment: Fragment(), ILoadDataView {
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_user)
    @JvmField var edt_user: EditText? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.edt_password)
    @JvmField var edt_password: EditText? = null
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_ok)
    @JvmField var btn_ok: Button? = null
    @SuppressLint("NonConstantResourceId")
    @OnClick(R.id.btn_ok)
    fun getTechnicalMaster(){
        if (validateInput()){
            technicalMasterPresenter
                    .executeGetTechnicalMaster(edt_user?.text!!.trim().toString(),
                            edt_password?.text!!.trim().toString())
        }else{
            requireContext().toast(requireContext().resources
                    .getString(R.string.input_error))
        }
    }

    val Fragment.app: App
        get() = requireActivity().application as App

    private val component by lazy { app.
            getAppComponent().plus(PresenterModule())}


    @Inject
    lateinit var technicalRemotePresenter: TechnicalRemotePresenter
    @Inject
    lateinit var typeNotificationRemotePresenter: TypeNotificationRemotePresenter
    @Inject
    lateinit var technicalMasterPresenter: TechnicalMasterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val root: View = inflater.inflate(R.layout.sign_in,
                container,false)
        ButterKnife.bind(this, root)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        technicalRemotePresenter.view = this
        technicalMasterPresenter.view = this
        typeNotificationRemotePresenter.view = this

        try {
            val prefs = PreferenceHelper.customPrefs(requireContext(),
                    Constants.PREFERENCE_TREBOL)
            val techKey: Boolean? = prefs[Constants.TECHNICAL_DB, false]
            if (techKey == null || !techKey){
                GlobalScope.launch{
                    technicalRemotePresenter.executeQueryRemote()
                    typeNotificationRemotePresenter.executeQueryRemote()
                }

            }else{
                GlobalScope.launch{
                    technicalRemotePresenter.executeQueryRemote()
                }
            }

        }catch (ie: IllegalStateException){
            println(ie.message)
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ChangeFormat.setHeightPercent(btn_ok!!, 0.15f)

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            ChangeFormat.setHeightPercent(btn_ok!!, 0.10f)

        }
    }

    override fun showMessage(message: String) {
        val config: RealmConfiguration = Realm.getDefaultConfiguration()!!
        println(config.path + " " + Realm.getGlobalInstanceCount(config).toString())
        requireContext().toast(message)
    }

    override fun showError(message: String) {
        if (context != null){
            requireContext().toast(message)
        }

    }

    private fun clearRemotePresenter(option: Int){
        when(option){
            1 -> {
                technicalRemotePresenter.destroy()
            }
            2 -> {
                typeNotificationRemotePresenter.destroy()
            }
        }
    }

    override fun <T> executeTask(obj: T) {
        if (obj != null){
            if (obj is Int){
                clearRemotePresenter(obj)

            }else{
                val nameTech = (obj as TechnicalModel).name
                Variables.codeTechMaster = (obj as TechnicalModel).code
                Variables.listTechnicals = ArrayList((obj as TechnicalModel).trd)

                requireContext().toast(requireContext().resources.getString(R.string.welcome) +
                        "\n" + nameTech)
                callMenu()

            }
        }

    }

    override fun <T> executeTask(objList: List<T>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun Context.toast(message: CharSequence) =
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    private fun license(): Boolean{
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val init = sdf.parse("2018-06-06")
        val limit = (init!!.time + (86400000 * 10))
        val dateWork = Date().time

        return (dateWork > limit)

    }

    private fun validateInput(): Boolean{

        return (!edt_user!!.text.isNullOrEmpty() &&
                !edt_password!!.text.isNullOrEmpty())
    }

    private fun callMenu(){
        val fragmentMenu = MenuFragment()
        requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.flContent, fragmentMenu,
                        fragmentMenu.javaClass.simpleName)
                .commit()
    }
}