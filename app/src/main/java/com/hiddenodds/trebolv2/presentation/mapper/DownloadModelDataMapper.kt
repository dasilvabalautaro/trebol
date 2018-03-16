package com.hiddenodds.trebolv2.presentation.mapper

import android.content.Context
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.data.Download
import com.hiddenodds.trebolv2.presentation.model.DownloadModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadModelDataMapper @Inject constructor(val context: Context) {

    fun transform(download: Download?): DownloadModel {
        if (download == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val downloadModel = DownloadModel()
        downloadModel.id = download.id
        downloadModel.code = download.code
        downloadModel.notification = download.notification
        downloadModel.customer = download.customer
        downloadModel.state = download.state
        return downloadModel
    }

    fun transform(downloadCollection: Collection<Download>?): Collection<DownloadModel>{
        val downloadModelCollection: MutableCollection<DownloadModel> = ArrayList()

        if (downloadCollection != null && !downloadCollection.isEmpty())
            downloadCollection.mapTo(downloadModelCollection) { transform(it) }
        return downloadModelCollection
    }

}