package com.ascri.failtok.ui.main

import android.os.Bundle
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.ascri.failtok.R
import com.ascri.failtok.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.android.support.DaggerAppCompatActivity

class MainActivity : DaggerAppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        navController.navigate(item.itemId)
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        navController = Navigation.findNavController(this, R.id.nav_host_frag)
        binding.navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            var index = 0
            when (destination.id) {
                R.id.hot_fails_fragment -> index = 0
                R.id.new_fails_fragment -> index = 1
                R.id.top_fails_fragment -> index = 2
            }
            binding.navView.menu[index].isChecked = true
        }
    }
}
