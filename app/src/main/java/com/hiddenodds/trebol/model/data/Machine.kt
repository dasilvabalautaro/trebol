package com.hiddenodds.trebol.model.data

import android.os.Parcel
import com.hiddenodds.trebol.model.interfaces.IDataContent
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Machine: RealmObject(), IDataContent {

    @PrimaryKey
    var id: String = ""
    var code: String = ""
    var compressor: String = ""
    var circuits: String = ""
    var others: String = ""



    override fun setContent(parcel: Parcel) {
        code = parcel.readString().toString()
        compressor = parcel.readString().toString()
        circuits = parcel.readString().toString()
        others = parcel.readString().toString()

    }

    override fun getContent(): Parcel {
        val parcel:Parcel = Parcel.obtain()

        parcel.writeString(code)
        parcel.writeString(compressor)
        parcel.writeString(circuits)
        parcel.writeString(others)

        return parcel
    }

}