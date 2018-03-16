package com.hiddenodds.trebolv2.model.persistent.caching

import android.support.v4.util.LruCache

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

}