package com.hiddenodds.trebolv2.domain.data

import android.os.Parcel
import com.hiddenodds.trebolv2.model.interfaces.IDataContent

class MapperTechnical: IDataContent {
    var code: String = ""
    var name: String = ""
    var email: String = ""
    var password: String = ""

    override fun setContent(parcel: Parcel) {
        code = parcel.readString()
        name = parcel.readString()
        email = parcel.readString()
        password = parcel.readString()
    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(password)
        return parcel
    }
}