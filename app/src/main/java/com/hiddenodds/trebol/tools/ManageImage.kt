package com.hiddenodds.trebol.tools

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.*
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.hiddenodds.trebol.App
import com.hiddenodds.trebol.R
import com.hiddenodds.trebol.model.persistent.file.ManageFile
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.graphics.image.JPEGFactory
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject
import java.io.*
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
    var scale: Boolean = false
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
                    //executeSaveImage()
                    message = context.getString(R.string.lbl_permission_enabled)
                    observableMessage.onNext(message)
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

    fun scaleBitmap(bitmap: Bitmap): Bitmap{
        val newWidth = bitmap.width/2
        val newHeight = bitmap.height/(bitmap.width/newWidth)
        val newBitmap = Bitmap.createBitmap(newWidth,
                newHeight, Bitmap.Config.ARGB_8888)

        val ratioX = newWidth / bitmap.width.toFloat()
        val ratioY = newHeight / bitmap.height.toFloat()
        val middleX = newWidth / 2.0f
        val middleY = newHeight / 2.0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY)

        val c = Canvas(newBitmap)
        c.matrix = scaleMatrix
        c.drawBitmap(bitmap, middleX - bitmap.width / 2,
                middleY - bitmap.height / 2, Paint(Paint.FILTER_BITMAP_FLAG))
        return newBitmap
    }

    @Throws(IOException::class)
    private fun saveBitmapToJPG(bitmap: Bitmap, signature: File) {
        val newBitmap = scaleBitmap(bitmap)
        val stream = FileOutputStream(signature)
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()
    }

    @Throws(IOException::class)
    private fun saveBitmapToPDF(bitmap: Bitmap, filePdf: File){
        var newBitmap: Bitmap? = null

        newBitmap = if (scale){
            bitmap
        }else{
            scaleBitmap(bitmap)
        }

        val stream = ByteArrayOutputStream()
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val imageInByte = stream.toByteArray()
        val imageInput = ByteArrayInputStream(imageInByte)

        val document = PDDocument()
        val rec = PDRectangle(newBitmap.width.toFloat(), newBitmap.height.toFloat())
        val page = PDPage(rec)
        document.addPage(page)
        val contentStream = PDPageContentStream(document, page)

        val img: PDImageXObject = JPEGFactory
                .createFromStream(document, imageInput)
        
        contentStream.drawImage(img, 0f, 0f)
        contentStream.close()
        document.save(filePdf)
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