package com.club.calisthenics.core.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.club.calisthenics.core.domain.repository.StorageRepository
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class FirebaseStorageRepository @Inject constructor(
    private val storage: FirebaseStorage
) : StorageRepository {

    override suspend fun uploadImage(uri: Uri, path: String): Result<String> = try {
        val ref = storage.reference.child(path)
        ref.putFile(uri).await()
        val downloadUrl = ref.downloadUrl.await().toString()
        Result.success(downloadUrl)
    } catch (e: Exception) {
        Result.failure(e)
    }

    override suspend fun uploadBitmap(bitmap: Bitmap, path: String): Result<String> = try {
        val ref = storage.reference.child(path)
        
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos)
        val data = baos.toByteArray()
        
        ref.putBytes(data).await()
        val downloadUrl = ref.downloadUrl.await().toString()
        Result.success(downloadUrl)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
