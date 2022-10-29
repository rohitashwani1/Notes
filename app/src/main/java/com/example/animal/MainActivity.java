package com.example.animal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {
    private EditText msg;
    private Button add,clear;
    private String a;
    private ListView list_1;
    private Vector<String> idi;
    private FirebaseDatabase f;
    private DatabaseReference db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        msg = findViewById(R.id.msg);
        clear = findViewById(R.id.clear);
        add = findViewById(R.id.add);
        list_1 = findViewById(R.id.list);
        f = FirebaseDatabase.getInstance();
        db = f.getReference();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                a = msg.getText().toString();
                msg.setText("");
                HashMap<String,Object> h = new HashMap<>();
                h.put(a,a);
                db.updateChildren(h);
            }
        });
        ArrayList<String> arr = new ArrayList<>();
        ArrayAdapter ad = new ArrayAdapter<String>(this , android.R.layout.simple_list_item_1,arr);
        list_1.setAdapter(ad);

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arr.clear();
                for(DataSnapshot snp : snapshot.getChildren()){
                    arr.add(snp.getValue().toString());
                }
                ad.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error){

            }
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.removeValue();
                Toast.makeText(MainActivity.this, "Cleared successfully !!!", Toast.LENGTH_SHORT).show();
            }
        });
        list_1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                db.child(adapterView.getItemAtPosition(i).toString()).removeValue();
            }
        });
    }
}