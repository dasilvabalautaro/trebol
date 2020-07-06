package com.hiddenodds.trebol.domain.data

import android.os.Parcel
import com.hiddenodds.trebol.model.interfaces.IDataContent

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapperNotification: IDataContent {
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
    var state: String = "0"
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
    var diet: String = "NO"
    var workHours: String = "0"
    var inside: String = "00:00"
    var outside: String = "00:00"

    override fun setContent(parcel: Parcel) {
        code = parcel.readString().toString()
        machine = parcel.readString().toString()
        type = parcel.readString().toString()
        dateInit = parcel.readString().toString()
        dateEnd = parcel.readString().toString()
        observations = parcel.readString().toString()
        dateCompleted = parcel.readString().toString()
        completed = parcel.readString().toString()
        duration = parcel.readString().toString()
        displacement = parcel.readString().toString()
        trbPartTwo = parcel.readString().toString()
        state = parcel.readString().toString()
        satd = parcel.readString().toString()
        satdk = parcel.readString().toString()
        ink = parcel.readString().toString()
        businessName = parcel.readString().toString()
        province = parcel.readString().toString()
        locality = parcel.readString().toString()
        symptom = parcel.readString().toString()
        address = parcel.readString().toString()
        series = parcel.readString().toString()
        product = parcel.readString().toString()
        trade = parcel.readString().toString()
        peaje = parcel.readString().toString()
        idTech = parcel.readString().toString()
        vSoft1 = parcel.readString().toString()
        vSoft2 = parcel.readString().toString()
        vSoft3 = parcel.readString().toString()
        hours = parcel.readString().toString()
        totalTeam = parcel.readString().toString()
        lastAmount = parcel.readString().toString()
        reportTechnical = parcel.readString().toString()
        diet = parcel.readString().toString()
        workHours = parcel.readString().toString()
        inside = parcel.readString().toString()
        outside = parcel.readString().toString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(machine)
        parcel.writeString(type)
        parcel.writeString(dateInit)
        parcel.writeString(dateEnd)
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