package com.hiddenodds.trebol.domain.interactor

import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.persistent.network.StatementSQL
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.json.JSONArray
import javax.inject.Inject

class DownloadCustomerUseCase @Inject constructor(private val getRemoteDataUseCase:
                                                   GetRemoteDataUseCase,
                                                  private val updateDownloadUseCase:
                                                   UpdateDownloadUseCase) {
    var listTechnicals: ArrayList<String> = ArrayList()
    private var codeTechnical: String = ""
    private var messageError: String = ""
    var observableMessageError: Subject<String> = PublishSubject.create()
    private var finishDownload: Boolean = false
    var observableFinishDownload: Subject<Boolean> = PublishSubject.create()
    val context = App.appComponent.context()

    init {
        this.observableMessageError
                .subscribe { this.messageError }
        this.observableFinishDownload
                .subscribe { this.finishDownload }
    }
    

    private fun executeGetRemote(code: String){
        if ((context as App).connectionNetwork.isOnline()) {
            this.codeTechnical = code
            getRemoteDataUseCase.sql = StatementSQL.getCustomer(code)
            getRemoteDataUseCase.execute(ListObserver())
        }else{
            this.messageError = context.getString(R.string.network_not_found)
            this.observableMessageError.onNext(this.messageError)
        }
    }

    private fun executeUpdateDownload(jsonArray: JSONArray){

        if (jsonArray.length() != 0){
            updateDownloadUseCase.value = jsonArray.toString(2)
            updateDownloadUseCase.code = this.codeTechnical
            updateDownloadUseCase.fieldName = "customer"
            updateDownloadUseCase.execute(UpdateDownloadObserver())
        }else{
            getCustomersNextTechnical()
        }
    }
    
    fun getCustomersNextTechnical(){
        if (this.listTechnicals.size != 0){
            val code = this.listTechnicals.last()
            this.listTechnicals.remove(code)
            executeGetRemote(code)
        }else if (!finishDownload){
            finishDownload = true
            observableFinishDownload.onNext(finishDownload)
            //println("Finish download notification: " + finishDownload.toString())
        }
    }

    fun destroy(){
        getRemoteDataUseCase.dispose()
        updateDownloadUseCase.dispose()
    }

    inner class ListObserver: DisposableObserver<JSONArray>(){
        override fun onNext(t: JSONArray) {
            executeUpdateDownload(t)
        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.download_complete))
        }

        override fun onError(e: Throwable) {
            //println("Error: JSONARRAY")
            if (e.message != null) {
                messageError = e.message!!
                observableMessageError.onNext(messageError)
            }
        }
    }
    

    inner class UpdateDownloadObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            getCustomersNextTechnical()
        }

        override fun onComplete() {
            if (listTechnicals.isEmpty()){
                println("Finish Download")
                finishDownload = true
                observableFinishDownload.onNext(finishDownload)
            }

        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                messageError = e.message!!
                observableMessageError.onNext(messageError)

            }
        }
    }

}