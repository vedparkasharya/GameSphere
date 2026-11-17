package com.snapify.creator

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : ComponentActivity() {
    private var selectedUri: Uri? = null
    private var selectedMime: String = "*/*"
    private lateinit var status: TextView
    private lateinit var preview: ImageView

    private val mediaPicker = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedUri = uri
        if (uri == null) {
            status.text = "No media selected"
            preview.setImageDrawable(null)
            return@registerForActivityResult
        }
        selectedMime = contentResolver.getType(uri) ?: "*/*"
        status.text = "Selected: " + if (selectedMime.startsWith("video/")) "Video" else "Image"
        status.append("\nReady to share")
        if (selectedMime.startsWith("image/")) {
            preview.setImageURI(uri)
            preview.visibility = ImageView.VISIBLE
        } else {
            preview.setImageDrawable(null)
            preview.visibility = ImageView.GONE
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER_HORIZONTAL
            setPadding(28, 48, 28, 28)
            setBackgroundColor(0xFF101014.toInt())
        }
        val heading = TextView(this).apply {
            text = "Snapify Creator"
            textSize = 28f
            setTextColor(0xFFFFFFFF.toInt())
            gravity = Gravity.CENTER
        }
        val subtitle = TextView(this).apply {
            text = "Choose a photo or video, preview it, then share using Android's supported Snapchat handoff."
            textSize = 14f
            setTextColor(0xFFCBCBD2.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 14, 0, 24)
        }
        preview = ImageView(this).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
            visibility = ImageView.GONE
            adjustViewBounds = true
        }
        status = TextView(this).apply {
            text = "Nothing selected yet"
            textSize = 16f
            setTextColor(0xFFFFFFFF.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 20, 0, 20)
        }
        val choose = Button(this).apply {
            text = "Choose photo or video"
            setOnClickListener {
                mediaPicker.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo)
                )
            }
        }
        val share = Button(this).apply {
            text = "Share to Snapchat"
            setOnClickListener { shareSelectedMedia() }
        }
        val note = TextView(this).apply {
            text = "Snapchat decides whether imported media is labelled as a gallery upload. This app does not disguise gallery media as a camera capture."
            textSize = 12f
            setTextColor(0xFFAAAAAF.toInt())
            gravity = Gravity.CENTER
            setPadding(0, 24, 0, 0)
        }
        root.addView(heading, matchWrap())
        root.addView(subtitle, matchWrap())
        root.addView(preview, LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f))
        root.addView(status, matchWrap())
        root.addView(choose, matchWrap())
        root.addView(share, matchWrap())
        root.addView(note, matchWrap())
        setContentView(root)
    }

    private fun shareSelectedMedia() {
        val uri = selectedUri
        if (uri == null) {
            Toast.makeText(this, "Choose a photo or video first", Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = selectedMime
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        val snapchatInstalled = try {
            packageManager.getPackageInfo("com.snapchat.android", 0)
            true
        } catch (_: Exception) {
            false
        }
        if (snapchatInstalled) {
            intent.setPackage("com.snapchat.android")
            try {
                startActivity(intent)
            } catch (_: Exception) {
                intent.setPackage(null)
                startActivity(Intent.createChooser(intent, "Share media"))
            }
        } else {
            startActivity(Intent.createChooser(intent, "Share media"))
        }
    }

    private fun matchWrap() = LinearLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )
}
