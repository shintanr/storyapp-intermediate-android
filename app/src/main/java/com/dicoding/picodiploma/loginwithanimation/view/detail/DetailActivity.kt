package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.withDateFormat

class DetailActivity : AppCompatActivity() {
    private val viewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val storyId = intent.getStringExtra(EXTRA_ID)
        storyId?.run(viewModel::setStoryId)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.button_back)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.detailStory.observe(this) { result ->
            when (result) {
                is ResultState.Error -> {
                    showLoading(false)
                    Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                }
                is ResultState.Loading -> { showLoading(true) }
                is ResultState.Success -> {
                    showLoading(false)
                    val item = result.data
                    binding.apply {
                        Glide.with(this@DetailActivity)
                            .load(item.photoUrl)
                            .into(binding.ImageView)
                        toolbar.title = item.name
                        toolbar.subtitle = item.createdAt?.withDateFormat()
                        tvDesc.text = item.description
                    }
                }
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        const val EXTRA_ID = "id"
    }

}