package com.example.demo.services;

public interface IProductService {
    public void createProductStagingTable();
    public void loadCsvDataIntoStaging(String filePath, String columns);
    public void truncateProductStagingTable();
}
