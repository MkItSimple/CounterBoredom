package com.mkitsimple.counterboredom.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.mkitsimple.counterboredom.R
import com.mkitsimple.counterboredom.data.models.User
import com.mkitsimple.counterboredom.ui.auth.RegisterActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.intentFor

class MainActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        toolbar.setTitle("")
        setSupportActionBar(toolbar)

        val latestChatsFragment = LatestChatsFragment()
        val friendsFragment = FriendsListFragment()
        val profileFragment = ProfileFragment()

        tab_layout.setupWithViewPager(view_pager)

        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager, 1)
        viewPagerAdapter.addFragment(latestChatsFragment, "LATEST CHATS")
        viewPagerAdapter.addFragment(friendsFragment, "FRIENDS LIST")
        //viewPagerAdapter.addFragment(profileFragment, "PROFILE")
        view_pager.setAdapter(viewPagerAdapter)
        //view_pager.setCurrentItem(currentFragment!!, true)

        circleImageViewMain.setOnClickListener {
            startActivity(intentFor<ProfileActivity>())
        }

        verifyUserIsLoggedIn()
        fetchCurrentUser()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_logout) {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        if (id == R.id.menu_account) {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
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

    private fun verifyUserIsLoggedIn() {
        val uid = viewModel.uid()

        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun fetchCurrentUser() {
        viewModel.fetchCurrentUser()
        viewModel.currentUser.observe(this, Observer { cUser ->
            currentUser = cUser
            //Log.d(TAG, "CurrentUser Name: "+ currentUser?.username)
            if (currentUser?.profileImageUrl != "null") {
                Picasso.get().load(currentUser?.profileImageUrl).into(circleImageViewMain)
            }

        })
    }

    override fun onResume() {
        super.onResume()
        fetchCurrentUser()
        //toast("Resumed")
    }
}
