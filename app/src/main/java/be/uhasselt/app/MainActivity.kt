package be.uhasselt.app

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.findNavController
import be.uhasselt.app.databinding.ActivityMainBinding
import be.uhasselt.app.fragments.MainMenuFragment
import be.uhasselt.app.net.Appwrite
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityMainBinding
    private lateinit var appwrite: Appwrite
    private var name = "anonymous session"

    init {
        instance = this
    }

    companion object {
        private var instance: MainActivity? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        appwrite = Appwrite
        account()
        setupMenuDrawer()
    }

    private fun setupMenuDrawer() {
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close)
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val headerView = navigationView.getHeaderView(0)

        headerView.findViewById<TextView>(R.id.text_view_header_title).text = "Welcome"
        headerView.findViewById<TextView>(R.id.text_view_header_text).text = name

        val navigationController =
            binding.fragmentContainerView.getFragment<MainMenuFragment>().findNavController()
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> navigationController.navigate(R.id.main_fragment)
                R.id.nav_rockets -> navigationController.navigate(R.id.rocket_list_fragment)
                R.id.nav_login -> navigationController.navigate(R.id.login_fragment)
                R.id.nav_register -> navigationController.navigate(R.id.register_fragment)
            }
            close()
            true
        }
    }

    private fun close() {
        binding.drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        account() // get account and display name when opening navigation drawer
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    fun checkInternet(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    private fun account() {
        appwrite.getAccount { isSuccess, errorMessage, user ->
            if (isSuccess) {
                name = user!!.name
            } else {
                name = "anonymous session"
                println(errorMessage)
            }

            val navigationView = findViewById<NavigationView>(R.id.navigation_view)
            val headerView = navigationView.getHeaderView(0)
            headerView.findViewById<TextView>(R.id.text_view_header_text).text = user!!.name
        }
    }
}
