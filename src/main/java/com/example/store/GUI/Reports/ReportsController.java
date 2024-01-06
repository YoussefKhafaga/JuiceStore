package com.example.store.GUI.Reports;


import com.example.store.Sales.GetSalesDocument;
import com.example.store.Sales.Sales;
import com.example.store.Sales.TotalSummary;
import com.example.store.Shift;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportsController {
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label income;
    @FXML
    private Label outcome;
    @FXML
    private Label netIncome;
    @FXML
    private Label totalSales;
    @FXML
    private Button BackButton;
    @FXML
    private Button yearlyReport;
    @FXML
    private Button WeeklyReport;
    @FXML
    private Button DailyReport;
    @FXML
    private Button MonthlyReport;
    @FXML
    private Button Submit;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Shift> tableView;
    @FXML
    private TableColumn <Shift, String> WorkerName;
    @FXML
    private TableColumn <Shift, Integer> ShiftNumber;
    @FXML
    private TableColumn<Shift, String> StartTime;
    @FXML
    private TableColumn <Shift, String> EndTime;
    @FXML
    private TableColumn <Shift, Double> Dorg;
    //@FXML
    //private TableColumn <Shift, Double> totalSalesColumn;
    ObservableList<Shift> shiftstable = FXCollections.observableArrayList();
    public void initialize()
    {
        WorkerName.setCellValueFactory(new PropertyValueFactory<Shift, String>("username"));
        ShiftNumber.setCellValueFactory(new PropertyValueFactory<Shift, Integer>("id"));
        StartTime.setCellValueFactory(new PropertyValueFactory<Shift, String>("beginLocalTime"));
        EndTime.setCellValueFactory(new PropertyValueFactory<Shift, String>("endLocalTime"));
        Dorg.setCellValueFactory(new PropertyValueFactory<Shift, Double>("totalMoney"));
        //totalSalesColumn.setCellValueFactory(new PropertyValueFactory<Shift, Double>("total"));
        tableView.setItems(shiftstable);
        Submit.setOnAction(actionEvent -> {
            handleSubmitButton();
        });
        DailyReport.setOnAction(actionEvent -> {
            dailyReport();
        });
        WeeklyReport.setOnAction(actionEvent -> {
            weeklyReport();
        });
        MonthlyReport.setOnAction(actionEvent -> {
            MonthlyReport();
        });
        yearlyReport.setOnAction(actionEvent -> {
            yearlyReport();
        });
        BackButton.setOnAction(actionEvent -> {
            handleBackButton();
        });
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            handleSubmitButton();
            // You can perform any actions you want based on the selected date change
        });
    }
    public void checkExistingShift(Shift newShift) {
        Shift existingShift = null;
        for (Shift shift : shiftstable) {
            if (shift.getId() == newShift.getId()) {
                existingShift = shift; // Corrected line
                break;
            }
        }

        /*if (existingShift != null) {
            // If the item exists, update it
            existingShift.setProductQuantity(newProduct.getProductQuantity());
        } else {
            // If the item doesn't exist, add a new item
            tableData.add(newProduct);
        }*/
    }
    public void handleSubmitButton() {
        LocalDate selectedDate = datePicker.getValue();
        List<Shift> shifts = new ArrayList<>();
        if (selectedDate != null) {
            // Fetch shifts for the selected date and update the table
            Shift shift = new Shift();
            shifts = shift.getShiftsByDate(selectedDate);
            shiftstable.addAll(shifts);
            tableView.setItems(shiftstable);
            tableView.refresh();
        } else {
            // Date is not selected, show an alert or perform appropriate action
            showAlert("من فضلك اختار التاريخ");
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("تحذير");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void dailyReport()
    {
        TotalSummary totalSummary = new TotalSummary();
        GetSalesDocument getSalesDocument = new GetSalesDocument();
        LocalDate selectedDate = datePicker.getValue();
        totalSummary = getSalesDocument.getTotalSummary(LocalDate.now().minusDays(1), selectedDate);
        //income.setText(String.valueOf(totalSummary.getTotalPaid()));
        //outcome.setText(String.valueOf(totalSummary.getRemaining()));
        netIncome.setText(String.valueOf(totalSummary.getTotalPrice()));
        totalSales.setText(String.valueOf(totalSummary.getTotalQuantity()));
    }
    public void weeklyReport()
    {
        TotalSummary totalSummary = new TotalSummary();
        GetSalesDocument getSalesDocument = new GetSalesDocument();
        LocalDate selectedDate = datePicker.getValue();
        totalSummary = getSalesDocument.getTotalSummary(LocalDate.now().minusWeeks(1), selectedDate);
        //income.setText(String.valueOf(totalSummary.getTotalPaid()));
        //outcome.setText(String.valueOf(totalSummary.getRemaining()));
        netIncome.setText(String.valueOf(totalSummary.getTotalPrice()));
        totalSales.setText(String.valueOf(totalSummary.getTotalQuantity()));
    }
    public void MonthlyReport()
    {
        TotalSummary totalSummary = new TotalSummary();
        GetSalesDocument getSalesDocument = new GetSalesDocument();
        LocalDate selectedDate = datePicker.getValue();
        totalSummary = getSalesDocument.getTotalSummary(LocalDate.now().minusMonths(1), selectedDate);
        //income.setText(String.valueOf(totalSummary.getTotalPaid()));
        //outcome.setText(String.valueOf(totalSummary.getRemaining()));
        netIncome.setText(String.valueOf(totalSummary.getTotalPrice()));
        totalSales.setText(String.valueOf(totalSummary.getTotalQuantity()));
    }
    public void yearlyReport()
    {
        TotalSummary totalSummary = new TotalSummary();
        GetSalesDocument getSalesDocument = new GetSalesDocument();
        LocalDate selectedDate = datePicker.getValue();
        totalSummary = getSalesDocument.getTotalSummary(LocalDate.now().minusYears(1), selectedDate);
        //income.setText(String.valueOf(totalSummary.getTotalPaid()));
        //outcome.setText(String.valueOf(totalSummary.getRemaining()));
        netIncome.setText(String.valueOf(totalSummary.getTotalPrice()));
        totalSales.setText(String.valueOf(totalSummary.getTotalQuantity()));
    }
    public void handleBackButton(){
        try {
            // Load the FXML file for the second view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/store/GUI/Menu/Menu.fxml"));
            Scene scene = new Scene(loader.load());

            // Get the current stage
            Stage currentStage = (Stage) borderPane.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(scene);
            currentStage.setTitle("Menu");
            currentStage.setResizable(false);
            currentStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }
}
