package com.example.ndca

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*


@Suppress("UNUSED_EXPRESSION")
class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //bottom nav clicks
        design_navigation_view.setOnNavigationItemSelectedListener { item ->

            var message = ""

            when (item.itemId) {
                R.id.action_home -> {
                    replaceFragment(HomeFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.action_Diet -> {
                    replaceFragment(DietFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_cam -> {
                    replaceFragment(CameraFragment())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.action_settings -> {
                    replaceFragment(SettingsFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            Toast.makeText(
                this,
                "$message clicked",
                Toast.LENGTH_SHORT
            ).show()

            return@setOnNavigationItemSelectedListener true
        }
        false
    }


    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.rel_layout, fragment)
        fragmentTransaction.commit()

    }


}
