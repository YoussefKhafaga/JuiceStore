package com.example.store.GUI.Cashier;

import com.example.store.GUI.Login.HelloController;
import com.example.store.Shift;
import com.example.store.Workers;
import com.sun.javafx.print.PrintHelper;
import com.sun.javafx.print.Units;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.print.*;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import com.example.store.GUI.Categories.AddCategories;
import com.example.store.Product.Products;
import com.example.store.Product.GetProductDocument;
import com.example.store.Sales.AddSalesDocument;
import com.example.store.Sales.GetSalesDocument;
import com.example.store.Sales.Sales;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javafx.css.SizeUnits.MM;
import static javafx.css.SizeUnits.S;

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
    private Double paid = 0.0;
    @FXML
    private Button BackButton;
    @FXML
    private Button enterNumber;
    @FXML
    private Button enterPaid;
    ObservableList<Products> tableData = FXCollections.observableArrayList();
    List<Products> saleProducts = new ArrayList<>();
    //@FXML
    //private Button Purchases;
    @FXML
    private Button Returns;
    @FXML
    private Button CloseShift;
    @FXML
    private Button OpenShift;
    @FXML
    private Button AddWorker;
    @FXML
    private Button Delivery;
    private String DeliveryName = null;
    private Double DeliveryValue = null;
    @FXML
    public Label shiftNumber;
    public void initialize() {
        Shift shift = new Shift();
        shiftNumber.setText(String.valueOf(shift.getLatestShiftId()));
        AddWorker.setOnAction(actionEvent -> {
            handleAddWorker();
        });
        Delivery.setOnAction(actionEvent -> {
            handleDelivery();
        });
        Returns.setOnAction(actionEvent -> {
            handleReturnsView();
        });
        /*Purchases.setOnAction(actionEvent -> {
            handlePurchasesView();
        });*/

        CloseShift.setOnAction(actionEvent -> {
            checkCloseShift();
        });

        OpenShift.setOnAction(actionEvent -> {
            checkOpenShift();
        });
        //GetProductDocument getProductDocument = new GetProductDocument();
        AddCategories addCategories = new AddCategories();
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
        enterNumber.setOnAction(actionEvent -> {
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
        });
        BackButton.setOnAction(actionEvent -> {
            handleBackButton();
        });
        enterPaid.setOnAction(actionEvent -> {
            paid = Double.parseDouble(paidTextField.getText());
            Double remaining = calculateReamining(Double.parseDouble(paidTextField.getText()));
            remainingLabel.setText(String.valueOf(remaining));
            borderpane.requestFocus();
        });
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
            saleProducts.clear();
            paidTextField.clear();
        });
        printButton.setOnAction(actionEvent -> {
            Sales sale = new Sales(generateUniqueId(), saleProducts, total, paid,
                    Double.parseDouble(remainingLabel.getText()), LocalDate.now(), LocalTime.now());
            if (DeliveryValue != null || DeliveryName != null)
            {
                printBill(sale, true);
                total = 0.0;
                totalPayment.setText(String.valueOf(total));
                tableView.getItems().clear();
                tableView.getSelectionModel().clearSelection();
                tableView.getSortOrder().clear();
                borderpane.setCenter(categoriesScrollPane);
                remainingLabel.setText(String.valueOf(0.0));
                AddSalesDocument addSalesDocument = new AddSalesDocument();
                addSalesDocument.AddSale(sale);
                saleProducts.clear();
            }
            if(checkInputMoney()) {
                printBill(sale, false);
                total = 0.0;
                totalPayment.setText(String.valueOf(total));
                tableView.getItems().clear();
                tableView.getSelectionModel().clearSelection();
                tableView.getSortOrder().clear();
                borderpane.setCenter(categoriesScrollPane);
                remainingLabel.setText(String.valueOf(0.0));
                AddSalesDocument addSalesDocument = new AddSalesDocument();
                addSalesDocument.AddSale(sale);
                saleProducts.clear();
            }
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
            Sales sale = new Sales(generateUniqueId(), saleProducts, total, paid,
                    Double.parseDouble(remainingLabel.getText()), LocalDate.now(), LocalTime.now());
            if (DeliveryValue != null || DeliveryName != null)
            {
                printBill(sale, true);
                total = 0.0;
                totalPayment.setText(String.valueOf(total));
                tableView.getItems().clear();
                tableView.getSelectionModel().clearSelection();
                tableView.getSortOrder().clear();
                borderpane.setCenter(categoriesScrollPane);
                remainingLabel.setText(String.valueOf(0.0));
                AddSalesDocument addSalesDocument = new AddSalesDocument();
                addSalesDocument.AddSale(sale);
                saleProducts.clear();
            }
            if(checkInputMoney()) {
                printBill(sale, false);
                total = 0.0;
                totalPayment.setText(String.valueOf(total));
                tableView.getItems().clear();
                tableView.getSelectionModel().clearSelection();
                tableView.getSortOrder().clear();
                borderpane.setCenter(categoriesScrollPane);
                remainingLabel.setText(String.valueOf(0.0));
                AddSalesDocument addSalesDocument = new AddSalesDocument();
                addSalesDocument.AddSale(sale);
                saleProducts.clear();
            }
        }
        if (event.getText().equalsIgnoreCase("n")) {
            total = 0.0;
            totalPayment.setText(String.valueOf(total));
            tableView.getItems().clear();
            tableView.getSelectionModel().clearSelection();
            tableView.getSortOrder().clear(); // Clear sorting order
            borderpane.setCenter(categoriesScrollPane);
            remainingLabel.setText(String.valueOf(0.0));
            saleProducts.clear();
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
        paid = Double.parseDouble(paidTextField.getText());
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
        GetProductDocument getProductDocument = new GetProductDocument();
        Products products;
        String buttonText = targetButton.getText();
        String productName = buttonText.replaceAll("[0-9]+", "").trim();
        products = getProductDocument.retrieveProductByName(productName);
        int quantity = Integer.parseInt(showCustomInputDialog(products.getProductName()));
        ctrlPressed = false;
        paidTextField.requestFocus();
        Products product = new Products(products.getProductName(), products.getProductPrice(), quantity);
        saleProducts.add(product);
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
        GetProductDocument getProductDocument = new GetProductDocument();
        List<Products> productsList;
        ScrollPane scrollPane;
        String buttonText = targetButton.getText();
        String category = buttonText.replaceAll("[0-9]+", "").trim();
        productsList = getProductDocument.retrieveProductsByCategory(category);
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
            button.setWrapText(true);
            button.setStyle("-fx-background-color: lightblue; -fx-background-radius: 12px;");

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
            button.setWrapText(true);
            button.setOnAction(actionEvent -> pressProductButton(button));
            // Check the text content and set the button color and border radius accordingly
            if (button.getText().contains("كبير")) {
                button.setStyle("-fx-background-color: lightcoral; -fx-background-radius: 12px;");
            } else if (button.getText().contains("وسط")) {
                button.setStyle("-fx-background-color: darkorange; -fx-background-radius: 12px;");
            } else if (button.getText().contains("صغير")) {
                button.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 12px;");
            }


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
    public void printBill(Sales sale, boolean delivery) {
        String printerName = "XP-80C";
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
            printerJob.getJobSettings().setCopies(2);

            if (printerJob != null) {
                // Adjust page layout and size settings
                PageLayout pageLayout = printerJob.getJobSettings().getPageLayout();
                double printableWidth = pageLayout.getPrintableWidth();
                double printableHeight = pageLayout.getPrintableHeight();

                // Your existing code for header and contentText

                // Create a Text node for content
                Text contentText = new Text();
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String dateTimeString = now.format(formatter);
                System.out.println(printableWidth);
                // Your header information
                String dump = "\n";
                String name = "محلات خان ماريا";
                String address = "جناكليز شارع ابو قير";
                String id = String.valueOf(sale.getId());


                String header =  // Append the dynamically generated dashed line to contentText
                        dump  + name + "\n" +
                                address + "\n" +
                                "رقم العملية " + id + "\n" +
                                dateTimeString + "\n";

                // Create a Text node for content
                contentText.setWrappingWidth(printableWidth);
                contentText.setNodeOrientation(javafx.geometry.NodeOrientation.RIGHT_TO_LEFT);
                contentText.setFont(Font.font(10));
                contentText.setStyle("-fx-font-weight: bold;");

                // Append header information to contentText
                contentText.setText(header);
                // Append the dynamically generated dashed line to contentText
                contentText.setText(contentText.getText() + "____________________________");
                // Append table headers
                contentText.setText(contentText.getText() + "\nاسم المنتج | الكمية | السعر\n");
                // Append the dynamically generated dashed line to contentText
                contentText.setText(contentText.getText() + "____________________________\n");
                // Append table content in a table format
                for (Products product : tableData) {
                    contentText.setText(contentText.getText() +
                            String.format("%s | %d | %.2f\n",
                                    product.getProductName(), product.getProductQuantity()
                                    , product.getProductPrice()));
                    // Append the dynamically generated dashed line to contentText
                    contentText.setText(contentText.getText() + "____________________________\n");                }


                if(delivery)
                {
                    // Append total, paid, and remaining
                    contentText.setText(contentText.getText() + "الاجمالي: " + total + " جنية\n");
                    contentText.setText(contentText.getText() + "المدفوع: " + paid + " جنية\n");
                    contentText.setText(contentText.getText() + "الباقي: " + "0.0" + " جنية\n");
                    //contentText.setText(contentText.getText() + "____________________________\n");
                }
                if(!delivery)
                {
                    // Append total, paid, and remaining
                    contentText.setText(contentText.getText() + "الاجمالي: " + total + " جنية\n");
                    contentText.setText(contentText.getText() + "المدفوع: " + paid + " جنية\n");
                    contentText.setText(contentText.getText() + "الباقي: " + remainingLabel.getText() + " جنية\n");
                    //contentText.setText(contentText.getText() + "____________________________\n");
                }

                if (DeliveryName != null && DeliveryValue != null)
                {
                    remainingLabel.setText("0.0");
                    contentText.setText(contentText.getText() + "تيك اواي\n");
                    contentText.setText(contentText.getText() + "العنوان: " + DeliveryName + "\n");
                    contentText.setText(contentText.getText() + "قيمة التوصيل: " + DeliveryValue + "\n");
                    //contentText.setText(contentText.getText() + "____________________________\n");
                    DeliveryValue = null;
                    DeliveryName = null;
                }
                double actualTextHeight = contentText.getBoundsInLocal().getHeight();
                // Create a rectangle or border around the content text
                Rectangle border = new Rectangle(printableWidth, actualTextHeight);
                border.setFill(null);  // Set fill to null to make it transparent
                border.setStroke(Color.BLACK);  // Set the border color
                border.setStrokeWidth(1.0);  // Set the border width
                // Print the content
                Group root = new Group(border, contentText);

                printerJob.printPage(root);
                printerJob.endJob();
                System.out.println(contentText.getText());
            }
        } catch (Exception e) {
            // Handle the exception (e.g., log, display an error message)
            e.printStackTrace();
        }
    }



    public int generateUniqueId() {
        GetSalesDocument getSalesDocument = new GetSalesDocument();
        int id = getSalesDocument.getLatestSaleIdFromDB();
        return id;
    }

    public void handleBackButton() {
        if (borderpane.getChildren().contains(productsScrollPane)) {
            // If in products view, switch to categories view
            borderpane.setCenter(categoriesScrollPane);
        } else {
            // If in categories view, switch to menu view
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Menu/Menu.fxml"));
                Scene scene = new Scene(loader.load());

                Stage currentStage = (Stage) borderpane.getScene().getWindow();
                currentStage.setScene(scene);
                currentStage.setTitle("Menu");
                currentStage.setResizable(false);
                currentStage.centerOnScreen();
            } catch (IOException e) {
                e.printStackTrace(); // Handle the exception appropriately
            }
        }
    }


    public void handlePurchasesView(){
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Purchases/Purchases.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles.css");

            // Get the current stage
            Stage currentStage = (Stage) borderpane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("المشتريات");
            currentStage.setResizable(false);
            //currentStage.setFullScreen(true);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
    /*public void handleReturnsView(){
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Returns/returns.fxml"));
            Scene scene = new Scene(loader.load());
            scene.getStylesheets().add("/styles.css");

            // Get the current stage
            Stage currentStage = (Stage) borderpane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("المرتجعات");
            currentStage.setResizable(false);
            //currentStage.setFullScreen(true);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }*/
    public void handleReturnsView() {
        // Create a TextInputDialog with both username and password fields
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Authentication");
        dialog.setHeaderText("من فضلك ادخل اسم المستخدم وكلمة المرور");

        // Set the button types
        ButtonType loginButtonType = new ButtonType("تسجيل الدخول", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("اسم المستخدم");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("كلمة المرور");

        grid.add(new Label("اسم المستخدم:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("كلمة المرور:"), 0, 1);
        grid.add(passwordField, 1, 1);

        // Enable/Disable login button depending on whether a username was entered
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(usernameField.getText(), passwordField.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            String username = usernamePassword.getKey();
            String password = usernamePassword.getValue();

            HelloController helloController = new HelloController();

            // Validate the credentials (replace this with your authentication logic)
            if (helloController.validateLogin(username, password)) {
                // Load the Returns view
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Returns/returns.fxml"));
                    Scene scene = new Scene(loader.load());
                    scene.getStylesheets().add("/styles.css");

                    Stage currentStage = (Stage) borderpane.getScene().getWindow();
                    currentStage.setScene(scene);
                    currentStage.setTitle("المرتجعات");
                    currentStage.setResizable(false);
                    currentStage.centerOnScreen();
                } catch (IOException e) {
                    e.printStackTrace(); // Handle the exception appropriately
                }
            } else {
                // Incorrect credentials, show an alert or take appropriate action
                Alert alert = new Alert(Alert.AlertType.ERROR, "خطأ في اسم المستخدم وكلمة المرور", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }

    public boolean checkInputMoney() {
        if (paidTextField != null) {
            String inputText = paidTextField.getText();

            if (!inputText.isEmpty() && inputText.matches("\\d+")) {
                // Input is not empty and contains only digits
                Double inputValue = Double.parseDouble(inputText);

                if (inputValue < Double.parseDouble(totalPayment.getText())) {
                    // Show alert box for value less than the threshold
                    Alert alert = new Alert(Alert.AlertType.ERROR, "المدفوع اقل من السعر الاجمالي", ButtonType.OK);
                    alert.showAndWait();
                    return false; // Return false for invalid input
                } else {
                    // Valid input
                    return true;
                }
            } else {
                // Show alert box for non-digit characters or empty input
                Alert alert = new Alert(Alert.AlertType.ERROR, "من فضلك ادخل ارقام", ButtonType.OK);
                alert.showAndWait();
                return false; // Return false for invalid input
            }
        } else {
            // Show alert box for null text field
            Alert alert = new Alert(Alert.AlertType.ERROR, "من فضلك ادخل المدفوع", ButtonType.OK);
            alert.showAndWait();
            return false; // Return false for invalid input
        }
    }

    public void handleAddWorker(){
        // Create a TextInputDialog with both username and password fields
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Authentication");
        dialog.setHeaderText("من فضلك ادخل اسم المستخدم وكلمة المرور");

        // Set the button types
        ButtonType loginButtonType = new ButtonType("تسجيل الدخول", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("اسم المستخدم");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("كلمة المرور");

        grid.add(new Label("اسم المستخدم:"), 0, 0);
        grid.add(usernameField, 1, 0);
        grid.add(new Label("كلمة المرور:"), 0, 1);
        grid.add(passwordField, 1, 1);

        // Enable/Disable login button depending on whether a username was entered
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Convert the result to a username-password-pair when the login button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(usernameField.getText(), passwordField.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            String username = usernamePassword.getKey();
            String password = usernamePassword.getValue();

            HelloController helloController = new HelloController();

            // Validate the credentials (replace this with your authentication logic)
            if (helloController.validateLogin(username, password)) {
                // Create a TextInputDialog with both username and password fields
                Dialog<Pair<String, String>> dialog1 = new Dialog<>();
                dialog1.setTitle("تسجيل عامل جديد");
                dialog1.setHeaderText("من فضلك ادخل اسم المستخدم وكلمة المرور");

                // Set the button types
                ButtonType loginButtonType1 = new ButtonType("تسجيل الدخول", ButtonBar.ButtonData.OK_DONE);
                dialog1.getDialogPane().getButtonTypes().addAll(loginButtonType1, ButtonType.CANCEL);

                // Create the username and password labels and fields
                GridPane grid1 = new GridPane();
                grid1.setHgap(10);
                grid1.setVgap(10);
                grid1.setPadding(new Insets(20, 150, 10, 10));

                TextField usernameField1 = new TextField();
                usernameField1.setPromptText("اسم المستخدم");
                PasswordField passwordField1 = new PasswordField();
                passwordField1.setPromptText("كلمة المرور");

                grid1.add(new Label("اسم المستخدم:"), 0, 0);
                grid1.add(usernameField1, 1, 0);
                grid1.add(new Label("كلمة المرور:"), 0, 1);
                grid1.add(passwordField1, 1, 1);

                // Enable/Disable login button depending on whether a username was entered
                Node loginButton1 = dialog1.getDialogPane().lookupButton(loginButtonType1);
                loginButton1.setDisable(true);

                // Do some validation
                usernameField1.textProperty().addListener((observable, oldValue, newValue) -> {
                    loginButton1.setDisable(newValue.trim().isEmpty());
                });

                dialog1.getDialogPane().setContent(grid1);

                // Convert the result to a username-password-pair when the login button is clicked
                dialog1.setResultConverter(dialogButton -> {
                    if (dialogButton == loginButtonType1) {
                        return new Pair<>(usernameField1.getText(), passwordField1.getText());
                    }
                    return null;
                });

                // Show the dialog and wait for the user's response
                Optional<Pair<String, String>> result1 = dialog1.showAndWait();

                result1.ifPresent(usernamePassword1 -> {
                    String username1 = usernamePassword1.getKey();
                    String password1 = usernamePassword1.getValue();

                    //add worker
                    Workers workers = new Workers(username1, password1);
                    if (workers.addWorker(username1, password1)) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "تم تسجيل العامل بنجاح", ButtonType.OK);
                        alert.showAndWait();
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR, "لم يتم تسجيل العامل بنجاح", ButtonType.OK);
                        alert.showAndWait();
                    }
                });

            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR, "خطأ في اسم المستخدم وكلمة مرور الادمن", ButtonType.OK);
                alert.showAndWait();
            }
        });
    }
    public void checkCloseShift()
    {
        {
            // Create a TextInputDialog with both username and password fields
            Dialog<Pair<String, String>> dialog1 = new Dialog<>();
            dialog1.setTitle("غلق شيفت");
            dialog1.setHeaderText("من فضلك ادخل اسم المستخدم وكلمة المرور للعامل");

            // Set the button types
            ButtonType loginButtonType1 = new ButtonType("تسجيل الدخول", ButtonBar.ButtonData.OK_DONE);
            dialog1.getDialogPane().getButtonTypes().addAll(loginButtonType1, ButtonType.CANCEL);

            // Create the username and password labels and fields
            GridPane grid1 = new GridPane();
            grid1.setHgap(10);
            grid1.setVgap(10);
            grid1.setPadding(new Insets(20, 150, 10, 10));

            TextField usernameField1 = new TextField();
            usernameField1.setPromptText("اسم العامل");
            PasswordField passwordField1 = new PasswordField();
            passwordField1.setPromptText("كلمة المرور");

            grid1.add(new Label("اسم المستخدم:"), 0, 0);
            grid1.add(usernameField1, 1, 0);
            grid1.add(new Label("كلمة المرور:"), 0, 1);
            grid1.add(passwordField1, 1, 1);

            // Enable/Disable login button depending on whether a username was entered
            Node loginButton1 = dialog1.getDialogPane().lookupButton(loginButtonType1);
            loginButton1.setDisable(true);

            // Do some validation
            usernameField1.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton1.setDisable(newValue.trim().isEmpty());
            });

            dialog1.getDialogPane().setContent(grid1);

            // Convert the result to a username-password-pair when the login button is clicked
            dialog1.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType1) {
                    return new Pair<>(usernameField1.getText(), passwordField1.getText());
                }
                return null;
            });

            // Show the dialog and wait for the user's response
            Optional<Pair<String, String>> result1 = dialog1.showAndWait();

            result1.ifPresent(usernamePassword1 -> {
                String username1 = usernamePassword1.getKey();
                String password1 = usernamePassword1.getValue();

                //add worker
                Workers workers = new Workers(username1, password1);
                if (workers.authenticateWorker(username1, password1)) {
                        // Create the custom input dialog
                        Dialog<String> dialog = new Dialog<>();
                        dialog.setTitle("ادخال مبلغ الدرج");
                        dialog.setHeaderText(" ادخل مبلغ الدرج");

                        // Set the icon (optional)
                        dialog.setGraphic(null);

                        // Create the input field and add it to the dialog
                        TextField textField = new TextField();

                        // Add margin to the TextField
                        textField.setStyle("-fx-margin: 10;"); // You can adjust the margin value as needed

                        // Create buttons for OK and Cancel
                        ButtonType submitButton = new ButtonType("ادخال", ButtonBar.ButtonData.OK_DONE);
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
                        dialog.showAndWait().orElse(null);
                    Shift shift = new Shift();
                    shift.setEndLocalDate(LocalDate.now());
                    shift.setEndLocalTime(LocalTime.now());
                    shift.setTotalMoney(Double.parseDouble(textField.getText()));
                    shift.setUsername(username1);
                    if(shift.editShift(shift, usernameField1.getText(), Integer.parseInt(shiftNumber.getText())))
                    {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "تم غلق الوردية ", ButtonType.OK);
                        alert.showAndWait();
                        shiftNumber.setText(String.valueOf(shift.getLatestShiftId()));
                        //shiftNumber.setText(String.valueOf(shift.getId()));
                    } else if (!shift.editShift(shift, usernameField1.getText(), Integer.parseInt(shiftNumber.getText()))) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "لا يوجد وردية ", ButtonType.OK);
                        alert.showAndWait();
                    }

                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "خطأ في اسم المستخدم وكلمة المرور", ButtonType.OK);
                    alert.showAndWait();
                }
            });

        }
    }
    public void checkOpenShift(){
        {
            // Create a TextInputDialog with both username and password fields
            Dialog<Pair<String, String>> dialog1 = new Dialog<>();
            dialog1.setTitle("فتح شيفت جديد");
            dialog1.setHeaderText("من فضلك ادخل اسم المستخدم وكلمة المرور للعامل");

            // Set the button types
            ButtonType loginButtonType1 = new ButtonType("تسجيل الدخول", ButtonBar.ButtonData.OK_DONE);
            dialog1.getDialogPane().getButtonTypes().addAll(loginButtonType1, ButtonType.CANCEL);

            // Create the username and password labels and fields
            GridPane grid1 = new GridPane();
            grid1.setHgap(10);
            grid1.setVgap(10);
            grid1.setPadding(new Insets(20, 150, 10, 10));

            TextField usernameField1 = new TextField();
            usernameField1.setPromptText("اسم المستخدم");
            PasswordField passwordField1 = new PasswordField();
            passwordField1.setPromptText("كلمة المرور");

            grid1.add(new Label("اسم المستخدم:"), 0, 0);
            grid1.add(usernameField1, 1, 0);
            grid1.add(new Label("كلمة المرور:"), 0, 1);
            grid1.add(passwordField1, 1, 1);

            // Enable/Disable login button depending on whether a username was entered
            Node loginButton1 = dialog1.getDialogPane().lookupButton(loginButtonType1);
            loginButton1.setDisable(true);

            // Do some validation
            usernameField1.textProperty().addListener((observable, oldValue, newValue) -> {
                loginButton1.setDisable(newValue.trim().isEmpty());
            });

            dialog1.getDialogPane().setContent(grid1);

            // Convert the result to a username-password-pair when the login button is clicked
            dialog1.setResultConverter(dialogButton -> {
                if (dialogButton == loginButtonType1) {
                    return new Pair<>(usernameField1.getText(), passwordField1.getText());
                }
                return null;
            });

            // Show the dialog and wait for the user's response
            Optional<Pair<String, String>> result1 = dialog1.showAndWait();

            result1.ifPresent(usernamePassword1 -> {
                String username1 = usernamePassword1.getKey();
                String password1 = usernamePassword1.getValue();

                //add worker
                Workers workers = new Workers(username1, password1);
                if (workers.authenticateWorker(username1, password1)) {
                    Shift shift = new Shift();
                    shift.setUsername(username1);
                    shift.setBeginLocalDate(LocalDate.now());
                    shift.setBeginLocalTime(LocalTime.now());
                    shift.setId(shift.getLatestShiftId());
                    if(shift.addShift(shift)) {
                        shiftNumber.setText(String.valueOf(shift.getLatestShiftId()));
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "تم فتح الوردية ", ButtonType.OK);
                        alert.showAndWait();
                    }
                    else{
                        Alert alert = new Alert(Alert.AlertType.ERROR, "هذا المستخدم له وردية لم تغلق من فضلك اغلق الوردية قبل بدأ وردية جديدة", ButtonType.OK);
                        alert.showAndWait();
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "خطأ في اسم المستخدم وكلمة المرور", ButtonType.OK);
                    alert.showAndWait();
                }
            });

        }
    }

    public void handleDelivery() {
        Dialog<Pair<String, Double>> dialog = new Dialog<>();
        dialog.setTitle("الدليفري");
        dialog.setHeaderText(" ادخل العنوان وقيمة التوصيل");

        // Set the icon (optional)
        dialog.setGraphic(null);

        // Create the input fields and labels, and add them to the dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label titleLabel = new Label("العنوان:");
        TextField titleField = new TextField();

        Label amountLabel = new Label("قيمة التوصيل:");
        TextField amountField = new TextField();

        grid.add(titleLabel, 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(amountLabel, 0, 1);
        grid.add(amountField, 1, 1);

        // Add margin to the TextFields
        titleField.setStyle("-fx-margin: 10;");
        amountField.setStyle("-fx-margin: 10;");

        dialog.getDialogPane().setPrefWidth(400);

        // Create buttons for OK and Cancel
        ButtonType submitButton = new ButtonType("ادخال", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButton, ButtonType.CANCEL);

        // Enable/Disable submit button based on whether both fields are entered
        Node submitButtonNode = dialog.getDialogPane().lookupButton(submitButton);
        submitButtonNode.setDisable(true);

        // Listen for changes in the input fields to enable/disable the submit button
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            submitButtonNode.setDisable(newValue.trim().isEmpty() || amountField.getText().trim().isEmpty());
        });

        amountField.textProperty().addListener((observable, oldValue, newValue) -> {
            submitButtonNode.setDisable(newValue.trim().isEmpty() || titleField.getText().trim().isEmpty());
        });

        // Set the result converter to return a Pair of strings when the submit button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == submitButton) {
                return new Pair<>(titleField.getText(), Double.parseDouble(amountField.getText()));
            }
            return null;
        });

        // Customize the layout of the DialogPane (add padding)
        dialog.getDialogPane().setPadding(new Insets(20));

        // Set the content of the DialogPane to include the GridPane
        dialog.getDialogPane().setContent(grid);

        // Request focus on the first TextField
        titleField.requestFocus();

        // Show the dialog and wait for the user's response
        Pair<String, Double> result = dialog.showAndWait().orElse(null);

        // Process the result (you can replace this with your logic)
        if (result != null) {
            DeliveryName = result.getKey();
            DeliveryValue = result.getValue();
        }
    }

}