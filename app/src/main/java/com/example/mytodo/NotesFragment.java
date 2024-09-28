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

public class NotesFragment extends Fragment {
    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter<String> notesAdapter;
    private ListView listView;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        listView = view.findViewById(R.id.listView);

        // Load saved notes from SharedPreferences
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.mytodo", Context.MODE_PRIVATE);
        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("notes", null);

        if (set == null) {  // if the set is empty no user input yet
            notes.add("Example note");
        } else {
            notes = new ArrayList(set);  // display the user input
        }  // end if

        notesAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, notes);
        listView.setAdapter(notesAdapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Add Note", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), NoteEditor.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                Intent intent = new Intent(getContext(), NoteEditor.class);
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
                                notes.remove(itemToDelete);
                                notesAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getContext().getSharedPreferences("com.example.mytodo", Context.MODE_PRIVATE);
                                HashSet<String> set = new HashSet<>(notes);
                                sharedPreferences.edit().putStringSet("notes", set).apply();
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
