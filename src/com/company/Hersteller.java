package com.company;

public class Hersteller {
    private int id = 0;
    private String name = null;
    private int plz = 0;
    private String ort = null;

    public Hersteller() {}

    public Hersteller(String name, int plz, String ort) {
        this.name = name;
        this.plz = plz;
        this.ort = ort;
    }

    public int getId() {return id;}
    public String getName() {return name;}
    public int getPlz() {return plz;}
    public String getOrt() {return ort;}

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
    public void setPlz(int plz) {this.plz = plz;}
    public void setOrt(String ort) {this.ort = ort;}
}
