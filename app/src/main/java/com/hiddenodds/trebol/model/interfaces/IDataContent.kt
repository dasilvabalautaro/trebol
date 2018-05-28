package com.hiddenodds.trebol.model.interfaces

import android.os.Parcel

interface IDataContent {
    fun setContent(parcel: Parcel)
    fun getContent(): Parcel
}