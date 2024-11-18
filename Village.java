/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class Village extends Locality {
    public int area;

    public Village(String name, int inhabitantsNr, String county, int area) {
        super(name, inhabitantsNr, county);
        this.area = area;
    }

    public String toString() {
        return "Village: Name: " + name + ", Inhabitants: " + inhabitantsNr +
                ", County: " + county + ", Area: " + area;
    }
}
