package com.hiddenodds.trebol.presentation.model

import com.hiddenodds.trebol.model.persistent.file.ManageFile
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