package com.company;

public class Lager {
    private int id = 0;
    private int einheiten = 0;

    public Lager() { }

    public Lager(int einheiten) {
        this.einheiten = einheiten;
    }

    public int getId() {return id;}
    public int getEinheiten() {return einheiten;}

    public void setId(int id) {this.id = id;}
    public void setEinheiten(int einheiten) {this.einheiten = einheiten;}
}
