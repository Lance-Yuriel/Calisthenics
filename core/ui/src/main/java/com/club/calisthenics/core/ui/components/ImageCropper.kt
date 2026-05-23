package com.club.calisthenics.core.ui.components

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageCropperDialog(
    uri: Uri,
    isCircular: Boolean = true,
    onConfirm: (Bitmap) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(uri) {
        withContext(Dispatchers.IO) {
            try {
                val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
                bitmap = BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("ADJUST IMAGE") },
                    navigationIcon = {
                        IconButton(onClick = onDismiss) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel")
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = {
                                bitmap?.let { b ->
                                    val processed = processImage(
                                        source = b,
                                        isCircular = isCircular,
                                        manualScale = scale,
                                        manualOffset = offset,
                                        density = density.density
                                    )
                                    onConfirm(processed)
                                }
                            },
                            colors = IconButtonDefaults.iconButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Check, contentDescription = "Save")
                        }
                    }
                )
            },
            containerColor = Color.Black
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                bitmap?.let { b ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .pointerInput(Unit) {
                                detectTransformGestures { _, pan, zoom, _ ->
                                    scale = (scale * zoom).coerceIn(0.5f, 5f)
                                    offset += pan
                                }
                            }
                    ) {
                        androidx.compose.foundation.Image(
                            bitmap = b.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer(
                                    scaleX = scale,
                                    scaleY = scale,
                                    translationX = offset.x,
                                    translationY = offset.y
                                ),
                            contentScale = ContentScale.Fit
                        )
                    }

                    // Crop Overlay (300dp circle/rect)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    ) {
                        Box(
                            modifier = Modifier
                                .size(if (isCircular) 300.dp else 340.dp)
                                .height(if (isCircular) 300.dp else 220.dp)
                                .align(Alignment.Center)
                                .clip(if (isCircular) CircleShape else RoundedCornerShape(24.dp))
                                .background(Color.Transparent)
                                .border(2.dp, Color.White, if (isCircular) CircleShape else RoundedCornerShape(24.dp))
                        )
                    }
                    
                    Text(
                        text = "Pinch to zoom • Drag to pan",
                        color = Color.White.copy(alpha = 0.7f),
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 32.dp)
                    )
                } ?: CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

private fun processImage(
    source: Bitmap,
    isCircular: Boolean,
    manualScale: Float,
    manualOffset: Offset,
    density: Float
): Bitmap {
    // New aggressive minimization targets:
    // Profile: 320x320
    // Event: 800x500
    val targetWidth = if (isCircular) 320 else 800
    val targetHeight = if (isCircular) 320 else 500
    
    val result = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(result)
    val matrix = Matrix()

    // Screen dimensions assumption (robustness check: typically 1080p width on modern Android for math)
    val viewWidth = 1080f
    val viewHeight = 1920f 
    
    // 1. Replicate ContentScale.Fit logic
    val fitScale = Math.min(viewWidth / source.width, viewHeight / source.height)
    val displayedWidth = source.width * fitScale
    val displayedHeight = source.height * fitScale
    
    // 2. The overlay on screen is 300dp (approx 300 * density px)
    val overlaySizePx = (if (isCircular) 300f else 340f) * density
    val overlayHeightPx = (if (isCircular) 300f else 220f) * density
    
    // 3. Map screen space back to bitmap space
    // Scale factor between the saved image and the screen display
    val screenToTargetScale = targetWidth.toFloat() / overlaySizePx
    
    val totalScale = fitScale * manualScale * screenToTargetScale
    matrix.postScale(totalScale, totalScale)
    
    // 4. Centering + Manual Offset correction
    val dx = (targetWidth - source.width * totalScale) / 2f + (manualOffset.x * screenToTargetScale)
    val dy = (targetHeight - source.height * totalScale) / 2f + (manualOffset.y * screenToTargetScale)
    
    matrix.postTranslate(dx, dy)
    canvas.drawBitmap(source, matrix, null)
    
    return result
}
