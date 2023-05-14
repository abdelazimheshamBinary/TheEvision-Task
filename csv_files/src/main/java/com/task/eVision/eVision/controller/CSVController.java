package com.task.eVision.eVision.controller;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.task.eVision.eVision.kafka.KafkaProducer;
import com.task.eVision.eVision.model.CSVData;
import com.task.eVision.eVision.service.CSVParser;

@RestController
@RequestMapping("/api/csv_files/")
@RequiredArgsConstructor
public class CSVController {

    private final CSVParser csvParser;

    
    @PostMapping("/data")
    public String postData(@RequestBody CSVData csvData) {
        csvParser.parseAndSend(csvData);
        return "Data Added Successfully";
    }


}
