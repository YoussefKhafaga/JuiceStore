package com.example.store.GUI.Cashier;

import com.example.store.GUI.Categories.AddCategories;
import com.example.store.Product.Products;
import com.example.store.Product.RetrieveProducts;
import com.example.store.Sales.Sales;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
public class CashierController {
    @FXML
    private BorderPane borderpane;
    @FXML
    private TextField shadowTextField;
    private boolean ctrlPressed = false;
    private boolean altPressed = false;
    private ScrollPane categoriesScrollPane;
    private ScrollPane productsScrollPane;
    @FXML
    TableView<Products> tableView;
    @FXML
    TableColumn<Products, String> productName = new TableColumn<>("الصنف");
    @FXML
    TableColumn<Products, Double> productPrice = new TableColumn<>("السعر");
    @FXML
    TableColumn<Products, Integer> productQuantity = new TableColumn<>("الكمية");
    @FXML
    TableColumn<Products, String> deleteColumn = new TableColumn<>("حذف");
    @FXML
    private Label totalPayment;
    @FXML
    private TextField paidTextField;
    @FXML
    private Button printButton;
    @FXML
    private Button newOrderButton;
    @FXML
    private Label remainingLabel;
    private Double total = 0.0;

    ObservableList<Products> tableData = FXCollections.observableArrayList();


    public void initialize() {
        RetrieveProducts retrieveProducts = new RetrieveProducts();
        AddCategories addCategories = new AddCategories();
        // Load an image for the icon
        //Image iconImage = new Image(getClass().getResourceAsStream("resources/com/example/store/logo.jpg"));
        // Create an ImageView with the icon image
        //ImageView iconImageView = new ImageView(iconImage);
        List<String> categoriesList;
        categoriesList = addCategories.getAllCategories();
        categoriesScrollPane = createCategories(categoriesList, categoriesList.size(), 5);
        borderpane.setCenter(categoriesScrollPane);
        // Set up event handler for key pressed events
        borderpane.setOnKeyPressed(event -> handleKeyPressed(event));
        borderpane.setOnKeyReleased(event -> handleKeyReleased(event));
        productName.setCellValueFactory(new PropertyValueFactory<Products, String>("productName"));
        productPrice.setCellValueFactory(new PropertyValueFactory<Products, Double>("productPrice"));
        productQuantity.setCellValueFactory(new PropertyValueFactory<Products, Integer>("productQuantity"));
        deleteColumn.setCellFactory(param -> new TableCell<>() {
            Button deleteButton = new Button("حذف");
            {
                // Customize the delete button (you can use an icon instead of text)
                deleteButton.setStyle("-fx-text-fill: red;");
                //deleteButton.setGraphic(iconImageView);
                deleteButton.setOnAction(event -> {
                    // Handle delete action (e.g., remove the selected row from the table)
                    int selectedIndex = getTableRow().getIndex();
                    // Access the item from the table view
                    Products selectedItem = tableView.getItems().get(selectedIndex);

                    // Calculate the total amount to be subtracted
                    double amountToRemove = selectedItem.getProductPrice() * selectedItem.getProductQuantity();
                    // Implement your logic for deletion here
                    total -= amountToRemove;
                    tableView.getItems().remove(selectedIndex);
                    totalPayment.setText(String.valueOf(total));
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(deleteButton);
                }
            }
        });
        newOrderButton.setOnAction(actionEvent -> {
            total = 0.0;
            totalPayment.setText(String.valueOf(total));
            tableView.getItems().clear();
            tableView.getSelectionModel().clearSelection();
            tableView.getSortOrder().clear(); // Clear sorting order
            borderpane.setCenter(categoriesScrollPane);
            remainingLabel.setText(String.valueOf(0.0));
        });
        printButton.setOnAction(actionEvent -> {
            total = 0.0;
            totalPayment.setText(String.valueOf(total));
            tableView.getItems().clear();
            tableView.getSelectionModel().clearSelection();
            tableView.getSortOrder().clear(); // Clear sorting order
            borderpane.setCenter(categoriesScrollPane);
            remainingLabel.setText(String.valueOf(0.0));
            printBill();
        });
    }

    private void handleKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.CONTROL) {
            ctrlPressed = true;
            shadowTextField.requestFocus();
        } else if (ctrlPressed && event.getCode() == KeyCode.ENTER) {
            handleEnterPressed();
        } else if (ctrlPressed && event.getText().matches("[0-9]")) {
            handleDigitKey(event);
        }

        if (event.getCode() == KeyCode.ESCAPE) {
            borderpane.setCenter(categoriesScrollPane);
        }

