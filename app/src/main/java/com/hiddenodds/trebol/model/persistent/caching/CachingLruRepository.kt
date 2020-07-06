package com.hiddenodds.trebol.model.persistent.caching

import androidx.collection.LruCache

class CachingLruRepository private constructor(){
    private var lru: LruCache<String, Any>
    private object Holder { val INSTANCE = CachingLruRepository() }

    companion object {
        val instance: CachingLruRepository by lazy { Holder.INSTANCE }
    }

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024)
        val cacheSize = (maxMemory / 4).toInt()
        lru = LruCache(cacheSize)
    }

    fun getLru(): LruCache<String, Any> = lru

    fun delLru(key: String){
        getLru().remove(key)
    }
}