/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;


public class LocalityManager extends Frame implements ActionListener {
    private ArrayList<Locality> localities;
    private List localityList;
    private Button addButton, modifyButton, deleteButton, exitButton;
    private Panel inputPanel;
    private Dialog addDialog;
    private CheckboxGroup radioGroup;
    private Checkbox cityRadio, villageRadio;
    private TextField nameField, inhabitantsNrField, countyField, nrOfUniversitiesField, areaField;
    private Button saveButton, cancelButton;

    public LocalityManager() {
        localities = new ArrayList<>();
        loadLocalities();

        setTitle("Locality Management");

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                saveLocalities();
                System.exit(0);
            }
        });
        setLayout(new BorderLayout());

        localityList = new List();
        for (Locality locality : localities) {
            localityList.add(locality.toString());
        }
        add(localityList, BorderLayout.CENTER);

        Panel buttonPanel = new Panel();
        addButton = new Button("Add");
        modifyButton = new Button("Modify");
        deleteButton = new Button("Delete");
        exitButton = new Button("Exit");
        addButton.addActionListener(this);
        modifyButton.addActionListener(this);
        deleteButton.addActionListener(this);
        exitButton.addActionListener(this);
        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exitButton);
        add(buttonPanel, BorderLayout.WEST);

        addWindow();

        setSize(700, 400);
        setLocationRelativeTo(null);
    }

    private void addWindow() {
        addDialog = new Dialog(this, "Add Locality", true);
        addDialog.setLayout(new FlowLayout());
        addDialog.setPreferredSize(new Dimension(500, 200));

        inputPanel = new Panel(new GridLayout(10, 2));

        radioGroup = new CheckboxGroup();
        cityRadio = new Checkbox("City", radioGroup, false);
        villageRadio = new Checkbox("Village", radioGroup, false);


        nameField = new TextField();
        inhabitantsNrField = new TextField();
        countyField = new TextField();
        nrOfUniversitiesField = new TextField();
        areaField = new TextField();
        inputPanel.add(new Label("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new Label("Inhabitants:"));
        inputPanel.add(inhabitantsNrField);
        inputPanel.add(new Label("County:"));
        inputPanel.add(countyField);
        inputPanel.add(new Label("Number of Universities:"));
        inputPanel.add(nrOfUniversitiesField);
        inputPanel.add(new Label("Area:"));
        inputPanel.add(areaField);
        inputPanel.add(cityRadio);
        inputPanel.add(villageRadio);
        addDialog.add(inputPanel, BorderLayout.CENTER);
        addDialog.setResizable(false);

        Panel buttonPanel = new Panel();
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        saveButton.addActionListener(this);
        cancelButton.addActionListener(this);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        addDialog.add(buttonPanel);
        addDialog.setLocationRelativeTo(this);

        cityRadio.addItemListener(e -> updateTextFields());
        villageRadio.addItemListener(e -> updateTextFields());

        addDialog.pack();
        updateTextFields();
    }

    private void loadLocalities() {
        try (BufferedReader reader = new BufferedReader(new FileReader("localities.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals("City")) {
                    localities.add(new City(parts[1], Integer.parseInt(parts[2]),
                            parts[3], Integer.parseInt(parts[4])));
                } else if (parts[0].equals("Village")) {
                    localities.add(new Village(parts[1], Integer.parseInt(parts[2]),
                            parts[3], Integer.parseInt(parts[4])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLocalities() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("localities.txt"))) {
            for (Locality locality : localities) {
                if (locality instanceof City) {
                    City city = (City) locality;
                    writer.println("City," + city.name + "," + city.inhabitantsNr +
                            "," + city.county + "," + city.nrOfUniversities);
                } else if (locality instanceof Village) {
                    Village village = (Village) locality;
                    writer.println("Village," + village.name + "," + village.inhabitantsNr +
                            "," + village.county + "," + village.area);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addLocality() {
        if (cityRadio.getState()) {
            if (isDuplicate(nameField.getText())) {
                showMessageDialog("Locality with the same name already exists.", "Duplicate Entry");
                return;
            }
            String name = nameField.getText();
            int inhabitantsNr = validations(inhabitantsNrField.getText(), "Inhabitants");
            if (inhabitantsNr == -1) return;
            String county = countyField.getText();
            int nrOfUniversities = validations(nrOfUniversitiesField.getText(), "Number of Universities");
            if (nrOfUniversities == -1) return;

            City city = new City(name, inhabitantsNr, county, nrOfUniversities);
            localities.add(city);
            localityList.add(city.toString());
        }
        else if (villageRadio.getState()) {
            if (isDuplicate(nameField.getText())) {
                showMessageDialog("Locality with the same name already exists.", "Duplicate Entry");
                return;
            }
            String name = nameField.getText();
            int inhabitantsNr = validations(inhabitantsNrField.getText(), "Inhabitants");
            if (inhabitantsNr == -1) return;
            String county = countyField.getText();
            int area = validations(areaField.getText(), "Area");
            if (area == -1) return;

            Village village = new Village(name, inhabitantsNr, county, area);
            localities.add(village);
            localityList.add(village.toString());
        }

        clearFields();

        addDialog.setVisible(false);

        saveLocalities();
    }

    private boolean isDuplicate(String name) {
        for (Locality locality : localities) {
            if (locality.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private int validations(String fieldValue, String fieldName) {
        if (fieldValue.isEmpty()) {
            showMessageDialog("Please enter " + fieldName + ".", "Validation Error");
            return -1;
        }

        try {
            int value = Integer.parseInt(fieldValue);

            if (value < 0) {
                showMessageDialog(fieldName + " cannot be negative.", "Validation Error");
                return -1;
            }

            if (fieldName.equals("Inhabitants") && value > 1000000) {
                showMessageDialog("Inhabitants cannot exceed 1,000,000.", "Validation Error");
                return -1;
            }

            if (fieldName.equals("Number of Universities") && value > 100) {
                showMessageDialog("Number of Universities cannot exceed 100.", "Validation Error");
                return -1;
            }

            if (fieldName.equals("Area") && value > 10000) {
                showMessageDialog("Area cannot exceed 10,000.", "Validation Error");
                return -1;
            }

            return value;
        } catch (NumberFormatException ex) {
            showMessageDialog("Please enter a valid number for " + fieldName + ".", "Validation Error");
            return -1;
        }
    }

    private void modifyLocality() {
        int selectedIndex = localityList.getSelectedIndex();

        if (selectedIndex != -1) {
            Locality locality = localities.get(selectedIndex);

            if (locality instanceof City) {
                cityRadio.setState(true);
                villageRadio.setState(false);


                City city = (City) locality;
                nameField.setText(city.name);
                inhabitantsNrField.setText(String.valueOf(city.inhabitantsNr));
                countyField.setText(city.county);
                nrOfUniversitiesField.setText(String.valueOf(city.nrOfUniversities));
                nrOfUniversitiesField.setEnabled(true);
                areaField.setText("");
                areaField.setEnabled(false);
            } else if (locality instanceof Village) {
                villageRadio.setState(true);
                cityRadio.setState(false);

                Village village = (Village) locality;
                nameField.setText(village.name);
                inhabitantsNrField.setText(String.valueOf(village.inhabitantsNr));
                countyField.setText(village.county);
                areaField.setText(String.valueOf(village.area));
                areaField.setEnabled(true);
                nrOfUniversitiesField.setText("");
                nrOfUniversitiesField.setEnabled(false);
            }

            addDialog.setTitle("Modify Locality");
            addDialog.setVisible(true);
        }
    }

    private void deleteLocality() {
        int selectedIndex = localityList.getSelectedIndex();

        if (selectedIndex != -1) {
            showConfirmationWindow("Are you sure you want to delete the selected locality?", "Confirmation");

            saveLocalities();
        }
    }private void updateTextFields() {
        if (cityRadio.getState()) {
            nrOfUniversitiesField.setEnabled(true);
            areaField.setEnabled(false);
        } else if (villageRadio.getState()) {
            nrOfUniversitiesField.setEnabled(false);
            areaField.setEnabled(true);
        }
    }

    private void showConfirmationWindow(String message, String title) {
        Dialog dialog = new Dialog(this, title, true);
        dialog.setLayout(new BorderLayout());
        dialog.add(new Label(message), BorderLayout.CENTER);

        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.addActionListener(e -> {
            int selectedIndex = localityList.getSelectedIndex();
            if (selectedIndex != -1) {
                localities.remove(selectedIndex);
                localityList.remove(selectedIndex);
            }
            dialog.dispose();
        });

        noButton.addActionListener(e -> dialog.dispose());

        Panel buttonPanel = new Panel(new FlowLayout());
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void clearFields() {
        nameField.setText("");
        inhabitantsNrField.setText("");
        countyField.setText("");
        nrOfUniversitiesField.setText("");
        areaField.setText("");
    }

    private void showMessageDialog(String message, String title) {
        Dialog dialog = new Dialog(this, title, true);
        dialog.setLayout(new FlowLayout());
        dialog.add(new Label(message));

        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.dispose());
        dialog.add(okButton);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitButton) {
            System.exit(0);
        }

        if (e.getSource() == addButton) {
            clearFields();
            addDialog.setVisible(true);
        }
        else if (e.getSource() == modifyButton) {
            modifyLocality();
        }
        else if (e.getSource() == deleteButton) {
            deleteLocality();
        }
        else if (e.getSource() == saveButton) {
            int selectedIndex = localityList.getSelectedIndex();

            if (selectedIndex != -1) {
                if (cityRadio.getState()) {
                    if (nameField.getText().trim().isEmpty()) {
                        showMessageDialog("Please enter a name.", "Validation Error");
                        return;
                    }
                    String name = nameField.getText().trim();

                    int inhabitantsNr = validations(inhabitantsNrField.getText(), "Inhabitants");
                    if (inhabitantsNr == -1) return;

                    if (countyField.getText().trim().isEmpty()) {
                        showMessageDialog("Please enter a county.", "Validation Error");
                        return;
                    }
                    String county = countyField.getText().trim();

                    int nrOfUniversities = validations(nrOfUniversitiesField.getText(), "Number of Universities");
                    if (nrOfUniversities == -1) return;

                    City modifiedLocality = new City(name, inhabitantsNr, county, nrOfUniversities);
                    localities.set(selectedIndex, modifiedLocality);
                    localityList.replaceItem(modifiedLocality.toString(), selectedIndex);
                }
                else if (villageRadio.getState()) {
                    if (nameField.getText().trim().isEmpty()) {
                        showMessageDialog("Please enter a name.", "Validation Error");
                        return;
                    }
                    String name = nameField.getText().trim();

                    int inhabitantsNr = validations(inhabitantsNrField.getText(), "Inhabitants");
                    if (inhabitantsNr == -1) return;

                    if (countyField.getText().trim().isEmpty()) {
                        showMessageDialog("Please enter a county.", "Validation Error");
                        return;
                    }
                    String county = countyField.getText().trim();

                    int area = validations(areaField.getText(), "Area");
                    if (area == -1) return;

                    Village modifiedLocality = new Village(name, inhabitantsNr, county, area);
                    localities.set(selectedIndex, modifiedLocality);
                    localityList.replaceItem(modifiedLocality.toString(), selectedIndex);
                }

                addDialog.setVisible(false);
                saveLocalities();
            }
            else {
                if (cityRadio.getState() || villageRadio.getState()) {
                    if (nameField.getText().trim().isEmpty()) {
                        showMessageDialog("Please enter a name.", "Validation Error");
                        return;
                    }

                    int inhabitantsNr = validations(inhabitantsNrField.getText(), "Inhabitants");
                    if (inhabitantsNr == -1) return;

                    if (isDuplicate(nameField.getText().trim())) {
                        showMessageDialog("Locality with the same name already exists.", "Duplicate Entry");
                        return;
                    }

                    if (cityRadio.getState()) {
                        if (countyField.getText().trim().isEmpty()) {
                            showMessageDialog("Please enter a county.", "Validation Error");
                            return;
                        }

                        int nrOfUniversities = validations(nrOfUniversitiesField.getText(), "Number of Universities");
                        if (nrOfUniversities == -1) return;

                        addLocality();
                    }
                    else if (villageRadio.getState()) {
                        if (countyField.getText().trim().isEmpty()) {
                            showMessageDialog("Please enter a county.", "Validation Error");
                            return;
                        }

                        int area = validations(areaField.getText(), "Area");
                        if (area == -1) return;

                        addLocality();
                    }
                }
                else {
                    showMessageDialog("Please select a locality type.", "Validation Error");
                }
            }
        }
        else if (e.getSource() == cancelButton) {
            addDialog.setVisible(false);
        }
    }

    public static void main(String[] args) {
        new LocalityManager().setVisible(true);
    }
}
