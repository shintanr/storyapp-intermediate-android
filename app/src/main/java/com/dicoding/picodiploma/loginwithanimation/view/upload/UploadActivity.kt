package com.dicoding.picodiploma.loginwithanimation.view.upload

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityUploadBinding
import com.dicoding.picodiploma.loginwithanimation.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.uriToFile
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity

class UploadActivity : AppCompatActivity() {
    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityUploadBinding
    private var photoUri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)

        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        photoUri = Uri.parse(intent.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE))

        Glide.with(this)
            .load(photoUri)
            .into(binding.ivStory)

        binding.btnClose.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.uploadButton.setOnClickListener { uploadImage() }
    }


    @RequiresApi(Build.VERSION_CODES.Q)
    private fun uploadImage() {
        photoUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.edAddDescription.text.toString().trim()

            viewModel.uploadImage(imageFile, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is ResultState.Loading -> {

                        }

                        is ResultState.Success -> {
                            showToast(result.data.message)
                            startActivity(Intent(this@UploadActivity, MainActivity::class.java))
                            finish()
                        }

                        is ResultState.Error -> {
                            showToast(result.error)
                                                    }
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


}