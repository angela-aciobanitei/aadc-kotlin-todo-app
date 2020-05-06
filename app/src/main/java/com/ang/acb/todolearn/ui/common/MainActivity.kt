package com.ang.acb.todolearn.ui.common

import android.app.Activity
import android.app.NotificationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.ang.acb.todolearn.R
import com.ang.acb.todolearn.notification.createTasksChannel
import com.google.android.material.navigation.NavigationView
import timber.log.Timber


// https://stackoverflow.com/questions/32013948/will-someone-please-explain-result-first-user
const val ADD_EDIT_RESULT_OK = Activity.RESULT_FIRST_USER + 1
const val DELETE_RESULT_OK = Activity.RESULT_FIRST_USER + 2


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()
        setupNavigationDrawer()
        setupNotificationChannel()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun setupToolbar() {
        val toolbar = findViewById<Toolbar>(R.id.main_toolbar)
        setSupportActionBar(toolbar)
    }

    private fun setupNavigationDrawer() {
        // Set up the drawer with the top app bar
        // https://developer.android.com/guide/navigation/navigation-ui#top_app_bar
        navController = this.findNavController(R.id.nav_host_fragment)
        val drawerLayout = findViewById<DrawerLayout>(R.id.root_drawer_layout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

        // Hook up the drawer to the nav controller
        val navigationView = findViewById<NavigationView>(R.id.drawer_navigation_view)
        NavigationUI.setupWithNavController(navigationView, navController)
    }

    private fun setupNotificationChannel() {
        // If you target Android 8.0 (API level 26) and post a notification without
        // specifying a channel, the notification will not appear and the system will
        // log an error message saying "No channel found ...".
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager
        notificationManager.createTasksChannel(this)

        Timber.d("Notification channel created")
    }
}
