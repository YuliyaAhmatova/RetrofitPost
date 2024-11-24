package com.example.retrofitpost

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.retrofitpost.utils.RetrofitInstance
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var toolbarMain: Toolbar
    private lateinit var downloadBTN: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var imageViewIV: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        init()

        setSupportActionBar(toolbarMain)
        title = "Загрузчик картинок"
        progressBar.visibility = View.GONE
    }

    fun download(view: View) {
        progressBar.visibility = View.VISIBLE
        getRandomDog()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun getRandomDog() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getRandomDog()
            } catch (e: IOException) {
                Toast.makeText(
                    applicationContext,
                    "app error ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                return@launch
            } catch (e: HttpException) {
                Toast.makeText(
                    applicationContext,
                    "http error ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
                return@launch
            }
            if (response.url.isNotEmpty()) {
                withContext(Dispatchers.Main) {
                    Glide.with(this@MainActivity)
                        .load(response.url)
                        .into(imageViewIV)
                    progressBar.visibility = View.GONE
                }
            }
        }
    }

    private fun init() {
        toolbarMain = findViewById(R.id.toolbarMain)
        downloadBTN = findViewById(R.id.downloadBTN)
        progressBar = findViewById(R.id.progressBar)
        imageViewIV = findViewById(R.id.imageViewIV)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.exitMenuMain -> {
                finishAffinity()
                Toast.makeText(
                    applicationContext,
                    "Программа завершена",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}