package com.hiddenodds.trebolv2.presentation.model

class NotificationModel {
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
    var vSoft1: String = ""
    var vSoft2: String = ""
    var vSoft3: String = ""
    var hours: String = ""
    var totalTeam: String = ""
    var lastAmount: String = ""
    var reportTechnical: String = ""
    var diet: String = ""
    var workHours: String = ""
    var inside: String = ""
    var outside: String = ""
    var materialUse: ArrayList<AssignedMaterialModel> = ArrayList()
    var materialOut: ArrayList<AssignedMaterialModel> = ArrayList()
}