package ucf.assignments.exercise56;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class InventoryTrackerController {
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
    TableColumn<Item, String> itemSerialNumberColumn;

    @FXML
    TableColumn<Item, String> itemNameColumn;

    @FXML
    TableColumn<Item, Double> itemValueColumn;

    @FXML
    TableView<Item> itemsTableView;

    //  create observable list
    ObservableList<Item> list = FXCollections.observableArrayList();

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
        //  add the observable list to the table view
        itemSerialNumberColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("serialNumber"));
        itemNameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));
        itemValueColumn.setCellValueFactory(new PropertyValueFactory<Item, Double>("value"));
        //  add the observable list to the table view
        itemsTableView.setItems(list);
        refresh();
    }

    @FXML
    public void removeItem(Event e) {
        //  find the index of the selected item
        int index = itemsTableView.getSelectionModel().getSelectedIndex();
        //  remove the index of the list
        itemsTableView.getItems().remove(index);
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