        if (event.getCode() == KeyCode.ALT) {
            altPressed = true;
            paidTextField.requestFocus();
            event.consume();
        } else if (altPressed && event.getText().matches("[0-9]")) {
            handleDigitKey(event);
        } else if (altPressed && event.getCode() == KeyCode.ENTER) {
            handleAltEnterPressed();
        }
        if (event.getText().equalsIgnoreCase("p")) {
            //TODO connect to printer and print bill
            total = 0.0;
            totalPayment.setText(String.valueOf(total));
            tableView.getItems().clear();
            tableView.getSelectionModel().clearSelection();
            tableView.getSortOrder().clear(); // Clear sorting order
            borderpane.setCenter(categoriesScrollPane);
            remainingLabel.setText(String.valueOf(0.0));
            printBill();
        }
        if (event.getText().equalsIgnoreCase("n")) {
            total = 0.0;
            totalPayment.setText(String.valueOf(total));
            tableView.getItems().clear();
            tableView.getSelectionModel().clearSelection();
            tableView.getSortOrder().clear(); // Clear sorting order
            borderpane.setCenter(categoriesScrollPane);
            remainingLabel.setText(String.valueOf(0.0));
        }
    }

    private void handleKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.CONTROL) {
            shadowTextField.clear();
            ctrlPressed = false;
        }
        if (event.getCode() == KeyCode.ENTER) {
            shadowTextField.clear();
            paidTextField.clear();
        }

        // Handle Alt key release
        if (event.getCode() == KeyCode.ALT) {
            handleAltKeyReleased();
        }
    }

    // Add this method to handle Alt + Enter
    private void handleAltEnterPressed() {
        Double remaining = calculateReamining(Double.parseDouble(paidTextField.getText()));
        remainingLabel.setText(String.valueOf(remaining));
        borderpane.requestFocus();
    }
    private Double calculateReamining(double paid) {
        return (paid - total);
    }

    private void handleAltKeyReleased() {
        paidTextField.clear();
        altPressed = false;
    }
    private void handleEnterPressed() {
        // Get the content of the TextField and process it
        String enteredText = shadowTextField.getText();
        Button targetButton = getButtonForDigit(Integer.parseInt(enteredText));
        // Clear the TextField
        shadowTextField.clear();
        if (targetButton != null) {
            if(borderpane.getChildren().contains(categoriesScrollPane)) {
                pressCategoryButton(targetButton);
            }
            else if (borderpane.getChildren().contains(productsScrollPane)){
                pressProductButton(targetButton);
            }
        }
        else {
            // Show an alert box for the error
            Alert alert = new Alert(Alert.AlertType.ERROR, "Button not found", ButtonType.OK);
            alert.showAndWait();
        }
    }
    private void handleDigitKey(KeyEvent event) {
        // Check if Ctrl is pressed and a digit key is pressed
        if (ctrlPressed && event.getCode().isKeypadKey()) {
            event.consume();
            // Append the textual representation of the key to the TextField
            shadowTextField.appendText(event.getText());
        }
        if (altPressed && event.getCode().isKeypadKey()) {
            event.consume();
            // Append the textual representation of the key to the TextField
            paidTextField.appendText(event.getText());
        }
    }
    private void pressProductButton(Button targetButton) {
        RetrieveProducts retrieveProducts = new RetrieveProducts();
        Products products;
        String buttonText = targetButton.getText();
        String productName = buttonText.replaceAll("[0-9]+", "").trim();
        products = retrieveProducts.retrieveProductByName(productName);
        int quantity = Integer.parseInt(showCustomInputDialog(products.getProductName()));
        ctrlPressed = false;
        paidTextField.requestFocus();
        Products product = new Products(products.getProductName(), products.getProductPrice(), quantity);
        total += calculateTotal(product.getProductPrice(), quantity);
        totalPayment.setText(String.valueOf(total));
        checkExistingSale(product);
        borderpane.requestFocus();
    }

    public void checkExistingSale(Products newProduct) {
        Products existingProduct = null;
        for (Products product : tableData) {
            if (product.getProductName().equals(newProduct.getProductName())) {
                existingProduct = product; // Corrected line
                break;
            }
        }

        if (existingProduct != null) {
            // If the item exists, update it
            existingProduct.setProductQuantity(newProduct.getProductQuantity());
        } else {
            // If the item doesn't exist, add a new item
            tableData.add(newProduct);
        }
        tableView.setItems(tableData);
        tableView.refresh();
    }

    private Double calculateTotal(double price, int quantity) {
        return (price * quantity);
    }

    private String showCustomInputDialog(String productName) {
        // Create the custom input dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("ادخال الكمية");
        dialog.setHeaderText(productName + " ادخل كمية المنتج");

        // Set the icon (optional)
        dialog.setGraphic(null);

        // Create the input field and add it to the dialog
        TextField textField = new TextField();

        // Add margin to the TextField
        textField.setStyle("-fx-margin: 10;"); // You can adjust the margin value as needed

        // Create buttons for OK and Cancel
        ButtonType submitButton = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButton, ButtonType.CANCEL);

        // Enable/Disable submit button based on whether a name was entered
        Node submitButtonNode = dialog.getDialogPane().lookupButton(submitButton);
        submitButtonNode.setDisable(true);

        // Listen for changes in the input field to enable/disable the submit button
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            submitButtonNode.setDisable(newValue.trim().isEmpty());
        });

        // Set the result converter to return the entered text when the submit button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButton) {
                return textField.getText();
            }
            return null;
        });

        // Customize the layout of the DialogPane (add padding)
        dialog.getDialogPane().setPadding(new Insets(20)); // You can adjust the padding value as needed

        // Set the content of the DialogPane to include the TextField
        dialog.getDialogPane().setContent(new VBox(10, textField));

        // Request focus on the TextField
        textField.requestFocus();

        // Show the dialog and wait for the user's response
        return dialog.showAndWait().orElse(null);
    }
    private void pressCategoryButton(Button targetButton) {
        RetrieveProducts retrieveProducts = new RetrieveProducts();
        List<Products> productsList;
        ScrollPane scrollPane;
        String buttonText = targetButton.getText();
        System.out.println(buttonText);
        String category = buttonText.replaceAll("[0-9]+", "").trim();
        productsList = retrieveProducts.retrieveProductsByCategory(category);
        scrollPane = createCategoryProducts(productsList, productsList.size(), 5);
        productsScrollPane = scrollPane;
        borderpane.setCenter(productsScrollPane);
    }
    private Button getButtonForDigit(int enteredNumber) {
        return (Button) borderpane.lookup("#" + "Button" + enteredNumber);
    }
    public ScrollPane createCategories(List<String> categories, int numberOfCategories, int columns) {
        // Create a new GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20); // Set horizontal gap between buttons
        gridPane.setVgap(20); // Set vertical gap between buttons
        gridPane.setPadding(new Insets(10)); // Set padding around the GridPane

        // Add buttons to the GridPane
        int columnIndex = 0;
        int rowIndex = 0;
        for (int i = 0; i < numberOfCategories; i++) {
            Button button = new Button();
            button.setId("Button" + (i+1));
            button.setText((i+1) + " " + categories.get(i));
            button.setOnAction(actionEvent -> pressCategoryButton(button));

            // Add the button to the GridPane at the specified column and row
            gridPane.add(button, columnIndex, rowIndex);

            // Increment the column index
            columnIndex++;

            // Check if the column index exceeds the specified number of columns
            if (columnIndex >= columns) {
                columnIndex = 0; // Reset column index
                rowIndex++; // Move to the next row
            }
        }
        // Create a ScrollPane and set the GridPane as its content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setId("createCategoriesScrollPane");


        return scrollPane;
    }
    public ScrollPane createCategoryProducts(List<Products> productList, int numberOfProducts, int columns) {
        // Create a new GridPane
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10); // Set horizontal gap between buttons
        gridPane.setVgap(10); // Set vertical gap between buttons
        gridPane.setPadding(new Insets(10)); // Set padding around the GridPane

        // Add buttons to the GridPane
        int columnIndex = 0;
        int rowIndex = 0;
        for (int i = 0; i < numberOfProducts; i++) {
            Button button = new Button();
            button.setId("Button" + (i+1));
            Products product = productList.get(i);
            button.setText((i + 1) + " " + product.getProductName());

            // Add the button to the GridPane at the specified column and row
            gridPane.add(button, columnIndex, rowIndex);

            // Increment the column index
            columnIndex++;

            // Check if the column index exceeds the specified number of columns
            if (columnIndex >= columns) {
                columnIndex = 0; // Reset column index
                rowIndex++; // Move to the next row
            }
        }

        // Create a ScrollPane and set the GridPane as its content
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(gridPane);
        scrollPane.setFitToWidth(true);

        // Set the ScrollPane focusable to receive key events
        scrollPane.setFocusTraversable(true);
        scrollPane.setId("createCategoriesProductsScrollPane");
        return scrollPane;
    }
    public void printBill() {
        String printerName = "YourPrinterName";
        Printer printer = Printer.getAllPrinters().stream()
                .filter(p -> p.getName().equals(printerName))
                .findFirst()
                .orElse(null);

        if (printer == null) {
            // Handle the case where the printer is not found
            System.out.println("Printer not found: " + printerName);
            return;
        }

        try {
            PrinterJob printerJob = PrinterJob.createPrinterJob(printer);

            if (printerJob != null && printerJob.showPrintDialog(tableView.getScene().getWindow())) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dateTimeString = now.format(formatter);

                String header = "Date/Time: " + dateTimeString + "\n\n";

                // Create a Text node for content
                Text contentText = new Text(header);

                // Append table content in a table format
                contentText.setText(contentText.getText() + String.format("%-20s%-10s%-10s\n", "Product Name", "Price (جنية)", "Quantity"));
                for (Products product : tableData) {
                    contentText.setText(contentText.getText() + String.format("%-20s%-10.2f%-10d\n", product.getProductName(), product.getProductPrice(), product.getProductQuantity()));
                }

                // Append total, paid, and remaining
                contentText.setText(contentText.getText() + "\nTotal: " + total + " جنية\n");
                contentText.setText(contentText.getText() + "Paid: " + totalPayment.getText() + " جنية\n");
                contentText.setText(contentText.getText() + "Remaining: " + remainingLabel.getText() + " جنية\n");

                // Print the content
                if (printerJob.printPage(contentText)) {
                    printerJob.endJob();
                }
            }
        } catch (Exception e) {
            // Handle the exception (e.g., log, display an error message)
            e.printStackTrace();
        }
    }

}