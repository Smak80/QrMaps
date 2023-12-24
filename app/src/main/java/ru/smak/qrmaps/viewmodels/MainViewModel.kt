package ru.smak.qrmaps.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.smak.qrmaps.ui.navigation.Navigation

class MainViewModel : ViewModel() {
    var currentPage: Navigation by mutableStateOf(Navigation.LIST)

}