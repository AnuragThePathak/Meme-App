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
    private var urlMeme = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)

        loadMeme()
    }

    private fun loadMeme() {
        binding.progressBar.visibility = View.VISIBLE
//        progressBar.isVisible=true

        val url = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                urlMeme = response.getString("url")

                Glide.with(this).load(urlMeme).listener(
                    object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility = View.GONE
//                            progressBar.isVisible = false
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.progressBar.visibility = View.GONE
                            return false
                        }
                    }).into(binding.imageView)
            },

            {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    this, "Something went wrong.", Toast.LENGTH_SHORT
                ).show()
            }
        )

//        Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun nextMeme(view: View) = loadMeme()

    //    This is the way to share the link not the image itself.
    fun shareMeme(view: View) {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, urlMeme)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, "Share using")
        startActivity(shareIntent)
    }
}