package com.frostylog.spring.api.models.temp;

public class BarcodeAbstract {
    String title;
    String description;
    String barcode;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public BarcodeAbstract() {
    }

}