package com.example.lab8.Entity;

import java.util.ArrayList;
import java.util.List;

public class Persona {

    private String name;
    private List<Contacto> contactos= new ArrayList<>();

    public Persona() {
        this.name = "";
    }

    public Persona(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }
}
