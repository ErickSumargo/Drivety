package com.example.drivety.utils

import android.view.View
import androidx.navigation.Navigation
import com.example.drivety.R

object Navigation {

    fun navigateToSeatbeltScreen(): View.OnClickListener {
        return Navigation.createNavigateOnClickListener(R.id.navigate_seatbelt_screen, null)
    }

    fun navigateToCrimeScreen(): View.OnClickListener {
        return Navigation.createNavigateOnClickListener(R.id.navigate_crime_screen, null)
    }

    fun navigateToGasLeakScreen(): View.OnClickListener {
        return Navigation.createNavigateOnClickListener(R.id.navigate_gas_leak_screen, null)
    }

    fun navigateToCarTheftScreen(): View.OnClickListener {
        return Navigation.createNavigateOnClickListener(R.id.navigate_car_theft_screen, null)
    }
}
