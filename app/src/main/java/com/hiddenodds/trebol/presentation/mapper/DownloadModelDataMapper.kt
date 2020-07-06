package com.hiddenodds.trebol.presentation.mapper

import android.content.Context
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.data.Download
import com.hiddenodds.trebol.presentation.interfaces.IModel
import com.hiddenodds.trebol.presentation.interfaces.IModelDataMapper
import com.hiddenodds.trebol.presentation.model.DownloadModel
import io.realm.RealmObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DownloadModelDataMapper @Inject constructor(val context: Context):
        IModelDataMapper {
    override fun <E : RealmObject> transform(objectRealm: E?): IModel {
        if (objectRealm == null)
            throw IllegalArgumentException(context.getString(R.string.value_null))
        val download = objectRealm as Download
        val downloadModel = DownloadModel()
        downloadModel.id = download.id
        downloadModel.code = download.code
        downloadModel.notification = download.notification
        downloadModel.customer = download.customer
        downloadModel.state = download.state
        return downloadModel
    }

    override fun <E : RealmObject> transform(objectRealmCollection:
                                             Collection<E>?): Collection<IModel> {
        val downloadCollection = objectRealmCollection!!
                .filterIsInstance<Download>() as Collection<Download>
        val downloadModelCollection: MutableCollection<IModel> = ArrayList()

        if (!downloadCollection.isEmpty())
            downloadCollection.mapTo(downloadModelCollection) { transform(it) }
        return downloadModelCollection
    }


}