package ucf.assignments.exercise56;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class InventoryTrackerController implements Initializable {
    //  make @FXML variables
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

        /*
        Item item1 = new Item("12345", "PS2", 500);
        Item item2 = new Item("X6246", "Xbox", 400);
        Item item3 = new Item("5505", "test", 5);

        list.addAll(item1, item2, item3);
         */


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
        //  make a new item and add it to the observable list
        list.add(new Item(itemSerialNumberTextField.getText(), itemNameTextField.getText(), Double.parseDouble(itemPriceTextField.getText())));
        refresh();
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
        //  set the new serial number
        itemsTableView.getItems().get(index).setSerialNumber(itemSerialNumberTextField.getText());
        //  set the new name
        itemsTableView.getItems().get(index).setName(itemNameTextField.getText());
        //  set the new value
        itemsTableView.getItems().get(index).setValue(Double.parseDouble(itemPriceTextField.getText()));
        refresh();
    }



}
