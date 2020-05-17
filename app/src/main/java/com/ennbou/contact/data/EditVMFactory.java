package com.ennbou.contact.data;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class EditVMFactory implements ViewModelProvider.Factory {

    private final Application application;

    public EditVMFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(EditContactVM.class)) {
            return (T) new EditContactVM(application);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}