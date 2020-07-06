package com.hiddenodds.trebol.domain.data

import android.os.Parcel
import com.hiddenodds.trebol.model.interfaces.IDataContent

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapperCustomer: IDataContent {

    override fun setContent(parcel: Parcel) {
        code = parcel.readString().toString()
        name = parcel.readString().toString()
        email = parcel.readString().toString()
        phone = parcel.readString().toString()
        tech = parcel.readString().toString()
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