package com.company;

import java.util.Scanner;

public class Medikament {
    private int id = 0;
    private String name = null;
    private Medikament_Typ medikament_typ = new Medikament_Typ();
    private Hersteller hersteller = new Hersteller();
    private Lager lager = new Lager();

    public Medikament () {}

    public Medikament (String _name, Medikament_Typ _medikament_typ, Hersteller _hersteller, Lager _lager) {
        name = _name;
        medikament_typ = _medikament_typ;
        hersteller = _hersteller;
        lager = _lager;
    }

    public int getId () {return id;}
    public String getName () {return name;}
    public Medikament_Typ getMedikament_typ() {return medikament_typ;}
    public Hersteller getHersteller () {return hersteller;}
    public Lager getLager () {return lager;}

    public void setId(int id) {this.id = id;}
    public void setName (String name) {this.name = name;}
    public void setMedikament_typ (Medikament_Typ medikament_typ) {this.medikament_typ = medikament_typ;}
    public void setHersteller (Hersteller hersteller) {this.hersteller = hersteller;}
    public void setLager (Lager lager) {this.lager = lager;}
}
