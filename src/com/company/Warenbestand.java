package com.company;

import java.util.Date;

public class Warenbestand {
    private int id = 0;
    private Medikament medikament = new Medikament();
    private Date ablauf_datum = null;
    private double verkaufspreis = 0.00;

    public Warenbestand() {}

    public Warenbestand(Medikament medikament, Date ablauf_datum, double verkaufspreis) {
        this.medikament = medikament;
        this.ablauf_datum = ablauf_datum;
        this.verkaufspreis = verkaufspreis;
    }

    public int getId() {return id;}
    public Medikament getMedikament() {return medikament;}
    public Date getAblauf_datum() {return ablauf_datum;}
    public double getVerkaufspreis() {return verkaufspreis;}

    public void setMedikament(Medikament medikament) {this.medikament = medikament;}
    public void setAblauf_datum(Date ablauf_datum) {this.ablauf_datum = ablauf_datum;}
    public void setVerkaufspreis(double verkaufspreis) {this.verkaufspreis = verkaufspreis;}
}
