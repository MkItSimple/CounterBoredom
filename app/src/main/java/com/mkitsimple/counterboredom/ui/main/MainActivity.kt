package com.mkitsimple.counterboredom.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.mkitsimple.counterboredom.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.custom_toolbar_latestchats.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar.setTitle("")
        setSupportActionBar(toolbar)

        val latestChatsFragment = LatestChatsFragment()
        val friendsFragment = FriendsListFragment()
        val profileFragment = ProfileFragment()

        tab_layout.setupWithViewPager(view_pager)

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 0)
        viewPagerAdapter.addFragment(latestChatsFragment, "Latest Chats")
        viewPagerAdapter.addFragment(friendsFragment, "Friends List")
        viewPagerAdapter.addFragment(profileFragment, "Profile")
        view_pager.setAdapter(viewPagerAdapter)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_logout) {
            Toast.makeText(applicationContext, "You clicked Page 1", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(applicationContext, "You clicked Page 2", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    private class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        private val fragments: MutableList<Fragment> = ArrayList()
        private val fragmentTitle: MutableList<String> = ArrayList()
        fun addFragment(fragment: Fragment, title: String) {
            fragments.add(fragment)
            fragmentTitle.add(title)
        }

        override fun getItem(position: Int): Fragment {
            return fragments[position]
        }

        override fun getCount(): Int {
            return fragments.size
        }

        @Nullable
        override fun getPageTitle(position: Int): CharSequence? {
            return fragmentTitle[position]
        }
    }
}
