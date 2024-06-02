package com.dicoding.picodiploma.loginwithanimation.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.data.ResultState
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityLoginBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupAction()
        setupEditTextListeners()
        playAnimation()
    }

    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            @Suppress("DEPRECATION")
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            if (!isFormValid(email, password)) {
                return@setOnClickListener
            }

            viewModel.login(email, password).observe(this) { user ->
                when (user) {
                    is ResultState.Error -> {
                        showToast(user.error)
                    }
                    is ResultState.Loading -> { }
                    is ResultState.Success -> {
                        showToast(user.data.message)
                        AlertDialog.Builder(this).apply {
                            setTitle("Hore")
                            setMessage("Selamat Anda berhasil login!")
                            setPositiveButton("Lanjut") { dialog, _ ->
                                saveSession(
                                    UserModel(
                                        user.data.loginResult.name,
                                        user.data.loginResult.token,
                                        user.data.loginResult.userId,
                                        true
                                    )
                                )
                                dialog.dismiss()
                            }
                            Log.e("Login", user.data.loginResult.token)
                            create()
                            show()
                        }
                    }
                }
            }
        }
    }

    private fun saveSession(session: UserModel) {
        lifecycleScope.launch {
            viewModel.saveSession(session)
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            ViewModelFactory.clearInstance()
            startActivity(intent)
        }
    }

    private fun setupEditTextListeners() {
        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun validatePassword(password: String) {
        when {
            password.isEmpty() -> binding.passwordEditText.error = "Password harus diisi"
            password.length < 8 -> binding.passwordEditText.error = "Password minimal 8 karakter"
            else -> binding.passwordEditText.error = null
        }
    }

    private fun isFormValid(email: String, password: String): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            binding.emailEditText.error = "Email harus diisi"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEditText.error = "Email tidak sesuai"
            isValid = false
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password harus diisi"
            isValid = false
        } else if (password.length < 8) {
            binding.passwordEditText.error = "Password minimal 8 karakter"
            isValid = false
        }

        return isValid
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val emailTextView = ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(100)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val passwordTextView = ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(100)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                login
            )
            startDelay = 100
        }.start()
    }
}
