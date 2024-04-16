package com.company;

public class Medikament_Typ {
    private int id = 0;
    private String name = null;

    public Medikament_Typ() {}

    public Medikament_Typ(String name) {
        this.name = name;
    }

    public int getId() {return id;}
    public String getName() {return name;}

    public void setId(int id) {this.id = id;}
    public void setName(String name) {this.name = name;}
}
