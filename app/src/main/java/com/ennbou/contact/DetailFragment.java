package com.ennbou.contact;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.ennbou.contact.data.Contact;
import com.ennbou.contact.data.ContactVMFactory;
import com.ennbou.contact.data.ContactViewModel;

public class DetailFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewCompat.setTranslationZ(getView(), 100.f);
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ImageView call = view.findViewById(R.id.call);
        ImageView message = view.findViewById(R.id.message);
        TextView edit = view.findViewById(R.id.edit);
        TextView phone = view.findViewById(R.id.phone);
        TextView job = view.findViewById(R.id.job);
        TextView email = view.findViewById(R.id.email);
        TextView note = view.findViewById(R.id.note);


        ContactViewModel vm = new ViewModelProvider(getActivity(), new ContactVMFactory(getActivity().getApplication())).get(ContactViewModel.class);

        vm.getContact().observe(getActivity(), new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                toolbar.setTitle(contact.getFullName());
                phone.setText(contact.getPhoneNumber());
                email.setText(contact.getEmail());
                job.setText(contact.getJob());
                note.setText(contact.getNote());
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.action_DetailFragment_to_EditFragment);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.getText().toString()));
                startActivity(intent);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:" + phone.getText().toString()));
                startActivity(sendIntent);
            }
        });

    }


}
