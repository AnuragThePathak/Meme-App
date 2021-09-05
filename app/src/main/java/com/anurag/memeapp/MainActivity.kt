package com.anurag.memeapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.anurag.memeapp.databinding.ActivityMainBinding
import com.bumptech.glide.Glide

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        loadMeme()
    }

    private fun loadMeme() {
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val nameUrl = response.getString("url")
                Glide.with(this).load(nameUrl).into(binding.imageView)
            },
            {
                Toast.makeText(this, "Something went wrong.", Toast.LENGTH_SHORT).show()
            }
        )

// Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest)
    }

    fun nextMeme(view: android.view.View) {}
    fun shareMeme(view: android.view.View) {}
}