package com.hiddenodds.trebolv2.domain.data

import android.os.Parcel
import com.hiddenodds.trebolv2.model.interfaces.IDataContent

class MapperCustomer: IDataContent {

    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        name = parcel.readString()
        email = parcel.readString()
        phone = parcel.readString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(phone)
        return parcel
    }

    var code: String = ""
    var name: String = ""
    var phone: String = ""
    var email: String = ""

}