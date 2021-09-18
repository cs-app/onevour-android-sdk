package org.cise.sdk.ciseapp.modules.mvvm.viewmodels;

import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import org.cise.core.utilities.commons.ValueOf;
import org.cise.sdk.ciseapp.modules.mvvm.models.LoginUser;
import org.cise.sdk.ciseapp.modules.mvvm.models.UserDTO;

public class LoginViewModel extends ViewModel {

    public MutableLiveData<UserDTO> user = new MutableLiveData<>();

    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<LoginUser> userMutableLiveData = new MutableLiveData<>();

    public MutableLiveData<LoginUser> getUser() {
        return userMutableLiveData;
    }

    public void onClick(View view) {
        LoginUser loginUser = new LoginUser(user.getValue());
        userMutableLiveData.setValue(loginUser);
        if (ValueOf.isNull(isLoading.getValue())) {
            isLoading.setValue(true);
        } else {
            isLoading.setValue(!isLoading.getValue());
        }
//        Log.d("MVVMLogin", "update login user " + user.getValue().getUsername() + " and loading " + isLoading.getValue());
    }

    public void init() {
        if (ValueOf.isNull(user.getValue())) {
            user.setValue(new UserDTO("ZULIADIN", "123456"));
            LoginUser loginUser = new LoginUser(user.getValue());
            userMutableLiveData.setValue(loginUser);
        }
    }
}
