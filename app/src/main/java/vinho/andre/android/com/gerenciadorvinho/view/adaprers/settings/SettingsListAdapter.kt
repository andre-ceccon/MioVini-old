package vinho.andre.android.com.gerenciadorvinho.view.adaprers.settings

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.domain.SettingItem
import vinho.andre.android.com.gerenciadorvinho.view.configuration.FilterPreferenceActivity
import vinho.andre.android.com.gerenciadorvinho.view.configuration.ProfileActivity
import vinho.andre.android.com.gerenciadorvinho.view.configuration.connectiondata.ConnectDataActivity

class SettingsListAdapter(
    context: Context
    ) : RecyclerView.Adapter<SettingsListViewHolder>() {

    private val items: List<SettingItem> =
        listOf(
            SettingItem(
                context.getString(R.string.setting_item_profile),
                context.getString(R.string.setting_item_profile_desc),
                ProfileActivity::class.java
            ),
            SettingItem(
                context.getString(R.string.setting_item_login),
                context.getString(R.string.setting_item_login_desc),
                ConnectDataActivity::class.java
            ),
            SettingItem(
                context.getString(R.string.title_group_navigation_drawer_filter),
                context.getString(R.string.default_filter),
                FilterPreferenceActivity::class.java
            )
        )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        type: Int
    ): SettingsListViewHolder {
        return SettingsListViewHolder(
            LayoutInflater
                .from(
                    parent.context
                ).inflate(
                    R.layout.settings_item,
                    parent,
                    false
                )
        )
    }

    override fun onBindViewHolder(
        holder: SettingsListViewHolder,
        position: Int
    ) {
        holder.setData(items[position])
    }

    override fun getItemCount() = items.size
}