package com.example.lab8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lab8.Database.Data;
import com.example.lab8.Entity.Contacto;

public class RegistrarActivity extends AppCompatActivity {

    ImageView add;
    ImageView back;
    EditText mainNombre;
    EditText nombre;
    EditText celular;
    ListView listView;
    ArrayAdapter<Contacto> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        add=findViewById(R.id.add);
        back=findViewById(R.id.back);
        mainNombre=findViewById(R.id.nombre_principal);
        nombre=findViewById(R.id.nombre);
        celular=findViewById(R.id.numero);
        final ListView listView= findViewById(R.id.list_view);
        adapter= new ArrayAdapter<Contacto>(this, android.R.layout.simple_list_item_1, Data.persona.getContactos().toArray(new Contacto[Data.persona.getContactos().size()]));
        listView.setAdapter(adapter);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarContacto()){
                    Contacto contacto= new Contacto(nombre.getText().toString(),celular.getText().toString());
                    boolean flag=false;
                    for(Contacto c : Data.persona.getContactos()){
                        if(c.getNombre().equals(contacto.getNombre()) && c.getTelefono().equals(contacto.getNombre())) {
                            Toast.makeText(RegistrarActivity.this, "Contacto Repetido. Por favor ingrese otro", Toast.LENGTH_SHORT).show();
                            flag=true;
                            break;
                        }
                    }
                    if(!flag) {
                        Data.persona.getContactos().add(contacto);
                        adapter= new ArrayAdapter<Contacto>(RegistrarActivity.this, android.R.layout.simple_list_item_1, Data.persona.getContactos().toArray(new Contacto[Data.persona.getContactos().size()]));
                        listView.setAdapter(adapter);
                        nombre.setText("");
                        celular.setText("");
                    }
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verificarBack()){
                    Data.persona.setName(mainNombre.getText().toString());
                    Intent intent= new Intent(RegistrarActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }
        });

        cargarNombre();
    }

    private boolean verificarContacto(){
        if(this.nombre.getText().toString().isEmpty()){
            Toast.makeText(this,"Por favor ingrese un nombre de contacto",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(this.celular.getText().toString().isEmpty()){

            Toast.makeText(this,"Por favor ingrese un numero de contacto",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }

    private boolean verificarBack(){
        if(this.mainNombre.getText().toString().isEmpty()){
            Toast.makeText(this,"Por favor ingrese su nombre",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Data.persona.getContactos().isEmpty()){
            Toast.makeText(this,"Por favor agregue contactos de emergencia",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        verificarBack();
    }

    private void cargarNombre(){
        if(!Data.persona.getName().isEmpty())
            this.mainNombre.setText(Data.persona.getName());
    }
}
