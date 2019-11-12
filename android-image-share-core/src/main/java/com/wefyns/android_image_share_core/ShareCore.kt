package com.wefyns.android_image_share_core

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import kotlin.random.Random

@SuppressLint("StaticFieldLeak")
object ShareCore {
    lateinit var context: Context
    lateinit var AUTHORITY: String
    fun setup(context: Context, authhority: String) {
        this.context = context
        this.AUTHORITY = authhority
    }

    fun getFileFormat(link: String): String =
        if (link.contains("."))
            link.substring(
                link.lastIndexOf("."),
                if (link.contains("?")) link.lastIndexOf("?") else link.length
            )
        else ".png"

    fun getFileFormatLetters(link: String): String =
        if (link.contains("."))
            link.substring(
                link.lastIndexOf(".") + 1,
                if (link.contains("?")) link.lastIndexOf("?") else link.length
            )
        else ".png"

    fun clearFolderFiles(folder: File) {
        if (folder.listFiles() != null) {
            for (file in folder.listFiles()) {
                file.delete()
            }
        }
        if (!folder.exists()) {
            folder.mkdir()
        }
    }

    fun getURI(path: String): Uri {
        val file = File(path)
        if (file.exists()) {
            val bytes = ByteArray(file.length().toInt())
            val inputStream = FileInputStream(file)
            inputStream.read(bytes)

            val cacheFolder = File(context.getCacheDir(), "images")
            clearFolderFiles(cacheFolder)
            val shareFile = File(cacheFolder, "${Random.nextInt(1, 100)}shared_image${getFileFormat(path)}")
            val output = FileOutputStream(shareFile)
            output.write(bytes)

            return FileProvider.getUriForFile(context, AUTHORITY, shareFile)
        }
        return Uri.parse("")
    }

    fun writeImgToDisk(bitmap: Bitmap, name: String): String {
        val dir = File(context.getApplicationInfo().dataDir + File.separator + "trash")
        dir.mkdir()
        val path = File(dir.absolutePath + File.separator + name)
        try {
            FileOutputStream(path).use { out ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return path.absolutePath
    }


    fun share(path: String?, text: String?, desc: String) {
        val intent = Intent(Intent.ACTION_SEND)
        //Отправить картинку с текстом или без текста
        if (path != null || text != null) {
            if (path != null) {
                val uri: Uri? = getURI(path)
                if (uri != null) {
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    intent.putExtra(Intent.EXTRA_TEXT, text ?: "")
                    intent.putExtra(Intent.EXTRA_STREAM, uri)
                    intent.setType("image/" + getFileFormatLetters(path))
                }
            }
            //Отправить текст
            if (path == null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra(Intent.EXTRA_TEXT, text)
                intent.setType("text/*")
            }
            context.startActivity(Intent.createChooser(intent, desc))
        } else {
            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
        }
    }
}