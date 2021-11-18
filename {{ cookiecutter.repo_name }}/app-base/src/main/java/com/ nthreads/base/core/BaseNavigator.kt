package com.nthreads.base.core

import androidx.navigation.NavController

abstract class BaseNavigator {
    protected var navController: NavController? = null

    fun bind(navController: NavController) {
        this.navController = navController
    }

    fun unbind() {
        navController = null
    }
}