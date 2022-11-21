package com.example.calling.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UserViewModel : ViewModel() {
    //Get latest trailer
    var latestTrailerMutableLiveData = MutableLiveData<UserViewModel>()
    /*public void getLatestTrailer(String page) {
        Client.getINSTANCE().getLatestTrailer(page).enqueue(new Callback<UserViewModel>() {
            @Override
            public void onResponse(Call<UserViewModel> call, Response<UserViewModel> response) {
                latestTrailerMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<UserViewModel> call, Throwable t) {
                Log.d("e2", t.getMessage());
            }
        });
    }*/
}