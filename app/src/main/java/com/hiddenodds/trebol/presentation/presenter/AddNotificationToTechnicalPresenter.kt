package com.hiddenodds.trebol.presentation.presenter

import com.hiddenodds.trebol.domain.interactor.AddCustomerToNotificationUseCase
import com.hiddenodds.trebol.domain.interactor.AddNotificationToTechnicalUseCase
import com.hiddenodds.trebol.model.persistent.file.ManageFile
import com.hiddenodds.trebol.tools.Variables
import io.reactivex.observers.DisposableObserver
import kotlinx.coroutines.experimental.async
import javax.inject.Inject


class AddNotificationToTechnicalPresenter @Inject constructor(private val addNotificationToTechnicalUseCase:
                                                              AddNotificationToTechnicalUseCase,
                                                              private val addCustomerToNotificationUseCase:
                                                              AddCustomerToNotificationUseCase):
        BasePresenter(){


    private var listTechnicals: ArrayList<String> = ArrayList()
    private var codeTechnical: String = ""
    private var countTech = 0

    fun executeAddNotification(){
        this.listTechnicals = ArrayList(Variables.listTechnicals)
        this.listTechnicals.add(Variables.codeTechMaster)
        this.countTech = this.listTechnicals.size
        getNextTechnical()

    }

    private fun executeAddCustomer(idTech: String){
        this.codeTechnical = idTech
        addCustomerToNotificationUseCase.codeTech = idTech
        addCustomerToNotificationUseCase.execute(AddCustomerObserver())

    }

    private fun addNotificationsTechnical(){
       addNotificationToTechnicalUseCase.codeTech = this.codeTechnical
       addNotificationToTechnicalUseCase.execute(AddNotificationTechnicalObserver())
    }

    private fun stopProgress(){
        async {
            ManageFile.deleteDirectoryOfWork()
        }

        view!!.executeTask(5)
    }


    private fun getNextTechnical(){
        if (listTechnicals.isNotEmpty()){
            val code = listTechnicals.last()
            listTechnicals.remove(code)
            executeAddCustomer(code)
        }
    }

    override fun destroy() {
        super.destroy()
        addNotificationToTechnicalUseCase.dispose()
        addCustomerToNotificationUseCase.dispose()
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
            countTech -= 1
            if (countTech == 0){
                stopProgress()
            }

        }

        override fun onError(e: Throwable) {
            println("Error: Add notification")
           if (e.message != null) {
               showError(e.message!!)
           }
        }
    }

}