package com.hiddenodds.trebol.presentation.presenter

import android.net.Uri
import com.hiddenodds.trebol.model.persistent.file.ManageFile
import com.hiddenodds.trebol.model.persistent.network.ManagementFtp
import javax.inject.Inject

class SendFtpPresenter @Inject constructor(private val managementFtp:
                                           ManagementFtp): BasePresenter() {


    private var pathFile: Uri? = null
    var nameFile = ""

    fun executeSendFile(){
        //&& managementFtp.changeDirectory()
        setFile()
        if (managementFtp.connect()){
            if (managementFtp.upload(this.pathFile!!.path!!, this.nameFile)){

                managementFtp.close()
            }
        }
    }

    private fun setFile(){
        this.pathFile = ManageFile.getFile(this.nameFile)
    }
}