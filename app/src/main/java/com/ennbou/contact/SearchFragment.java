package com.ennbou.contact;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
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
import com.ennbou.contact.ui.ItemClickSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;


public class SearchFragment extends Fragment {

    List<Contact> listContacts;
    private NavController navController;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ViewCompat.setTranslationZ(getView(), 100f);

        ContactViewModel viewModel = new ViewModelProvider(getActivity(), new ContactVMFactory(getActivity().getApplication())).get(ContactViewModel.class);

        TextView empty = view.findViewById(R.id.empty);

        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);

        viewModel.getAllContacts().observe(getActivity(), new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> contacts) {
                listContacts = contacts;
            }
        });

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        SearchView searchView = view.findViewById(R.id.searchView);
        RecyclerView list = view.findViewById(R.id.recyclerView);

        ContactAdapter adapter = new ContactAdapter(getActivity());
        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setAdapter(adapter);

        // make search focused and show keyboard
        searchView.requestFocus();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(searchView, InputMethodManager.SHOW_IMPLICIT);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                onQueryTextChange(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Contact> list = new ArrayList<>();
                if (newText.isEmpty()) {
                    empty.setVisibility(View.VISIBLE);
                    adapter.setContact(list);
                    return true;
                }
                String[] keys = newText.split(" ");
                StringBuffer rgx1 = new StringBuffer();
                StringBuffer rgx2 = new StringBuffer();
                for (String key : keys) {
                    rgx1.append("\\\\*");
                    rgx1.append(key);
                    rgx1.append("\\\\*");
                    rgx2.insert(0, "\\\\*");
                    rgx2.insert(0, key);
                    rgx2.insert(0, "\\\\*");
                }

                Pattern rgxSearch1 = Pattern.compile(rgx1.toString());
                Pattern rgxSearch2 = Pattern.compile(rgx2.toString());

                for (Contact contact : listContacts) {
                    if (rgxSearch1.matcher(contact.getFullName()).find() || rgxSearch2.matcher(contact.getFullName()).find()) {
                        list.add(contact);
                    }
                }
                if (list.size() > 0) {
                    empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                }
                adapter.setContact(list);
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        ItemClickSupport.addTo(list).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                Contact contact = ((ContactAdapter) recyclerView.getAdapter()).getItem(position);
                viewModel.setContact(contact);
                navController.navigate(R.id.action_SearchFragment_to_DetailFragment);
            }
        });

    }


}
