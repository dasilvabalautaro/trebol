package com.hiddenodds.trebolv2.tools

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.hiddenodds.trebolv2.App
import com.hiddenodds.trebolv2.R
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
    val REQUEST_EXTERNAL_STORAGE = 1
    private var message: String = ""
    private val ALBUM_APP = "SignatureTrebol"
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
                            REQUEST_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            else -> executeSaveImage()
        }
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
        when {
            requestCode != REQUEST_EXTERNAL_STORAGE -> return
            permissionUtils.isPermissionGranted(permissions, grantResults,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) -> executeSaveImage()
            else -> {
                message = context.getString(R.string.permission_write)
                observableMessage.onNext(message)
            }
        }
    }

    private fun getAlbumStorageDir(): File {
        val file = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), ALBUM_APP)
        if (!file.mkdirs()) {
            println("Directory not created")
        }
        return file
    }

    @Throws(IOException::class)
    private fun saveBitmapToJPG(bitmap: Bitmap, signature: File) {
        val newBitmap = Bitmap.createBitmap(bitmap.width,
                bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(newBitmap)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        val stream = FileOutputStream(signature)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        stream.close()
    }
    @Throws(IOException::class)
    private fun saveBitmapToPDF(bitmap: Bitmap, filePdf: File){
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width,
                bitmap.height, 1).create()
        val page = document.startPage(pageInfo)

        val canvas = page.canvas

        val paint = Paint()
        paint.color = Color.parseColor("#ffffff")
        canvas.drawPaint(paint)


        val bitmapScaled = Bitmap.createScaledBitmap(bitmap,
                bitmap.width, bitmap.height, true)

        paint.color = Color.BLUE
        canvas.drawBitmap(bitmapScaled, 0f, 0f, null)
        document.finishPage(page)
        document.writeTo(FileOutputStream(filePdf))
        document.close()
    }

    fun getSignatureStore(code: String): Bitmap?{
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
        try {
            val pathSignature = File(getAlbumStorageDir(), "$code.jpg")
            if (pathSignature.delete()){
                println("Delete file OK")
            }
        }catch (ie: IOException){
            if (ie.message != null){
                println(ie.message)
            }
        }
    }


}