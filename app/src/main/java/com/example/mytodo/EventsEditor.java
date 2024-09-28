package com.example.mytodo;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashSet;

public class EventsEditor extends AppCompatActivity {
    private int noteId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editor);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editText = findViewById(R.id.editText);

        Intent intent = getIntent();
        noteId = intent.getIntExtra("noteId", -1);


        if (noteId != -1) {  // if note id is not -1, already has item
            // display the contents that you pull out from the file
            editText.setText(EventsFragment.eventsNotes.get(noteId));
        } else {
            EventsFragment.eventsNotes.add("");  // display nothing
            noteId = EventsFragment.eventsNotes.size() - 1; // arraylist item start 0
            EventsFragment.eventsAdapter.notifyDataSetChanged();
            // update arraylist
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (noteId != -1 && noteId < EventsFragment.eventsNotes.size()) {
                    EventsFragment.eventsNotes.set(noteId, charSequence.toString());
                } else {
                    EventsFragment.eventsNotes.add(charSequence.toString());
                    noteId = EventsFragment.eventsNotes.size() - 1;
                }
                EventsFragment.eventsAdapter.notifyDataSetChanged();
                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.mytodo", Context.MODE_PRIVATE);
                // convert the notes array list into hash set
                // because SP cannot read data from notes array list
                // user input --> array list --> hash set--> shared preferences file
                HashSet<String> set = new HashSet(EventsFragment.eventsNotes);
                // open the SP in edit mode
                // use put() method to save data into the SP
                // apply() means save the entire file, apply the changes
                sharedPreferences.edit().putStringSet("eventsNotes", set).apply();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // No action needed
            }
        });
    }
}
