module com.example.store {
    requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;

    opens com.example.store to javafx.fxml;
    exports com.example.store;

    exports com.example.store.Admin;
    opens com.example.store.Admin to javafx.fxml;

    exports com.example.store.Product;
    opens com.example.store.Product to javafx.fxml;

    exports com.example.store.Sales;
    opens com.example.store.Sales to javafx.fxml;

    exports com.example.store.GUI.Login;
    opens com.example.store.GUI.Login to javafx.fxml;

    exports com.example.store.Test;
    opens com.example.store.Test to javafx.fxml;

    opens com.example.store.GUI.Menu to javafx.fxml;
    exports com.example.store.GUI.Menu to javafx.fxml;

    opens com.example.store.GUI.AddItems;
    exports com.example.store.GUI.AddItems to javafx.fxml;

    opens com.example.store.GUI.Cashier;
    exports com.example.store.GUI.Cashier to javafx.fxml;

    opens com.example.store.GUI.Reports;
    exports com.example.store.GUI.Reports to javafx.fxml;

    opens com.example.store.GUI.Categories;
    exports com.example.store.GUI.Categories to javafx.fxml;

    opens com.example.store.GUI.Purchases;
    exports com.example.store.GUI.Purchases to javafx.fxml;

    opens com.example.store.GUI.Returns;
    exports com.example.store.GUI.Returns to javafx.fxml;

}