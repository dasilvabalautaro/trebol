package com.hiddenodds.trebol.presentation.model

import com.hiddenodds.trebol.presentation.interfaces.IModel

class TechnicalModel: IModel {
    var id: String = ""
    var code: String = ""
    var name: String = ""
    var email: String = ""
    var password: String = ""
    var trd: ArrayList<String> = ArrayList()
    var notifications: ArrayList<NotificationModel> = ArrayList()

}