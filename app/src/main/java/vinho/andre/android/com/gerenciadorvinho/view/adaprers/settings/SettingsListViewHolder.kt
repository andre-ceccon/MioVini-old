package vinho.andre.android.com.gerenciadorvinho.view.adaprers.settings

import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.SettingItem
import vinho.andre.android.com.gerenciadorvinho.domain.User
import vinho.andre.android.com.gerenciadorvinho.view.SettingsActivity

class SettingsListViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView),
    View.OnClickListener {

    private lateinit var item: SettingItem

    private val tvLabel: TextView =
        itemView.findViewById(R.id.tv_label)

    private val tvDescription: TextView =
        itemView.findViewById(R.id.tv_description)

    fun setData(
        item: SettingItem
    ) {
        this.item = item

        tvLabel.text = item.label
        tvDescription.text = item.description
        itemView.setOnClickListener(this)
    }

    override fun onClick(
        view: View
    ) {
        val activity = view.context as SettingsActivity
        val user = activity.getUser()

        if (item.label == activity.getString(R.string.setting_item_login) && user.provaider != "password") {
            Toast.makeText(
                activity.applicationContext,
                view.context.getString(R.string.wrong_provider_to_change_password),
                Toast.LENGTH_LONG
            ).show()
        } else {
            activity.startActivityForResult(
                Intent(
                    activity,
                    item.activityClass
                ).putExtra(
                    User.KEY,
                    user
                ),
                SettingsActivity.idProfileActivity
            )
        }
    }
}