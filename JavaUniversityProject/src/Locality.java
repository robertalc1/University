/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
public abstract class Locality {
    protected String name;
    protected int inhabitantsNr;
    protected String county;

    public Locality(String name, int inhabitantsNr, String county) {
        this.name = name;
        this.inhabitantsNr = inhabitantsNr;
        this.county = county;
    }

    public String getName() {
        return name;
    }

    public abstract String toString();
}