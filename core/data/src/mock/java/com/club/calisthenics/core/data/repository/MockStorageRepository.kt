package com.club.calisthenics.core.data.repository

import android.graphics.Bitmap
import android.net.Uri
import com.club.calisthenics.core.domain.repository.StorageRepository
import javax.inject.Inject

class MockStorageRepository @Inject constructor() : StorageRepository {
    override suspend fun uploadImage(uri: Uri, path: String): Result<String> {
        return Result.success("https://images.unsplash.com/photo-1574673139722-c48b4f582cd3?q=80&w=256&h=256&auto=format&fit=crop")
    }

    override suspend fun uploadBitmap(bitmap: Bitmap, path: String): Result<String> {
        return Result.success("https://images.unsplash.com/photo-1574673139722-c48b4f582cd3?q=80&w=256&h=256&auto=format&fit=crop")
    }
}
