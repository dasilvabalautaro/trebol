package com.hiddenodds.trebolv2.model.data

import android.os.Parcel
import com.hiddenodds.trebolv2.model.interfaces.IDataContent
import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Notification: RealmObject(), IDataContent {

    @PrimaryKey
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
    var customer: Customer? = null
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
    var materialUse: RealmList<AssignedMaterial> = RealmList()
    var materialOut: RealmList<AssignedMaterial> = RealmList()

    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        machine = parcel.readString()
        type = parcel.readString()
        dateInit = parcel.readString()
        dateEnd = parcel.readString()
        observations = parcel.readString()
        dateCompleted = parcel.readString()
        completed = parcel.readString()
        duration = parcel.readString()
        displacement = parcel.readString()
        trbPartTwo = parcel.readString()
        state = parcel.readString()
        satd = parcel.readString()
        satdk = parcel.readString()
        ink = parcel.readString()
        businessName = parcel.readString()
        province = parcel.readString()
        locality = parcel.readString()
        symptom = parcel.readString()
        address = parcel.readString()
        series = parcel.readString()
        product = parcel.readString()
        trade = parcel.readString()
        peaje = parcel.readString()
        idTech = parcel.readString()
        vSoft1 = parcel.readString()
        vSoft2 = parcel.readString()
        vSoft3 = parcel.readString()
        hours = parcel.readString()
        totalTeam = parcel.readString()
        lastAmount = parcel.readString()
        reportTechnical = parcel.readString()
        diet = parcel.readString()
        workHours = parcel.readString()
        inside = parcel.readString()
        outside = parcel.readString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(machine)
        parcel.writeString(dateInit)
        parcel.writeString(dateEnd)
        parcel.writeString(type)
        parcel.writeString(observations)
        parcel.writeString(dateCompleted)
        parcel.writeString(completed)
        parcel.writeString(duration)
        parcel.writeString(displacement)
        parcel.writeString(trbPartTwo)
        parcel.writeString(state)
        parcel.writeString(satd)
        parcel.writeString(satdk)
        parcel.writeString(ink)
        parcel.writeString(businessName)
        parcel.writeString(province)
        parcel.writeString(locality)
        parcel.writeString(symptom)
        parcel.writeString(address)
        parcel.writeString(series)
        parcel.writeString(product)
        parcel.writeString(trade)
        parcel.writeString(peaje)
        parcel.writeString(idTech)
        parcel.writeString(vSoft1)
        parcel.writeString(vSoft2)
        parcel.writeString(vSoft3)
        parcel.writeString(hours)
        parcel.writeString(totalTeam)
        parcel.writeString(lastAmount)
        parcel.writeString(reportTechnical)
        parcel.writeString(diet)
        parcel.writeString(workHours)
        parcel.writeString(inside)
        parcel.writeString(outside)

        return parcel
    }

}