package ir.ryca.fcm.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.core.app.NavUtils

import ir.ryca.fcm.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_message_show.*


class MessageShowActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_show)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //receive data from MyFirebaseMessagingService class
        val title = intent.getStringExtra("title")
        val timeStampString = intent.getStringExtra("timestamp")
        val contentString = intent.getStringExtra("content")
        val companyString = intent.getStringExtra("company")
        val imageUrl = intent.getStringExtra("image")

        //Set data on UI
        Picasso.get()
            .load(imageUrl)
            .placeholder(R.drawable.image_placeholder)
            .error(R.drawable.image_placeholder)
            .into(featureGraphics)

        header.text = title
        timeStamp.text = timeStampString
        content.text = contentString
        company.text = companyString
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId == android.R.id.home)
            onBackPressed()

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val intent = NavUtils.getParentActivityIntent(this)
        startActivity(intent)
    }
}
