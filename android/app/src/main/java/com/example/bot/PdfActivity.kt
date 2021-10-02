package com.example.bot

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.bot.databinding.ActivityPdfBinding
import java.io.IOException
import java.net.URL


class PdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPdfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(supportActionBar != null)
            this.supportActionBar?.hide();
    }

    override fun onStart() {
        super.onStart()

        val intent = intent
        var link = ""
        if (intent.hasExtra("link"))
            link = intent.getStringExtra("link").toString()

        val pdfFileApi = resources.getString(R.string.pdf_file)
        val pdfLinkApi = resources.getString(R.string.pdf_link)

        PdfLoader(link, pdfFileApi, pdfLinkApi, binding).execute()
    }

    private class PdfLoader(
        val link: String,
        val fileApi: String,
        val linkApi: String,
        val binding: ActivityPdfBinding
    ): AsyncTask<Void?, Void?, Void?>() {
        @SuppressLint("StaticFieldLeak", "SetTextI18n")
        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                val url = URL("$fileApi$link.pdf").openStream()
                binding.pdfView.fromStream(url).load()
            } catch (e: IOException) {
                e.printStackTrace()
                binding.pdfView.isVisible = false
                binding.pdfLink.isVisible = true
                binding.pdfLink.text = "$linkApi$link"
            }
            return null
        }

    }


}
