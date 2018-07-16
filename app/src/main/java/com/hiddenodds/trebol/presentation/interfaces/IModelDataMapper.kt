package com.hiddenodds.trebol.presentation.interfaces

import io.realm.RealmObject

interface IModelDataMapper {
    fun <E : RealmObject> transform(objectRealm: E?): IModel
    fun <E : RealmObject> transform(objectRealmCollection: Collection<E>?):
            Collection<IModel>
}