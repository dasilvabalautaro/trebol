package com.hiddenodds.trebol.presentation.model

import com.hiddenodds.trebol.presentation.interfaces.IModel

class DownloadModel: IModel {
    var id: String = ""
    var code: String = ""
    var notification: String = ""
    var customer: String = ""
    var state: Byte = 0
}