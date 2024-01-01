package com.example.store.GUI.Reports;

import com.example.store.Sales.GetSalesDocument;
import com.example.store.Sales.TotalSummary;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

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
    public void initialize()
    {
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
    }
    public void dailyReport()
    {
        TotalSummary totalSummary = new TotalSummary();
        GetSalesDocument getSalesDocument = new GetSalesDocument();

        totalSummary = getSalesDocument.getTotalSummary(LocalDate.now().minusDays(1), LocalDate.now());
        income.setText(String.valueOf(totalSummary.getTotalPaid()));
        outcome.setText(String.valueOf(totalSummary.getRemaining()));
        netIncome.setText(String.valueOf(totalSummary.getTotalPaid() - totalSummary.getRemaining()));
        totalSales.setText(String.valueOf(totalSummary.getTotalQuantity()));
    }
    public void weeklyReport()
    {
        TotalSummary totalSummary = new TotalSummary();
        GetSalesDocument getSalesDocument = new GetSalesDocument();

        totalSummary = getSalesDocument.getTotalSummary(LocalDate.now().minusWeeks(1), LocalDate.now());
        income.setText(String.valueOf(totalSummary.getTotalPaid()));
        outcome.setText(String.valueOf(totalSummary.getRemaining()));
        netIncome.setText(String.valueOf(totalSummary.getTotalPaid() - totalSummary.getRemaining()));
        totalSales.setText(String.valueOf(totalSummary.getTotalQuantity()));
    }
    public void MonthlyReport()
    {
        TotalSummary totalSummary = new TotalSummary();
        GetSalesDocument getSalesDocument = new GetSalesDocument();

        totalSummary = getSalesDocument.getTotalSummary(LocalDate.now().minusMonths(1), LocalDate.now());
        income.setText(String.valueOf(totalSummary.getTotalPaid()));
        outcome.setText(String.valueOf(totalSummary.getRemaining()));
        netIncome.setText(String.valueOf(totalSummary.getTotalPaid() - totalSummary.getRemaining()));
        totalSales.setText(String.valueOf(totalSummary.getTotalQuantity()));
    }
    public void yearlyReport()
    {
        TotalSummary totalSummary = new TotalSummary();
        GetSalesDocument getSalesDocument = new GetSalesDocument();

        totalSummary = getSalesDocument.getTotalSummary(LocalDate.now().minusYears(1), LocalDate.now());
        income.setText(String.valueOf(totalSummary.getTotalPaid()));
        outcome.setText(String.valueOf(totalSummary.getRemaining()));
        netIncome.setText(String.valueOf(totalSummary.getTotalPaid() - totalSummary.getRemaining()));
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
