package com.hiddenodds.trebol.domain.data

import android.os.Parcel
import com.hiddenodds.trebol.model.interfaces.IDataContent

class MapperCustomer: IDataContent {

    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        name = parcel.readString()
        email = parcel.readString()
        phone = parcel.readString()
        tech = parcel.readString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phone)
        parcel.writeString(tech)
        return parcel
    }

    var code: String = ""
    var name: String = ""
    var phone: String = ""
    var email: String = ""
    var tech: String = ""

}