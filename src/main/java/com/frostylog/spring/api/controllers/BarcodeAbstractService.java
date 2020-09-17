package com.frostylog.spring.api.controllers;

import com.frostylog.spring.api.models.temp.ProductBarcodeDto;
import com.frostylog.spring.api.models.temp.SupplyBarcodeDto;

public interface BarcodeAbstractService {

    static <T> void createBarcode(T barcode) {
        System.out.println("DTO passed to new service.." + barcode);
        if (barcode instanceof ProductBarcodeDto)
            System.out.println("This is a productBarcodeDto...");
        if (barcode instanceof SupplyBarcodeDto)
            System.out.println("This is a SupplyBarcodeDto...");

    }

}
