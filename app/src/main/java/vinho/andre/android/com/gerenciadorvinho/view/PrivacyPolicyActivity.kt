package vinho.andre.android.com.gerenciadorvinho.view

import android.os.Bundle
import android.view.MenuItem
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.content_privacy_policy.*
import vinho.andre.android.com.gerenciadorvinho.R

class PrivacyPolicyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_privacy_policy)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        webview_privacy.webViewClient =
            object : WebViewClient() {
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    url: String?
                ): Boolean {
                    view?.loadUrl(url)
                    return true
                }
            }
    }

    override fun onOptionsItemSelected(
        item: MenuItem
    ): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        webview_privacy.loadUrl(
            "https://docs.google.com/document/d/13AShVB-17J2D_UblqMCEWrXKJNK9Gld3P5fz-Q8WQ8k/edit?usp=sharing"
        )
    }
}