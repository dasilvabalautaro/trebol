package com.hiddenodds.trebolv2.presentation.presenter

import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.interactor.AddCustomerToNotificationUseCase
import com.hiddenodds.trebolv2.domain.interactor.AddNotificationToTechnicalUseCase
import com.hiddenodds.trebolv2.domain.interactor.GetTechnicalMasterUseCase
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import com.hiddenodds.trebolv2.tools.Constants
import com.hiddenodds.trebolv2.tools.PreferenceHelper
import com.hiddenodds.trebolv2.tools.PreferenceHelper.get
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject


class AddNotificationToTechnicalPresenter @Inject constructor(private val addNotificationToTechnicalUseCase:
                                                              AddNotificationToTechnicalUseCase,
                                                              private val addCustomerToNotificationUseCase:
                                                              AddCustomerToNotificationUseCase,
                                                              private val getTechnicalMasterUseCase:
                                                              GetTechnicalMasterUseCase):
        BasePresenter(){


    private var listTechnicals: ArrayList<String> = ArrayList()
    private var codeTechnical: String = ""

    fun executeAddNotification(){
        val prefs = PreferenceHelper.customPrefs(context,
                Constants.PREFERENCE_TREBOL)
        val code: String? = prefs[Constants.TECHNICAL_KEY, ""]
        val password: String? = prefs[Constants.TECHNICAL_PASSWORD, ""]
        if (!code.isNullOrEmpty() && !password.isNullOrEmpty()){
            getTechnicalMasterUseCase.code = code!!
            getTechnicalMasterUseCase.password = password!!
            getTechnicalMasterUseCase.execute(TechObserver())
        }else{
            println("Technical not found")
        }

    }

    fun executeAddCustomer(idTech: String){
        this.codeTechnical = idTech
        addCustomerToNotificationUseCase.codeTech = idTech
        addCustomerToNotificationUseCase.execute(AddCustomerObserver())

    }

    private fun addNotificationsTechnical(){
       addNotificationToTechnicalUseCase.codeTech = this.codeTechnical
       addNotificationToTechnicalUseCase.execute(AddNotificationTechnicalObserver())
    }

    private fun stopProgress(){
        view!!.executeTask(3)
    }


    private fun getNextTechnical(){
        if (listTechnicals.isNotEmpty()){
            val code = listTechnicals.last()
            listTechnicals.remove(code)
            executeAddCustomer(code)
        }else{

            showMessage(context.resources.getString(R.string.add_notifications))
            stopProgress()
        }
    }

    inner class AddCustomerObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            addNotificationsTechnical()

        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.add_customer))
        }

        override fun onError(e: Throwable) {
            println("Error: Add customer")
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

    inner class AddNotificationTechnicalObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            getNextTechnical()

        }

        override fun onComplete() {

            //showMessage(context.resources.getString(R.string.add_notifications))
        }

        override fun onError(e: Throwable) {
            println("Error: Add notification")
           if (e.message != null) {
               showError(e.message!!)
           }
        }
    }

    inner class TechObserver: DisposableObserver<TechnicalModel>(){
        override fun onNext(t: TechnicalModel) {
            listTechnicals = ArrayList(t.trd)
            executeAddCustomer(t.code)
        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.get_complete))
        }

        override fun onError(e: Throwable) {
            println("Error: Technical master")
            if (e.message != null) {
                showError(e.message!!)
            }
        }
    }

}