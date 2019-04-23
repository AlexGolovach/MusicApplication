package com.example.android.musicapplication.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import com.example.android.database.Callback
import com.example.android.database.Injector
import com.example.android.database.model.User
import com.example.android.musicapplication.R
import com.example.android.musicapplication.entry.LoginActivity
import com.example.android.musicapplication.playlist.PlaylistActivity
import com.example.android.musicapplication.profile.ProfileActivity
import com.example.android.musicapplication.search.SearchActivity
import com.example.android.musicapplication.topartists.TopArtistsFragment
import com.example.android.musicapplication.toptracks.TopTracksFragment
import com.example.android.musicapplication.utils.TabAdapter
import com.example.android.musicapplication.utils.getAccount
import com.example.android.musicapplication.utils.removeAccountInformation
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer_header.view.*

class MainActivity : AppCompatActivity(), Callback<User> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        initDrawer()
        initViewPager()

        tablayout.setupWithViewPager(viewpager)
    }

    private fun initDrawer() {
        val toggle = ActionBarDrawerToggle(
            this,
            drawer_layout,
            toolbar,
            R.string.nav_drawer_open,
            R.string.nav_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        val header = navigation_view.getHeaderView(0)
        header.header_username.text = getAccount()?.username

        navigation_view.setNavigationItemSelectedListener {

            when (it.itemId) {
                R.id.playlist -> {
                    startActivity(Intent(this, PlaylistActivity::class.java))
                }

                R.id.search -> {
                    startActivity(Intent(this,SearchActivity::class.java))
                }

                R.id.profile -> {
                    startActivity(Intent(this,ProfileActivity::class.java))
                }

                R.id.exit -> {
                    removeAccountInformation()

                    val userRepository = Injector.getUserRepositoryImpl()
                    userRepository.logout(this)

                    drawer_layout.closeDrawer(GravityCompat.START)
                }
            }

            true
        }
    }

    private fun initViewPager() {
        val fragmentPagerAdapter = TabAdapter(supportFragmentManager)

        fragmentPagerAdapter.addFragment(TopArtistsFragment(), "Top Artists")
        fragmentPagerAdapter.addFragment(TopTracksFragment(), "Top Tracks")

        viewpager.adapter = fragmentPagerAdapter
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)

        } else {
            super.onBackPressed()
        }
    }

    override fun onSuccess(result: User) {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
