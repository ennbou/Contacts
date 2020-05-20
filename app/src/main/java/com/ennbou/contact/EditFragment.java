package com.ennbou.contact;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;


import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.ennbou.contact.data.Contact;
import com.ennbou.contact.data.ContactVMFactory;
import com.ennbou.contact.data.ContactViewModel;

import com.google.android.material.textfield.TextInputEditText;

import java.util.Optional;


public class EditFragment extends Fragment {

    private Contact contact = new Contact();
    private String title;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        TextInputEditText firstName = view.findViewById(R.id.first_name);
        TextInputEditText lastName = view.findViewById(R.id.last_name);
        TextInputEditText phone = view.findViewById(R.id.phone);
        TextInputEditText email = view.findViewById(R.id.email);
        TextInputEditText job = view.findViewById(R.id.job);
        TextInputEditText note = view.findViewById(R.id.note);

        TextView save = view.findViewById(R.id.save);
        title = getContext().getResources().getString(R.string.add_new_contact);
        toolbar.setTitle(title);

        ContactViewModel vm = new ViewModelProvider(getActivity(), new ContactVMFactory(getActivity().getApplication())).get(ContactViewModel.class);

        vm.getContact().observe(getActivity(), new Observer<Contact>() {
            @Override
            public void onChanged(Contact contact) {
                EditFragment.this.contact = contact;
                title = (contact.getId() == null) ? title : contact.getFullName();
                toolbar.setTitle(title);
                firstName.setText(contact.getFirstName());
                lastName.setText(contact.getLastName());
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cFirstName, cLastName, cPhone, cJob, cEmail, cNote;

                cFirstName = firstName.getText().toString();
                cPhone = phone.getText().toString();

                if (cFirstName.isEmpty() || cPhone.isEmpty()) {
                    Toast.makeText(getContext(),
                            getActivity().getResources().getString(R.string.msg_form_validation), Toast.LENGTH_SHORT).show();
                    return;
                }

                cLastName = lastName.getText().toString();
                cJob = job.getText().toString();
                cEmail = email.getText().toString();
                cNote = note.getText().toString();


                contact.setFirstName(cFirstName);
                contact.setLastName(cLastName);
                contact.setPhoneNumber(cPhone);
                contact.setEmail(cEmail);
                contact.setJob(cJob);
                contact.setNote(cNote);


                if (contact.getId() != null) {
                    vm.saveContact(contact);
                } else {
                    vm.insertContact(contact);
                }
                getActivity().onBackPressed();
            }
        });

    }


}
