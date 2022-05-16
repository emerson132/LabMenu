package com.example.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.StrictMode
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

lateinit var drawerLayout : DrawerLayout

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)

        var toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()

        var navigationView = findViewById<NavigationView>(R.id.navigation_view)

        navigationView.setNavigationItemSelectedListener (this)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val fragmet: Fragment
        var title: Int
        when(item.itemId){
            R.id.nav_gallery ->{
                fragmet = GaleriaFragment.newInstance(getString(R.string.menu_gallery))
                title = R.string.menu_gallery
            }
            R.id.nav_camera ->{
                fragmet = CamaraFragment.newInstance(getString(R.string.menu_camera))
                title = R.string.menu_camera

            } else ->{
            fragmet = HomeFragment.newInstance("Home")
            title = R.string.menu_gallery
            }
        }

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.home_content, fragmet)
            .commit()
        setTitle(getString((title)))
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



}