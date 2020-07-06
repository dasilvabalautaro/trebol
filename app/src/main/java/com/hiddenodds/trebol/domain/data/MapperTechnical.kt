package com.hiddenodds.trebol.domain.data

import android.os.Parcel
import com.hiddenodds.trebol.model.interfaces.IDataContent

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapperTechnical: IDataContent {
    var code: String = ""
    var name: String = ""
    var email: String = ""
    var password: String = ""

    override fun setContent(parcel: Parcel) {
        code = parcel.readString().toString()
        name = parcel.readString().toString()
        email = parcel.readString().toString()
        password = parcel.readString().toString()
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