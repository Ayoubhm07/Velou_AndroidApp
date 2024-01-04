package tn.esprit.gestionpost

import android.content.Context
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

fun Uri?.toMultipartBody(context: Context?, partName: String): MultipartBody.Part? {
    if (context == null) {
        return null
    }
    val inputStream = this?.let { context.contentResolver.openInputStream(it) }

    // Créez un fichier temporaire dans le répertoire cache de l'application
    val file = File(context.cacheDir, "temp_image_file")

    // Copiez l'InputStream dans le fichier temporaire
    inputStream?.copyTo(file.outputStream())

    // Créez un RequestBody à partir du fichier
    val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

    // Créez le MultipartBody.Part avec le nom spécifié
    return MultipartBody.Part.createFormData(partName, file.name, requestFile)
}