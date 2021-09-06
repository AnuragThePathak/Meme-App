package com.anurag.memeapp

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.anurag.memeapp.databinding.ActivityMainBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var currentImageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        loadMeme()
    }

    private fun loadMeme() {
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
//        progressBar.isVisible=true

        val url = "https://meme-api.herokuapp.com/gimme"

//        Creating the request using JsonObjectRequest function.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,

            { response ->
                val nameUrl = response.getString("url")
                currentImageUrl = nameUrl

//                Glide manages the image loading and caching.
                Glide.with(this).load(nameUrl).listener(
                    object : RequestListener<Drawable> {

                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            progressBar.visibility = View.GONE
                            return false
                        }
                    }).into(binding.imageView)
            },

            {
                progressBar.visibility = View.GONE
//                progressBar.isVisible = false

                Toast.makeText(
                    this, "Something went wrong.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        )

// Add the request to requestQueue.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun nextMeme(view: View) = this.loadMeme()

    //    This is the way to share the link not the image itself.
    fun shareMeme(view: View) {
        val intent = Intent().apply {
            Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, currentImageUrl)
            type = "text/plain"
        }

        startActivity(Intent.createChooser(intent, null))
    }
}