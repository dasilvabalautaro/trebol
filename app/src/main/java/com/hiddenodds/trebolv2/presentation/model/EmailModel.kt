package com.hiddenodds.trebolv2.presentation.model

import com.hiddenodds.trebolv2.model.persistent.file.ManageFile
import java.io.Serializable

class EmailModel: Serializable {
    var title: String = ""
    var client: String = ""
    var whoOf: String = ""
    var whoFor: String = ""
    var whoCopy: String = ""
    var subject: String = ""
    var clip: String = ""
        set(value){
            if (ManageFile.isFileExist(value)) field = value
        }
    var message: String = ""


}