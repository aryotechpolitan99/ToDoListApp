package com.aryotech.todolistapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fabAdd;
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

        // 5. Define FAB dan buat onClickListener nya.
        fabAdd = findViewById(R.id.btn_fab);
        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //6. Method di bawah ini dibuat sendiri di bawah
                onClickFabAdd();
            }
        });

        //7. Buat onItemLongclickListener di list view unutk hapus data
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // panggil method deleteItem()
                deleteItem(position);
                return false;
            }
        });
    }

    //1. siapkan data
    private void createTodos(){
        data.add("eat");
        data.add("pray");
        data.add("sport");
        data.add("break");
    }
    //6. Buat Method ketika FAB Add di click untuk menambahkan data
    private void onClickFabAdd(){
        //Cara pertama tambah edit text ke dialog
        //EditText edtTodo = new EditText(this);

        //Cara dua tambah edit text ke dialog
        //proses ini disebut dengan inflate layout
        View view = View.inflate(this,R.layout.dialog_add_view, null);

        //EditText ini dideklarisikan di atas di dalam class
        edtTodo = view.findViewById(R.id.edt_todo);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add your list");
        dialog.setView(view);
        dialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                data.add(edtTodo.getText().toString()); // tambah data ke object ArrayList data.
                arrayAdapter.notifyDataSetChanged(); // merefresh list view
            }
        });
        dialog.setNegativeButton("Cancel",null);
        dialog.create();
        dialog.show();
    }

    // 7. buat method delete item untuk menghapus data dari array list dan mengupdate listviewdan mengupdate listview
    private void deleteItem(int position){// beri paramater position untuk mewadahi position dari listview

       // kontanta untuk menampung data position yang di passing dari item onitemlongclicklistener
        final int index = position;

        /// buat alert dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Anda yakin menghapus item ?");
        dialog.setPositiveButton("ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // hapus item dari array list data berdasarkan index/position dari item di list view
                data.remove(index); // index didapat position paramater

                // suruh adapter unutk notify ke listview kalau data telah berubah, lalu merefresh listview
                arrayAdapter.notifyDataSetChanged();
            }
        });
        dialog.setNegativeButton("Tidak", null);
        dialog.create().show();
    }
}

