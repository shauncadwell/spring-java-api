package com.frostylog.spring.api.models.temp;

public class SupplyBarcodeDto extends BarcodeAbstract {
    String supply;

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public SupplyBarcodeDto() {
    }

}