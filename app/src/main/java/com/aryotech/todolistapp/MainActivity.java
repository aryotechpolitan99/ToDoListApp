package com.aryotech.todolistapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    ListView listView;
    EditText edtTodo;

    //1. Siapkan Data
    ArrayList<String> data = new ArrayList<String>();

    //3. Buat Adapter untuk List View
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //1. Siapkan data
        createTodos();

        //2. Buat List View
        listView = findViewById(R.id.lv_list); // define list view

        // 3. Buat Adapter dan masukkan parameter yg dibutuhkan. (context, layout_content,tv,data)
        //      parameter data diambil dari langkah 1.
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.content_list,R.id.tv_todo,data);
        // 4. Set Adapter kepada List View
        listView.setAdapter(arrayAdapter);

    }
    // 1. Siapkan Data
    private void createTodos(){
        data.add("Coding");
        data.add("Eat");
        data.add("Sleep");
        data.add("Traveling");
    }
}
