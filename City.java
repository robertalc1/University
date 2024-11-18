/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
public class City extends Locality {
    public int nrOfUniversities;

    public City(String name, int inhabitantsNr, String county, int nrOfUniversities) {
        super(name, inhabitantsNr, county);
        this.nrOfUniversities = nrOfUniversities;
    }

    public String toString() {
        return "City: Name: " + name + ", Inhabitants: " + inhabitantsNr +
                ", County: " + county + ", Number of Universities: " + nrOfUniversities;
    }
}
