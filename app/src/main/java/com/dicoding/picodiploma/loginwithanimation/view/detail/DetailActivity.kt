package com.dicoding.picodiploma.loginwithanimation.view.detail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailBinding
import com.dicoding.picodiploma.loginwithanimation.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.withDateFormat


@Suppress("DEPRECATION")
class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val detail = intent.getParcelableExtra<ListStoryItem>(DetailActivity.DETAIL_STORY) as ListStoryItem
        setupAction(detail)

        supportActionBar?.show()
        supportActionBar?.title = "Detail Story"



    }

    private fun setupAction(detail: ListStoryItem){
        Glide.with(applicationContext)
            .load(detail.photoUrl)
            .into(binding.avatarDetail)
        binding.tvName.text = detail.name
        binding.tvDesc.text = detail.description
    }
    companion object {
        const val DETAIL_STORY = "detail_story"
    }
}
