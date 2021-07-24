package ucf.assignments.exercise56;

import com.google.gson.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class InventoryTrackerController implements Initializable {

    FileChooser fileChooser = new FileChooser();

    //  make @FXML variables
    @FXML
    MenuItem saveTSV;

    @FXML
    MenuItem saveHTML;

    @FXML
    MenuItem saveJSON;

    @FXML
    MenuItem loadTSV;

    @FXML
    MenuItem loadHTML;

    @FXML
    MenuItem loadJSON;

    @FXML
    TextField itemSerialNumberTextField;

    @FXML
    TextField itemNameTextField;

    @FXML
    TextField itemPriceTextField;

    @FXML
    Button addItemButton;

    @FXML
    Button deleteItemButton;

    @FXML
    Button editItemButton;

    @FXML
    TextField filterField;

    @FXML
    TableColumn<Item, String> itemSerialNumberColumn;

    @FXML
    TableColumn<Item, String> itemNameColumn;

    @FXML
    TableColumn<Item, Double> itemValueColumn;

    @FXML
    TableView<Item> itemsTableView;

    //  create observable lists
    ObservableList<Item> list = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        itemSerialNumberColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("serialNumber"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        itemValueColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("value"));


        Item item1 = new Item("12345", "PS2", 500);
        Item item2 = new Item("X6246", "Xbox", 400);
        Item item3 = new Item("5505", "test", 5);

        list.addAll(item1, item2, item3);

        FilteredList<Item> filteredData = new FilteredList<>(list, b -> true);
            filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(item -> {
                    //  if filter text is empty, display all items
                    if(newValue == null || newValue.isEmpty()) {
                        return true;
                    }

                    //  compare serial number and name of every item with filter text
                    String lowerCaseFilter = newValue.toLowerCase();

                    if(item.getSerialNumber().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;    //  filter matches with serial number
                    } else if(item.getName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                        return true;    //  filter matches with name
                    } else {
                        return false;   //  does not match
                    }
                });
            });

            //  wrap the filteredlist in a sortedlist
            SortedList<Item> sortedData = new SortedList<>(filteredData);

            //  bind the sortedlist comparator to the tableview comparator
            sortedData.comparatorProperty().bind(itemsTableView.comparatorProperty());

            //  add sorted and filtered data to the table
            itemsTableView.setItems(sortedData);
    }


    //  refreshes the serial number, name, and price text fields
    public void refresh() {
        itemSerialNumberTextField.setText(null);
        itemNameTextField.setText(null);
        itemPriceTextField.setText(null);
        itemsTableView.refresh();
    }

    public void tableViewSelectedItem() {
        //  takes the serial  number of the item that was clicked
        itemsTableView.setOnMouseClicked(event -> {
            String selectedItemSN = itemsTableView.getSelectionModel().getSelectedItem().getSerialNumber();
            String selectedItemName = itemsTableView.getSelectionModel().getSelectedItem().getName();
            Double selectedItemValue = itemsTableView.getSelectionModel().getSelectedItem().getValue();
            //  set the data to the text fields
            itemSerialNumberTextField.setText(selectedItemSN);
            itemNameTextField.setText(selectedItemName);
            itemPriceTextField.setText(String.valueOf(selectedItemValue));
        });
        refresh();
    }

    @FXML
    public void addItem(Event e) {
        Boolean similarFound = false;
        //  checks if there is an item with the serial number from the textfield exists
        for(int i = 0; i < list.size(); i++) {
            if((itemSerialNumberTextField.getText().compareTo(list.get(i).getSerialNumber())) == 0) {
                //  if it exists, the boolean is set to true
                similarFound = true;
            }
        }
        //  if there is no item with serial number in list
        if(similarFound.equals(false)) {
            //  make a new item and add it to the observable list
            list.add(new Item(itemSerialNumberTextField.getText(), itemNameTextField.getText(), Double.parseDouble(itemPriceTextField.getText())));
            refresh();
        } else {    //  if there is an item with serial number in the list
            Alert a1 = new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Warning");
            a1.setContentText("Item with inputted serial number already exists.");
            a1.setHeaderText(null);
            a1.showAndWait();
        }
    }

    @FXML
    public void removeItem(Event e) {
        //  find the index of the selected item
        int index = itemsTableView.getSelectionModel().getSelectedIndex();
        //  take the name of the selected index
        String selectedName = itemsTableView.getItems().get(index).getName();

        //  iterate through the list of items, and if the string matches, delete the item in the list
        for(int i = 0; i < list.size(); i++) {
            if(selectedName.equals(list.get(i).getName())) {
                list.remove(i);
            }
        }
        refresh();
    }

    @FXML
    public void editItem(Event e) {
        //  find the index of the selected item
        int index = itemsTableView.getSelectionModel().getSelectedIndex();

        Boolean similarFound = false;
        //  checks if there is an item with the serial number from the textfield exists
        for(int i = 0; i < list.size(); i++) {
            if((itemSerialNumberTextField.getText().compareTo(list.get(i).getSerialNumber())) == 0) {
                //  if it exists, the boolean is set to true
                similarFound = true;
            }
            if(itemSerialNumberTextField.getText().compareTo(list.get(index).getSerialNumber()) == 0) {
                similarFound = false;
            }
        }
        //  if there is no item with serial number in list
        if(similarFound.equals(false)) {
            //  make a new item and add it to the observable list

            //  set the new serial number
            itemsTableView.getItems().get(index).setSerialNumber(itemSerialNumberTextField.getText());
            //  set the new name
            itemsTableView.getItems().get(index).setName(itemNameTextField.getText());
            //  set the new value
            itemsTableView.getItems().get(index).setValue(Double.parseDouble(itemPriceTextField.getText()));
            refresh();
        } else {    //  if there is an item with serial number in the list
            Alert a1 = new Alert(Alert.AlertType.WARNING);
            a1.setTitle("Warning");
            a1.setContentText("Item with inputted serial number already exists.");
            a1.setHeaderText(null);
            a1.showAndWait();
        }
    }

    @FXML
    public void exportHTML(ActionEvent event) {

    }

    @FXML
    public void exportJSON(ActionEvent event) {
        //  open up an open window and can select the file
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON (JavaScript Object Notation", "*.json"));
        File file = fileChooser.showSaveDialog(new Stage());

        //  if the file is not empty, write the list's data into json format
        if(file != null) {
            try {
                PrintWriter pw = new PrintWriter(file);
                StringBuilder sb = new StringBuilder();

                sb.append("{\n\t");
                sb.append("\"items\": [\n\t");
                //  separate the serial number, name, and price with tabs for each line
                for(int i = 0; i < list.size() - 1; i++) {
                    sb.append("{\n\t\t");
                    sb.append("\"serialnumber\": \"" + list.get(i).getSerialNumber() + "\",\n\t\t");
                    sb.append("\"name\": \"" + list.get(i).getName() + "\",\n\t\t");
                    sb.append("\"value\": \"" + list.get(i).getValue() + "\"\n\t");
                    sb.append("},\n\t");
                }
                sb.append("{\n\t\t");
                sb.append("\"serialnumber\": \"" + list.get(list.size() - 1).getSerialNumber() + "\",\n\t\t");
                sb.append("\"name\": \"" + list.get(list.size() - 1).getName() + "\",\n\t\t");
                sb.append("\"value\": \"" + list.get(list.size() - 1).getValue() + "\"\n\t}\n");
                sb.append("     ]\n}");


                pw.write(sb.toString());
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void importHTML(ActionEvent event) {

    }

    @FXML
    public void importJSON(ActionEvent event) {
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JSON (JavaScript Object Notation", "*.json"));
        File input = fileChooser.showOpenDialog(new Stage());
        itemsTableView.getItems().clear();

        try {
            //  process all items
            JsonElement fileElement = JsonParser.parseReader(new FileReader(input));
            JsonObject fileObject = fileElement.getAsJsonObject();

            JsonArray jsonArrayOfItems = fileObject.get("items").getAsJsonArray();
            for(JsonElement itemElement : jsonArrayOfItems) {
                //  get the JsonObject:
                JsonObject itemJsonObject = itemElement.getAsJsonObject();

                //  extract data
                String serialnumber = itemJsonObject.get("serialnumber").getAsString();
                String name = itemJsonObject.get("name").getAsString();
                Double price = itemJsonObject.get("value").getAsDouble();

                list.add(new Item(serialnumber, name, price));
            }
            itemsTableView.setItems(list);
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exportTSV(ActionEvent event) {
        //  open up an open window and can select the file
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TSV (Tab delimited)", "*.tsv"));
        File file = fileChooser.showSaveDialog(new Stage());

        //  if the file is not empty, write the list's data into tsv format
        if(file != null) {
            try {
                PrintWriter pw = new PrintWriter(file);
                StringBuilder sb = new StringBuilder();

                sb.append("serialnumber\tname\tvalue\n");
                //  separate the serial number, name, and price with tabs for each line
                for(int i = 0; i < list.size(); i++) {
                    sb.append(list.get(i).getSerialNumber());
                    sb.append("\t");
                    sb.append(list.get(i).getName());
                    sb.append("\t");
                    sb.append(list.get(i).getValue());
                    sb.append("\n");
                }
                pw.write(sb.toString());
                pw.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void importTSV(ActionEvent event) {
        //  String line = "";

        //  open up an open window and select the file
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TSV (Tab delimited)", "*.tsv"));
        File file = fileChooser.showOpenDialog(new Stage());
        itemsTableView.getItems().clear();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();
            String line = null;
            //  reads in tsv file
            while((line = br.readLine()) != null) {
                String[] temp = line.split("\t");
                Double value = Double.parseDouble(temp[2]);
                list.add(new Item(temp[0], temp[1], value));
                itemsTableView.setItems(list);
                refresh();
            }

        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
