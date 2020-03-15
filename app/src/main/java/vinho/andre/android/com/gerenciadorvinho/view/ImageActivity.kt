package vinho.andre.android.com.gerenciadorvinho.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_image.*
import vinho.andre.android.com.gerenciadorvinho.R

class ImageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
    }

    override fun onStart() {
        super.onStart()
        Picasso
            .get()
            .load(
                intent?.extras!!.getString("image")
            )
            .resize(
                480, 854
            )
            .into(iv_photo)
    }
}