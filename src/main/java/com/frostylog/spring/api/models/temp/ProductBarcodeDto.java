package com.frostylog.spring.api.models.temp;

public class ProductBarcodeDto extends BarcodeAbstract {
    String weight;
    String weightUnit;
    String product;

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(String weightUnit) {
        this.weightUnit = weightUnit;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public ProductBarcodeDto() {
    }
}