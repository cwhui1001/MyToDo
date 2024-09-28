package com.example.mytodo;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;

public class EventsFragment extends Fragment {
    static ArrayList<String> eventsNotes = new ArrayList<>();
    static ArrayAdapter<String> eventsAdapter;
    private ListView listView;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        listView = view.findViewById(R.id.listView);

        // Load saved notes from SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.mytodo", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("eventsNotes", null);

        if (set == null) {  // if the set is empty no user input yet
            eventsNotes.add("Example note");
        } else {
            eventsNotes = new ArrayList(set);  // display the user input
        }  // end if

        eventsAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, eventsNotes);
        listView.setAdapter(eventsAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Add Event", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), EventsEditor.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(getContext(), EventsEditor.class);
                intent.putExtra("noteId",i);
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int itemToDelete = i;
                new AlertDialog.Builder(getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("Do you want to delete this note?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                eventsNotes.remove(itemToDelete);
                                eventsAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.mytodo", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet<>(eventsNotes);
                                sharedPreferences.edit().putStringSet("eventsNotes", set).apply();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
        return view;
    }
}
