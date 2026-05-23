package com.club.calisthenics.core.domain.repository

import android.graphics.Bitmap
import android.net.Uri

interface StorageRepository {
    suspend fun uploadImage(uri: Uri, path: String): Result<String>
    suspend fun uploadBitmap(bitmap: Bitmap, path: String): Result<String>
}
