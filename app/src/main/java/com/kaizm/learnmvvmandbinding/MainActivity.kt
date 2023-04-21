package com.kaizm.learnmvvmandbinding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import com.kaizm.learnmvvmandbinding.databinding.ActivityMainBinding
import com.kaizm.learnmvvmandbinding.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)

        //https://stackoverflow.com/questions/58262507/how-to-manage-bottomnavigationview-backstack-in-android-navigation-component
        //https://github.com/public-apis/public-apis

    }
}

