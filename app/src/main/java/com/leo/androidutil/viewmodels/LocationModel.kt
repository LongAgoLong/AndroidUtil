package com.leo.androidutil.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LocationModel : ViewModel() {
    var mUpdateTime: MutableLiveData<String> = MutableLiveData()

}