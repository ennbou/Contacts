package com.ennbou.contact;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.constraintlayout.motion.widget.TransitionAdapter;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ennbou.contact.data.Contact;
import com.ennbou.contact.data.ContactAdapter;
import com.ennbou.contact.data.ContactVMFactory;
import com.ennbou.contact.data.ContactViewModel;
import com.ennbou.contact.ui.HeadContact;
import com.ennbou.contact.ui.ItemClickSupport;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class ListFragment extends Fragment implements MainActivity.IOnBackPressed {


    private RecyclerView list;
    private ContactAdapter adapter;
    private ContactViewModel viewModel;

    private MotionLayout layout;
    private ImageView btnCancel;
    private TextInputEditText firstName, lastName, phone;

    private MainActivity dis;

    private NavController navController;
    private HeadContact headerLetter = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        ((Toolbar) view.findViewById(R.id.toolbar)).setTitle(getActivity().getResources().getString(R.string.app_name));
        list = view.findViewById(R.id.list);
        layout = view.findViewById(R.id.layout);
        ImageView btnAdd = view.findViewById(R.id.add_icon);
        ImageView btnSave = view.findViewById(R.id.save_icon);
        btnCancel = view.findViewById(R.id.close_icon);
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        phone = view.findViewById(R.id.phone);
        MaterialButton search = view.findViewById(R.id.search);

        dis = (MainActivity) getActivity();

        adapter = new ContactAdapter(dis);
        list.setLayoutManager(new LinearLayoutManager(dis));
        list.setAdapter(adapter);

        String titleDialog = getContext().getResources().getString(R.string.remove_contact);
        String messageDialog = getContext().getResources().getString(R.string.remove_confirmation);
        String cancel = getContext().getResources().getString(R.string.cancel);
        String remove = getContext().getResources().getString(R.string.remove);

        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(getContext());
        dialog.setTitle(titleDialog);


        ItemClickSupport.addTo(list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Contact contact = ((ContactAdapter) recyclerView.getAdapter()).getItem(position);
                viewModel.setContact(contact);
                navController.navigate(R.id.action_ListFragment_to_DetailFragment);
            }
        });

        ItemClickSupport.addTo(list).setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClicked(RecyclerView recyclerView, int position, View v) {

                Contact contact = ((ContactAdapter) recyclerView.getAdapter()).getItem(position);

                dialog.setMessage(messageDialog + " \n " + contact.getFirstName())
                        .setPositiveButton(remove, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                viewModel.deleteContact(contact);
                                dialog.dismiss();
                            }
                        }).setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

                return true;
            }
        });


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_ListFragment_to_SearchFragment);
            }
        });

        viewModel = new ViewModelProvider(dis, new ContactVMFactory(dis.getApplication())).get(ContactViewModel.class);

        viewModel.getAllContacts().observe(dis, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                adapter.setContact(contacts);
                if (headerLetter != null) {
                    list.removeItemDecoration(headerLetter);
                }
                headerLetter = new HeadContact(contacts, getContext());
                int size = contacts.size();
                if (size > 0)
                    list.addItemDecoration(headerLetter);
                search.setText(size + " contact");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setTransition(R.id.set1, R.id.set2);
                layout.transitionToState(R.id.set2);

                layout.setTransitionListener(new TransitionAdapter() {
                    @Override
                    public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                        layout.transitionToState(R.id.set3);
                        layout.setTransitionListener(new TransitionAdapter() {
                            @Override
                            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                                layout.transitionToState(R.id.set4);
                                layout.setTransitionListener(null);
                                hideSoftKeyboard();
                            }
                        });
                    }
                });

            }
        });

        btnAdd.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                viewModel.setContact(new Contact());
                navController.navigate(R.id.action_ListFragment_to_EditFragment);
                return true;
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                firstName.setText("");
                lastName.setText("");
                phone.setText("");
                layout.transitionToStart();
                layout.setTransitionListener(new TransitionAdapter() {
                    @Override
                    public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                        layout.setTransition(R.id.set2, R.id.set3);
                        layout.setProgress(0.8f);
                        layout.transitionToStart();
                        layout.setTransitionListener(new TransitionAdapter() {
                            @Override
                            public void onTransitionCompleted(MotionLayout motionLayout, int currentId) {
                                layout.setTransition(R.id.set1, R.id.set2);
                                layout.setProgress(1f);
                                layout.transitionToStart();
                                layout.setTransitionListener(null);
                            }
                        });
                    }
                });


            }
        });


        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fN = firstName.getText().toString();
                String lN = lastName.getText().toString();
                String p = phone.getText().toString();
                if (fN.isEmpty() || p.isEmpty()) {
                    // TODO set error message
                } else {
                    Contact c = new Contact();
                    c.setFirstName(fN);
                    c.setLastName(lN);
                    c.setPhoneNumber(p);
                    viewModel.insertContact(c);
                    btnCancel.performClick();
                }
            }
        });

    }


    private void hideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) dis.getSystemService(INPUT_METHOD_SERVICE);
        View view = dis.getCurrentFocus();
        if (view == null) {
            view = new View(dis);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        dis.getWindow().getDecorView().clearFocus();

    }

    @Override
    public void onStart() {
        super.onStart();
        hideSoftKeyboard();
    }


    @Override
    public boolean onBackPressed() {
        return true;
    }
}
