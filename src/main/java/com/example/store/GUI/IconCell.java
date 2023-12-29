package com.example.store.GUI;

import com.example.store.Product.Products;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconCell extends TableCell<Products, Products> {
    public IconCell(String iconName) {
        Button iconButton = new Button();
        iconButton.setGraphic(new ImageView(new Image(getClass().getResourceAsStream(iconName + ".svg"))));
        iconButton.setOnAction(event -> {
            Products products = getTableView().getItems().get(getIndex());
            if (iconName.equals("edit")) {
                //TODO implement edit
            } else if (iconName.equals("delete")) {
                //TODO implement delete
            }
        });

    }
}
