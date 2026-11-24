package com.example.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Html
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import com.example.domain.model.AppLanguage
import com.example.qr.QrCodeUtils
import com.example.qr.WebCompanionHtmlBuilder
import com.example.ui.components.QrCameraScanner
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.JetBrainsMonoFontFamily
import com.example.ui.viewmodel.ArchUiState
import kotlinx.coroutines.launch

@Composable
fun QrWebScreen(
    uiState: ArchUiState,
    onSetWebBaseUrl: (String) -> Unit,
    onOpenTopicInApp: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var activeTab by remember { mutableIntStateOf(0) } // 0 = Share Web QR, 1 = Scan QR
    var webPreviewUrl by remember { mutableStateOf<String?>(null) }
    var previewOfflineHtml by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 820.dp)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = when (uiState.progress.language) {
                        AppLanguage.TELUGU -> "వెబ్ QR షేర్ & స్కానర్"
                        AppLanguage.HINGLISH -> "Website QR Share & Scanner"
                        AppLanguage.ENGLISH -> "Web QR Share & Scanner"
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = when (uiState.progress.language) {
                        AppLanguage.TELUGU -> "ఈ QR స్కాన్ చేస్తే Android & Apple ఫోన్లలో యాప్ బదులు వెబ్‌సైట్ తెరుచుకుంటుంది."
                        AppLanguage.HINGLISH -> "Is QR ko Android ya Apple iPhone se scan karne par app ke bajaye seedha Website khulegi."
                        AppLanguage.ENGLISH -> "Others can scan this QR on Android or Apple to open the study guide as a Website instead of installing the app."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    SegmentedButton(
                        selected = activeTab == 0,
                        onClick = { activeTab = 0 },
                        shape = SegmentedButtonDefaults.itemShape(index = 0, count = 2),
                        icon = { Icon(Icons.Default.QrCode2, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.testTag("qr_tab_share")
                    ) {
                        Text(
                            text = when (uiState.progress.language) {
                                AppLanguage.TELUGU -> "వెబ్ QR షేర్"
                                AppLanguage.HINGLISH -> "Share Web QR"
                                AppLanguage.ENGLISH -> "Share Web QR"
                            }
                        )
                    }
                    SegmentedButton(
                        selected = activeTab == 1,
                        onClick = { activeTab = 1 },
                        shape = SegmentedButtonDefaults.itemShape(index = 1, count = 2),
                        icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        modifier = Modifier.testTag("qr_tab_scan")
                    ) {
                        Text(
                            text = when (uiState.progress.language) {
                                AppLanguage.TELUGU -> "QR స్కాన్ చేయి"
                                AppLanguage.HINGLISH -> "Scan QR"
                                AppLanguage.ENGLISH -> "Scan QR"
                            }
                        )
                    }
                }
            }

            if (activeTab == 0) {
                ShareWebQrTab(
                    uiState = uiState,
                    onSetWebBaseUrl = onSetWebBaseUrl,
                    onPreviewOfflineWebsite = {
                        previewOfflineHtml = true
                        webPreviewUrl = "offline"
                    },
                    onShowMessage = { msg -> scope.launch { snackbarHostState.showSnackbar(msg) } },
                    modifier = Modifier.weight(1f)
                )
            } else {
                ScanQrTab(
                    language = uiState.progress.language,
                    onOpenExternalWebsite = { url ->
                        runCatching {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        }.onFailure {
                            previewOfflineHtml = false
                            webPreviewUrl = url
                        }
                    },
                    onPreviewWebsiteInApp = { url ->
                        previewOfflineHtml = false
                        webPreviewUrl = url
                    },
                    onOpenTopicInApp = onOpenTopicInApp,
                    onShowMessage = { msg -> scope.launch { snackbarHostState.showSnackbar(msg) } },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(12.dp)
        )
    }

    if (webPreviewUrl != null) {
        WebPreviewDialog(
            url = webPreviewUrl!!,
            useOfflineCompanion = previewOfflineHtml,
            language = uiState.progress.language,
            onDismiss = { webPreviewUrl = null }
        )
    }
}

@Composable
private fun ShareWebQrTab(
    uiState: ArchUiState,
    onSetWebBaseUrl: (String) -> Unit,
    onPreviewOfflineWebsite: () -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboard = LocalClipboardManager.current
    val lang = uiState.progress.language

    var selectedTopicId by remember { mutableStateOf<String?>(null) }
    var urlDraft by remember(uiState.progress.webShareBaseUrl) {
        mutableStateOf(uiState.progress.webShareBaseUrl)
    }

    val fullShareUrl = remember(urlDraft, selectedTopicId, lang) {
        QrCodeUtils.buildWebShareUrl(urlDraft, selectedTopicId, lang)
    }
    val qrBitmap = remember(fullShareUrl) {
        QrCodeUtils.generateQrBitmap(fullShareUrl, 640)
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // QR Display Card
        item {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), RoundedCornerShape(22.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        color = Color.White,
                        shape = RoundedCornerShape(18.dp),
                        tonalElevation = 4.dp,
                        modifier = Modifier
                            .size(230.dp)
                            .border(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(18.dp))
                    ) {
                        Image(
                            bitmap = qrBitmap.asImageBitmap(),
                            contentDescription = "Scannable Website QR Code",
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(10.dp)
                                .testTag("generated_qr_image")
                        )
                    }

                    Text(
                        text = fullShareUrl,
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = JetBrainsMonoFontFamily),
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                clipboard.setText(AnnotatedString(fullShareUrl))
                                onShowMessage("Website link copied")
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("copy_web_link_btn")
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy URL")
                        }
                        Button(
                            onClick = { QrCodeUtils.shareQrBitmap(context, qrBitmap, fullShareUrl) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("share_qr_btn")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Share QR")
                        }
                    }
                }
            }
        }

        // Topic Filter for QR Deep-Link
        item {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = when (lang) {
                        AppLanguage.TELUGU -> "QR కోసం అంశాన్ని ఎంచుకోండి"
                        AppLanguage.HINGLISH -> "QR Link Target Chuno"
                        AppLanguage.ENGLISH -> "Choose QR Link Target"
                    },
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        FilterChip(
                            selected = selectedTopicId == null,
                            onClick = { selectedTopicId = null },
                            label = { Text("Full Website") }
                        )
                    }
                    items(uiState.allTopics, key = { it.id }) { topic ->
                        FilterChip(
                            selected = selectedTopicId == topic.id,
                            onClick = { selectedTopicId = topic.id },
                            label = { Text(topic.title) }
                        )
                    }
                }
            }
        }

        // Editable Website URL + Self-Hosted Web HTML Export
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f), RoundedCornerShape(18.dp))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "Website URL & Standalone Web Export",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    OutlinedTextField(
                        value = urlDraft,
                        onValueChange = { urlDraft = it },
                        label = { Text("Hosted Website URL (opens in Safari / Chrome)") },
                        singleLine = true,
                        trailingIcon = {
                            if (urlDraft != QrCodeUtils.DEFAULT_WEB_SHARE_URL) {
                                IconButton(
                                    onClick = {
                                        urlDraft = QrCodeUtils.DEFAULT_WEB_SHARE_URL
                                        onSetWebBaseUrl(QrCodeUtils.DEFAULT_WEB_SHARE_URL)
                                    },
                                    modifier = Modifier.size(48.dp)
                                ) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Reset default URL")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("web_url_input")
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                onSetWebBaseUrl(urlDraft)
                                onShowMessage("Website URL saved to QR")
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("save_web_url_btn")
                        ) {
                            Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save URL")
                        }
                        OutlinedButton(
                            onClick = onPreviewOfflineWebsite,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp)
                                .testTag("preview_web_btn")
                        ) {
                            Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Preview Web")
                        }
                    }

                    Button(
                        onClick = {
                            val html = WebCompanionHtmlBuilder.buildStandaloneHtml(lang)
                            QrCodeUtils.shareHtmlWebsiteFile(context, html)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("export_html_btn")
                    ) {
                        Icon(Icons.Default.Html, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export Web Website (index.html)")
                    }
                    Text(
                        text = "Tip: Upload index.html to Netlify Drop or GitHub Pages and paste your link above so anyone on Apple or Android opens the live website via QR.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ScanQrTab(
    language: AppLanguage,
    onOpenExternalWebsite: (String) -> Unit,
    onPreviewWebsiteInApp: (String) -> Unit,
    onOpenTopicInApp: (String) -> Unit,
    onShowMessage: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                PackageManager.PERMISSION_GRANTED
        )
    }
    var scannedResult by remember { mutableStateOf<String?>(null) }

    val cameraPermLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasCameraPermission = granted
        if (!granted) {
            onShowMessage("Camera permission denied — you can still pick a QR image from Photos")
        }
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            val decoded = runCatching {
                val bmp = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    val src = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(src) { decoder, _, _ ->
                        decoder.isMutableRequired = false
                        decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                    }
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                }
                QrCodeUtils.decodeQrFromBitmap(bmp)
            }.getOrNull()

            if (!decoded.isNullOrBlank()) {
                scannedResult = decoded
            } else {
                onShowMessage("No QR code found in the selected image")
            }
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            if (scannedResult != null) {
                val raw = scannedResult!!
                val isWeb = QrCodeUtils.isWebUrl(raw)
                val openableUrl = if (isWeb) QrCodeUtils.normalizeOpenableUrl(raw) else null
                val matchedTopicId = QrCodeUtils.extractArchTopicIdFromUrl(raw)

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, EmeraldSuccess, RoundedCornerShape(20.dp))
                        .testTag("qr_scan_result_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isWeb) "Website QR Detected" else "QR Code Scanned",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = raw,
                                style = MaterialTheme.typography.labelMedium.copy(fontFamily = JetBrainsMonoFontFamily),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(12.dp)
                            )
                        }

                        if (openableUrl != null) {
                            Button(
                                onClick = { onOpenExternalWebsite(openableUrl) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("open_as_website_btn")
                            ) {
                                Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Open as Website (Browser)")
                            }
                            OutlinedButton(
                                onClick = { onPreviewWebsiteInApp(openableUrl) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Preview Website In-App")
                            }
                        }

                        if (matchedTopicId != null) {
                            OutlinedButton(
                                onClick = { onOpenTopicInApp(matchedTopicId) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                            ) {
                                Text("Open Topic in App Instead")
                            }
                        }

                        OutlinedButton(
                            onClick = { scannedResult = null },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("scan_again_btn")
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Scan Another QR")
                        }
                    }
                }
            } else if (hasCameraPermission) {
                QrCameraScanner(
                    onQrDetected = { text -> scannedResult = text },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(340.dp)
                )
            } else {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Camera permission",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(46.dp)
                        )
                        Text(
                            text = when (language) {
                                AppLanguage.TELUGU -> "QR స్కాన్ చేయడానికి కెమెరాను అనుమతించండి"
                                AppLanguage.HINGLISH -> "QR Scan karne ke liye Camera Permission dein"
                                AppLanguage.ENGLISH -> "Allow Camera Access to Scan QR Codes"
                            },
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Point your camera at any website QR code to open it directly in the browser.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = { cameraPermLauncher.launch(Manifest.permission.CAMERA) },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("grant_camera_btn")
                        ) {
                            Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Enable Camera Scanner")
                        }
                    }
                }
            }
        }

        item {
            OutlinedButton(
                onClick = {
                    photoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("pick_qr_photo_btn")
            ) {
                Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Pick QR Image from Photos")
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun WebPreviewDialog(
    url: String,
    useOfflineCompanion: Boolean,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val htmlData = remember(useOfflineCompanion, language) {
        if (useOfflineCompanion) WebCompanionHtmlBuilder.buildStandaloneHtml(language) else null
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (useOfflineCompanion) "Web Companion Preview" else url,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("close_web_preview_btn")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close web preview")
                    }
                }

                AndroidView(
                    factory = { ctx ->
                        WebView(ctx).apply {
                            settings.javaScriptEnabled = true
                            settings.domStorageEnabled = true
                            webViewClient = WebViewClient()
                            if (htmlData != null) {
                                loadDataWithBaseURL(
                                    "https://arch3dcoa.local/",
                                    htmlData,
                                    "text/html",
                                    "UTF-8",
                                    null
                                )
                            } else {
                                loadUrl(url)
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
            }
        }
    }
}
