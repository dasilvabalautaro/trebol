package com.hiddenodds.trebolv2.model.executor

import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.interactor.GetRemoteDataUseCase
import com.hiddenodds.trebolv2.domain.interactor.GetTechnicalMasterUseCase
import com.hiddenodds.trebolv2.domain.interactor.UpdateDownloadUseCase
import com.hiddenodds.trebolv2.model.persistent.network.StatementSQL
import com.hiddenodds.trebolv2.presentation.model.TechnicalModel
import com.hiddenodds.trebolv2.tools.Constants
import com.hiddenodds.trebolv2.tools.PreferenceHelper
import com.hiddenodds.trebolv2.tools.PreferenceHelper.get
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.json.JSONArray
import javax.inject.Inject

class DownloadCustomerExecutor @Inject constructor(private val getRemoteDataUseCase:
                                                   GetRemoteDataUseCase,
                                                   private val getTechnicalMasterUseCase:
                                                   GetTechnicalMasterUseCase,
                                                   private val updateDownloadUseCase:
                                                   UpdateDownloadUseCase) {
    private var listTechnicals: ArrayList<String> = ArrayList()
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

    private fun getMasterTechnical(){
        val prefs = PreferenceHelper.customPrefs(context,
                Constants.PREFERENCE_TREBOL)
        val code: String? = prefs[Constants.TECHNICAL_KEY, ""]
        val password: String? = prefs[Constants.TECHNICAL_PASSWORD, ""]
        if (!code.isNullOrEmpty() && !password.isNullOrEmpty()){
            getTechnicalMasterUseCase.code = code!!
            getTechnicalMasterUseCase.password = password!!
            getTechnicalMasterUseCase.execute(TechObserver())
        }else{
            this.messageError = context.getString(R.string.technical_not_found)
            this.observableMessageError.onNext(this.messageError)

        }
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
            getNotificationsNextTechnical()
        }
    }

    private fun getNotificationsNextTechnical(){
        if (listTechnicals.size != 0){
            val code = listTechnicals.last()
            listTechnicals.remove(code)
            executeGetRemote(code)
        }
    }

    inner class ListObserver: DisposableObserver<JSONArray>(){
        override fun onNext(t: JSONArray) {
            executeUpdateDownload(t)
        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.download_complete))
        }

        override fun onError(e: Throwable) {
            println("Error: JSONARRAY")
            if (e.message != null) {
                messageError = e.message!!
                observableMessageError.onNext(messageError)
            }
        }
    }

    inner class TechObserver: DisposableObserver<TechnicalModel>(){
        override fun onNext(t: TechnicalModel) {
            listTechnicals = ArrayList(t.trd)
            codeTechnical = t.code
            executeGetRemote(codeTechnical)
        }

        override fun onComplete() {
            //showMessage(context.resources.getString(R.string.get_complete))
        }

        override fun onError(e: Throwable) {
            if (e.message != null) {
                messageError = e.message!!
                observableMessageError.onNext(messageError)

            }
        }
    }

    inner class UpdateDownloadObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            getNotificationsNextTechnical()
        }

        override fun onComplete() {
            if (listTechnicals.isEmpty()){
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