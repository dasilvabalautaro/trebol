package com.hiddenodds.trebolv2.model.persistent.caching

import android.support.v4.util.LruCache

class CachingLruRepository private constructor(){
    private val CACHE_SIZE = 1024
    private var lru: LruCache<String, Any>
    private object Holder { val INSTANCE = CachingLruRepository() }

    companion object {
        val instance: CachingLruRepository by lazy { Holder.INSTANCE }
    }

    init {
        val cacheSize = CACHE_SIZE
        lru = LruCache(cacheSize)
    }

    fun getLru(): LruCache<String, Any> = lru

}