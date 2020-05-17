package com.ennbou.contact;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.ennbou.contact.data.ContactVMFactory;
import com.ennbou.contact.data.ContactViewModel;

public class MainActivity extends AppCompatActivity {

    private ContactViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = new ViewModelProvider(this, new ContactVMFactory(this.getApplication())).get(ContactViewModel.class);
    }

    @Override public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.ListFragment);
        if (!(fragment instanceof IOnBackPressed) || !((IOnBackPressed) fragment).onBackPressed()) {
            super.onBackPressed();
        }
    }

    public interface IOnBackPressed {
        boolean onBackPressed();
    }

}


