package com.hiddenodds.trebolv2.domain.interactor

import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.domain.data.MapperDownload
import com.hiddenodds.trebolv2.model.persistent.network.StatementSQL
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.json.JSONArray
import javax.inject.Inject


class DownloadNotificationUseCase @Inject constructor(private val getRemoteDataUseCase:
                                                       GetRemoteDataUseCase,
                                                      private val createDownloadsUseCase:
                                                       CreateDownloadsUseCase,
                                                      private val updateDownloadUseCase:
                                                       UpdateDownloadUseCase,
                                                      private val deleteDownloadsUseCase:
                                                       DeleteDownloadsUseCase){

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
            getRemoteDataUseCase.sql = StatementSQL.getNotifications(code)
            getRemoteDataUseCase.execute(ListObserver())
        }else{
            this.messageError = context.getString(R.string.network_not_found)
            this.observableMessageError.onNext(this.messageError)
        }
    }

    private fun executeCreateDownload(){
        val listMapperDownload: ArrayList<MapperDownload> = ArrayList()
        if (this.listTechnicals.isNotEmpty()){
            for (i in this.listTechnicals.indices){
                val md = MapperDownload()
                md.code = this.listTechnicals[i]
                listMapperDownload.add(md)
            }
            createDownloadsUseCase.listMapperDownload = listMapperDownload
            createDownloadsUseCase.execute(CreateDownloadObserver())

        }

    }

    fun executeDeleteDownload(){
        deleteDownloadsUseCase.listTechnical = this.listTechnicals
        deleteDownloadsUseCase.execute(DeleteDownloadObserver())
    }

    private fun executeUpdateDownload(jsonArray: JSONArray){

        if (jsonArray.length() != 0){
            updateDownloadUseCase.value = jsonArray.toString(2)
            updateDownloadUseCase.code = this.codeTechnical
            updateDownloadUseCase.fieldName = "notification"
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

    fun destroy(){
        getRemoteDataUseCase.dispose()
        createDownloadsUseCase.dispose()
        updateDownloadUseCase.dispose()
        deleteDownloadsUseCase.dispose()
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
    inner class CreateDownloadObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            getNotificationsNextTechnical()
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

    inner class DeleteDownloadObserver: DisposableObserver<Boolean>(){
        override fun onNext(t: Boolean) {
            executeCreateDownload()
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