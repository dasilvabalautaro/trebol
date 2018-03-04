package com.hiddenodds.trebolv2.model.interfaces

import android.os.Parcel

interface IDataContent {
    fun setContent(parcel: Parcel)
    fun getContent(): Parcel
}