package com.example.sharedprefs;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUI(findViewById(R.id.main_parent));

        Spinner spinner = findViewById(R.id.spinner_day);
        EditText editText = findViewById(R.id.editText);
        Button buttonAdd = findViewById(R.id.buttonAddTo);
        Button buttonDisplay = findViewById(R.id.buttonShowPrefs);

        SharedPreferences sharedPreferences = getSharedPreferences("SharedPrefs", MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = sharedPreferences.edit();

        //Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.day_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        final String[] selectedDay = new String[1];
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Integer selectedPosition = spinner.getSelectedItemPosition();
                selectedDay[0] = spinner.getSelectedItem().toString();
                Log.d("MyTag", selectedDay[0] + " selected");
                Toast.makeText(MainActivity.this, selectedDay[0] + " selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Add Button
        buttonAdd.setOnClickListener(new View.OnClickListener() {

            //Alert Dialog
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

            @Override
            public void onClick(View v) {
                String enteredText = editText.getText().toString();
                if (TextUtils.isEmpty(enteredText)){
                    editText.setError("Required");
                    return;
                }
                builder.setTitle("Alert!");
                builder.setMessage("You sure, you wanna add to Shared Preferences");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", (dialog, which) -> {
                    prefsEditor.putString("selectedDay", selectedDay[0]);
                    prefsEditor.putString("enteredText", enteredText);
                    prefsEditor.putString("textView", "Shared Preferences");
                    prefsEditor.apply();
                    Toast.makeText(MainActivity.this, "Added to Shared Preferences", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("No", (dialog, which) -> dialog.cancel());
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        //Display Button
        buttonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(), 0
            );
        }
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(MainActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }
}

