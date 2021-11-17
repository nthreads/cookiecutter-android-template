package com.csms.base.utils.extensions

import androidx.navigation.NavController


/**
 * Package Name : com.csms.base.utils.extensions
 * Created by Asad Ur Rehman on 6/23/21 - 9:55 AM.
 * Copyright (c) 2021 Creativity Smart Media Solutions. All rights reserved.
 */

fun NavController.isFragmentRemovedFromBackStack(destinationId: Int) =
    try {
        getBackStackEntry(destinationId)
        false
    } catch (e: Exception) {
        true
    }