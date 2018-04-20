package com.hiddenodds.trebolv2.tools

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
import com.hiddenodds.trebolv2.model.persistent.file.ManageFile
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ManageImage @Inject constructor(private val permissionUtils:
                                      PermissionUtils):
        ActivityCompat.OnRequestPermissionsResultCallback {

    private val context = App.appComponent.context()
    var image: Bitmap? = null
    var code: String = ""
    var flagPdf = false
    val WRITE_EXTERNAL_STORAGE = 1
    val READ_EXTERNAL_STORAGE = 2
    private var message: String = ""
    var observableMessage: Subject<String> =
            PublishSubject.create()

    init {
        observableMessage
                .subscribe { message }
    }

    fun addFileToGallery(activity: Activity){
        when {
            ContextCompat.checkSelfPermission(context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED -> permissionUtils
                    .requestPermission(activity,
                            WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            else -> executeSaveImage()
        }
    }

    fun getFileOfGallery(activity: Activity):Bitmap?{
        when {
            ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) !=
                    PackageManager.PERMISSION_GRANTED -> permissionUtils
                    .requestPermission(activity,
                            READ_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
            else -> return getSignatureStore()
        }
        return null
    }

    private fun executeSaveImage(){
        when(flagPdf){
            true -> {
                flagPdf = false
                executeSaveImageToPdf()
            }
            false -> {
                executeSaveImageToJPG()
            }
        }
    }

    private fun executeSaveImageToJPG(){
        try {
            val signatureImageFile = File(getAlbumStorageDir(), "$code.jpg")
            saveBitmapToJPG(image!!, signatureImageFile)
            message = context.getString(R.string.image_save)
            observableMessage.onNext(message)
        }catch (ie: IOException){
            if (ie.message != null){
                message = ie.message!!
                observableMessage.onNext(message)
            }
        }
    }

    private fun executeSaveImageToPdf(){
        try {
            val imageFile = File(getAlbumStorageDir(), "$code.pdf")
            saveBitmapToPDF(image!!, imageFile)
            message = context.getString(R.string.image_save)
            observableMessage.onNext(message)
        }catch (ie: IOException){
            if (ie.message != null){
                message = ie.message!!
                observableMessage.onNext(message)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        when(requestCode) {

            WRITE_EXTERNAL_STORAGE ->{
                if (permissionUtils
                                .permissionGranted(requestCode,
                                        WRITE_EXTERNAL_STORAGE,
                                        grantResults)) {
                    executeSaveImage()
                }
            }
            READ_EXTERNAL_STORAGE ->{
                if (permissionUtils
                                .permissionGranted(requestCode,
                                        READ_EXTERNAL_STORAGE,
                                        grantResults)) {
                    message = context.getString(R.string.lbl_permission_enabled)
                    observableMessage.onNext(message)
                }
            }

        }
    }

    private fun getAlbumStorageDir(): File {
        return ManageFile.getAlbumStorageDir()
    }

    @Throws(IOException::class)
    private fun saveBitmapToJPG(bitmap: Bitmap, signature: File) {
        val newWidth = bitmap.width/2
        val newHeight = bitmap.height/(bitmap.width/newWidth)

        val newBitmap = Bitmap.createBitmap(newWidth,
                newHeight, Bitmap.Config.RGB_565)

        val bitmapScaled = Bitmap.createScaledBitmap(bitmap,
                newWidth, newHeight, false)

        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmapScaled, 0f, 0f, null)
        val stream = FileOutputStream(signature)
        bitmapScaled.compress(Bitmap.CompressFormat.JPEG, 40, stream)
        stream.close()
    }
    @Throws(IOException::class)
    private fun saveBitmapToPDF(bitmap: Bitmap, filePdf: File){
        val newWidth = bitmap.width/2
        val newHeight = bitmap.height/(bitmap.width/newWidth)
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(newWidth,
                newHeight, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas
        val bitmapScaled = Bitmap.createScaledBitmap(bitmap,
                newWidth, newHeight, false)

        canvas.drawBitmap(bitmapScaled, 0f, 0f, null)

        document.finishPage(page)

        document.writeTo(FileOutputStream(filePdf))
        document.close()
    }

    private fun getSignatureStore(): Bitmap?{
        var bitmap: Bitmap? = null
        try {
            val pathSignature = File(getAlbumStorageDir(), "$code.jpg")
            val contentUri = Uri.fromFile(pathSignature)
            bitmap = BitmapFactory.decodeStream(context
                    .contentResolver.openInputStream(contentUri))

        }catch (ie: IOException){
            if (ie.message != null){
                println(ie.message)
            }
        }
        return bitmap
    }

    fun deleteSignatureStore(code:String){
        ManageFile.deleteFileOfWork(code)
    }


}