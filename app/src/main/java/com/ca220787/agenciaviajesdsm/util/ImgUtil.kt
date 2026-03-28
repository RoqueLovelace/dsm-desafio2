package com.ca220787.agenciaviajesdsm.util

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream

class ImgUtil {
    companion object {
        fun uriABase64(contRes: ContentResolver, uriImag: Uri): String {
            val inpStr = contRes.openInputStream(uriImag)
            val bitMap = BitmapFactory.decodeStream(inpStr)
            inpStr?.close()

            val outStr = ByteArrayOutputStream()
            bitMap.compress(Bitmap.CompressFormat.JPEG, 70, outStr)
            val bytArr = outStr.toByteArray()

            return Base64.encodeToString(bytArr, Base64.DEFAULT)
        }

        fun base64ABitmap(txtBase: String): Bitmap? {
            return try {
                val bytArr = Base64.decode(txtBase, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(bytArr, 0, bytArr.size)
            } catch (e: Exception) {
                null
            }
        }
    }
}