package vinho.andre.android.com.gerenciadorvinho.view

import android.app.SearchManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.info_block.*
import kotlinx.android.synthetic.main.proxy_screen.*
import vinho.andre.android.com.gerenciadorvinho.BuildConfig
import vinho.andre.android.com.gerenciadorvinho.R
import vinho.andre.android.com.gerenciadorvinho.data.sqlite.DBHelper
import vinho.andre.android.com.gerenciadorvinho.domain.User
import vinho.andre.android.com.gerenciadorvinho.domain.Wine
import vinho.andre.android.com.gerenciadorvinho.interfaces.view.MainActivity
import vinho.andre.android.com.gerenciadorvinho.presenter.MainActivityPresenter
import vinho.andre.android.com.gerenciadorvinho.util.SharedPreferencesUtil
import vinho.andre.android.com.gerenciadorvinho.util.function.isJobServiceOn
import vinho.andre.android.com.gerenciadorvinho.util.service.WineSynchronization
import vinho.andre.android.com.gerenciadorvinho.view.adaprers.mainactivity.SearchAdapter
import vinho.andre.android.com.gerenciadorvinho.view.adaprers.mainactivity.WineAdapter
import java.util.*

class MainActivity :
    AppCompatActivity(),
    NavigationView.OnNavigationItemSelectedListener,
    MainActivity {

    private lateinit var searchView: SearchView
    private var alertDialog: AlertDialog? = null
    private var selectedNavigationItemId: Int = 0
    private var adapterSearch: SearchAdapter? = null
    private var adapterFirebaseBase: WineAdapter? = null
    private var presenter = MainActivityPresenter(this)

    private var listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                WineSynchronization.WineBottles -> {
                    updateToolbarSubTitle()
                }
            }
        }

    private fun showInfoBlock(
        status: Boolean
    ) {
        ll_info_container.visibility = if (status) View.VISIBLE else View.GONE
    }

    override fun callLoginActivity() {
        startActivity(
            Intent(
                this,
                LoginActivity::class.java
            )
        )
        finish()
    }

    private fun callWineRegisterActivity() {
        startActivity(
            Intent(
                this,
                WineRegisterActivity::class.java
            )
        )
    }

    override fun callDetailsActivity(
        wine: Wine
    ) {
        startActivity(
            Intent(
                this,
                WineDetailsActivity::class.java
            ).putExtra(
                Wine.ParcelableWine,
                wine
            )
        )
    }

    private fun callService(
        view: View? = null
    ) {
        val info: JobInfo = JobInfo.Builder(
            WineSynchronization.JobId,
            ComponentName(this, WineSynchronization::class.java)
        ).setPersisted(true)
            .setPeriodic(360 * 60 * 1000)
            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
            .setRequiresCharging(false)
            .build()

        val scheduler: JobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        if (BuildConfig.DEBUG) {
            Toast.makeText(
                this,
                String.format(
                    "Job scheduling %s",
                    if (scheduler.schedule(info) != JobScheduler.RESULT_SUCCESS) {
                        "failed"
                    } else {
                        "start"
                    }
                ),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            scheduler.schedule(info)
        }
    }

    private fun cancelService() {
        val scheduler: JobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancelAll()
    }

    fun syncNow(
        view: View? = null
    ) {
        cancelService()
        callService()
        showInfoBlock(false)
    }

    override fun createDialogOfNoWineRegistration(
        title: String,
        menssage: String,
        textNegativeButton: String
    ) {
        if (alertDialog == null) {
            alertDialog = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(menssage)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.add_button)) { _, _ ->
                    callWineRegisterActivity()
                }
                .setNegativeButton(textNegativeButton) { dialog, _ ->
                    dialog.cancel()
                }.create()
        }


        if (alertDialog != null && !alertDialog!!.isShowing) {
            alertDialog!!.show()
        }

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
            else -> super.onBackPressed()
        }
    }

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        selectedNavigationItemId =
            SharedPreferencesUtil(
                this
            ).getAppOpenFilter()

        adapterPreparation()
        showInfoBlock(false)
        initDrawerLayoutAndNavigation()
        floating_action_add.setOnClickListener {
            callWineRegisterActivity()
        }
    }

    override fun onCreateOptionsMenu(
        menu: Menu?
    ): Boolean {
        menuInflater.inflate(
            R.menu.menu_main,
            menu
        )
        researchLogic(menu)
        return true
    }

    override fun onStart() {
        super.onStart()

        if (!isJobServiceOn(this) &&
            !SharedPreferencesUtil(this).getIsNewUser()
        ) {
            callService()
        }

        if (rv.adapter == adapterFirebaseBase) {
            adapterFirebaseBase?.startListening()
        } else {
            adapterSearch?.updateList(
                DBHelper(this).search(
                    searchView.query.toString()
                )
            )
        }

        val sp = SharedPreferencesUtil(this)

        sp.getPreference()?.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onResume() {
        super.onResume()
        updateToolbarSubTitle()
        presenter.getUserInformation()
    }

    override fun onStop() {
        super.onStop()
        adapterFirebaseBase?.stopListening()
    }

    override fun onDestroy() {
        super.onDestroy()
        SharedPreferencesUtil(
            this
        ).getPreference()?.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onNavigationItemSelected(
        item: MenuItem
    ): Boolean {
        when (item.itemId) {
            R.id.nav_settings -> {
                presenter.getUserInformation()
                startActivity(
                    Intent(
                        this,
                        SettingsActivity::class.java
                    ).putExtra(
                        User.KEY,
                        presenter.getUser()
                    )
                )
            }
            R.id.nav_privacy_policy -> {
                startActivity(
                    Intent(
                        this,
                        PrivacyPolicyActivity::class.java
                    )
                )
            }
            R.id.nav_sign_out -> {
                cancelService()
                SharedPreferencesUtil(
                    this
                ).resetSharedPreference()
                presenter.signOut(this)
            }
            else -> {
                selectedNavigationItemId = item.itemId
                adapterPreparation()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)

        if (item.itemId == R.id.nav_in_the_cellar) {
            setTextInfoContainer(
                String.format(
                    "%s %s",
                    getString(
                        R.string.alert_information_sync_container
                    ),
                    SharedPreferencesUtil(
                        this
                    ).getAShared(WineSynchronization.KeyDateSync)
                )
            )

            Handler().postDelayed(
                {
                    showInfoBlock(false)
                }, 10 * 1000
            )
        }
        return true
    }

    private fun researchLogic(
        menu: Menu?
    ) {
        val searchManager: SearchManager = getSystemService(
            Context.SEARCH_SERVICE
        ) as SearchManager

        searchView = menu?.findItem(
            R.id.action_search
        )?.actionView as SearchView

        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(componentName)
        )

        val dbHelper = DBHelper(this)

        adapterSearch = SearchAdapter(
            this,
            mutableListOf(),
            this
        )

        searchView.addOnAttachStateChangeListener(
            object : View.OnAttachStateChangeListener {
                // search was detached/closed
                override fun onViewDetachedFromWindow(
                    p0: View?
                ) {
                    rv.adapter = adapterFirebaseBase
                    adapterFirebaseBase?.startListening()
                    if (ll_info_container.visibility == View.VISIBLE) {
                        showInfoBlock(false)
                    }
                }

                // search was opened
                override fun onViewAttachedToWindow(
                    p0: View?
                ) {
                    rv.adapter = adapterSearch
                    adapterFirebaseBase?.stopListening()

                    setTextInfoContainer(
                        String.format(
                            "%s %s %s",
                            getString(
                                R.string.alert_information_search_container
                            ),
                            getString(
                                R.string.alert_information_sync_container
                            ).toLowerCase(Locale.getDefault()),
                            SharedPreferencesUtil(
                                applicationContext
                            ).getAShared(WineSynchronization.KeyDateSync)
                        )
                    )
                }
            }
        )

        searchView.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(
                    query: String
                ): Boolean {
                    adapterSearch!!.updateList(
                        if (query.isNotEmpty()) {
                            dbHelper.search(
                                query.trim()
                            )
                        } else {
                            emptyList()
                        }
                    )
                    return false
                }

                override fun onQueryTextChange(
                    query: String
                ): Boolean {
                    if (ll_info_container.visibility == View.VISIBLE) {
                        showInfoBlock(false)
                    }

                    adapterSearch!!.updateList(
                        if (query.isNotEmpty()) {
                            dbHelper.search(
                                query.trim()
                            )
                        } else {
                            emptyList()
                        }
                    )
                    return false
                }
            }
        )
    }

    override fun showProxy(
        status: Boolean
    ) {
        fl_proxy_container.visibility =
            if (status)
                View.VISIBLE
            else
                View.GONE
    }

    override fun getContext(): Context = this

    private fun adapterPreparation() {
        if (nav_view.checkedItem?.itemId != selectedNavigationItemId) {
            showProxy(true)
            adapterFirebaseBase =
                WineAdapter(
                    presenter
                        .getFirestoreRecyclerOptions(
                            selectedNavigationItemId
                        ),
                    this
                )

            updateToolbarSubTitle()

            rv.adapter = adapterFirebaseBase
            adapterFirebaseBase?.startListening()
        }
    }

    private fun initDrawerLayoutAndNavigation() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar, R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        presenter.getUserInformation()

        nav_view.setCheckedItem(
            SharedPreferencesUtil(
                this
            ).getAppOpenFilter()
        )
    }

    override fun setUserInformationInNavigation(
        user: User
    ) {
        val navView: View =
            nav_view.getHeaderView(0)

        val textViewNameUser: TextView =
            navView.findViewById(R.id.tv_name)

        val textViewEmailUser: TextView =
            navView.findViewById(R.id.tv_email)

        textViewEmailUser.text = user.email
        textViewNameUser.text = user.name.toString()
    }

    override fun updateToolbarSubTitle() {
        var subTitle = ""
        when (selectedNavigationItemId) {
            R.id.nav_in_the_cellar -> {
                subTitle = getString(R.string.menu_item_in_the_cellar)
            }
            R.id.nav_rating -> {
                subTitle = getString(R.string.menu_item_rating)
            }
            R.id.nav_bookmark -> {
                subTitle = getString(R.string.menu_item_bookmark)
            }
            R.id.nav_name -> {
                subTitle = getString(R.string.menu_item_name)
            }
            R.id.nav_country_of_origin -> {
                subTitle = getString(R.string.menu_item_country_of_origin)
            }
        }

        val sp = SharedPreferencesUtil(this)
        sp.saveShared(
            WineSynchronization.WineBottles,
            DBHelper(
                this
            ).getSumVintage().toString()
        )

        toolbar.subtitle =
            when {
                adapterFirebaseBase?.itemCount == null -> {
                    "$subTitle (0)"
                }
                subTitle.contains(getString(R.string.menu_item_in_the_cellar)) -> {
                    "$subTitle (" +
                            sp.getAShared(
                                WineSynchronization.WineBottles
                            ) +
                            "/${adapterFirebaseBase?.itemCount})"
                }
                else -> {
                    "$subTitle (${adapterFirebaseBase?.itemCount})"
                }
            }


    }

    private fun setTextInfoContainer(
        text: String
    ) {
        Handler().postDelayed({
            tv_info_sync.visibility = View.VISIBLE
            showInfoBlock(true)

            tv_info_block.text = text
        }, 500)
    }
}