package com.hiddenodds.trebol.presentation.model

import com.hiddenodds.trebol.presentation.interfaces.IModel

class NotificationModel: IModel {
    var id: String = ""
    var code: String = ""
    var machine: String = ""
    var dateInit: String = ""
    var dateEnd: String = ""
    var type: String = ""
    var observations: String = ""
    var dateCompleted: String = ""
    var completed: String = ""
    var duration: String = ""
    var displacement: String = ""
    var trbPartTwo: String = ""
    var customer: CustomerModel? = null
    var state: String = ""
    var satd: String = ""
    var satdk: String = ""
    var ink: String = ""
    var businessName: String = ""
    var province: String = ""
    var locality: String = ""
    var symptom: String = ""
    var address: String = ""
    var series: String = ""
    var product: String = ""
    var trade: String = ""
    var peaje: String = ""
    var idTech: String = ""
    var vSoft1: String = "0"
    var vSoft2: String = "0"
    var vSoft3: String = "0"
    var hours: String = "0"
    var totalTeam: String = "0"
    var lastAmount: String = "0"
    var reportTechnical: String = ""
    var diet: String = ""
    var workHours: String = "0"
    var inside: String = "00:00"
    var outside: String = "00:00"
    var materialUse: ArrayList<AssignedMaterialModel> = ArrayList()
    var materialOut: ArrayList<AssignedMaterialModel> = ArrayList()
}