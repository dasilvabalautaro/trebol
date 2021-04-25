package com.hiddenodds.trebol.model.persistent.file

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.core.app.ActivityCompat.startActivityForResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FileManager @Inject constructor(){

    private val PICK_PHOTO_CODE = 1046


    fun onPickPhoto(activity: Activity) {
        // Create intent for picking a photo from the gallery
        val intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(activity.packageManager) != null) {
            // Bring up gallery to select a photo
            activity.startActivityForResult(intent, PICK_PHOTO_CODE)
        }
    }

    fun insertImage(activity: Activity){
        val resolver = activity.applicationContext.contentResolver
        val imagesCollection = MediaStore.Images
                .Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val newImage = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "test.jpg")
        }
        val imageFav = resolver.insert(imagesCollection, newImage)
    }
}