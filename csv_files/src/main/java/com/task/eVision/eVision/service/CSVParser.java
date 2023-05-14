package com.task.eVision.eVision.service;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.task.eVision.eVision.kafka.KafkaProducer;
import com.task.eVision.eVision.model.CSVData;
import org.springframework.stereotype.Service;

import static com.task.eVision.eVision.constant.Constants.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CSVParser {
	
    private final KafkaProducer kafkaProducer;

    private final Set<String> processedDataKeys = new HashSet<>();


    public void parseAndSend(CSVData csvData) {
        double amount = csvData.getAmount();

        log.info("the amount is " + amount);



            if (amount < checkAmountsNumber) {
                writeDataToSmallFile(csvData);
                kafkaProducer.sendSmallAmountMessage(csvData);
            }
            else {
                writeDataToLargeFile(csvData);
                kafkaProducer.sendLargeAmountMessage(csvData);
            }


            markDataAsProcessed(csvData);
    }

    private void writeDataToSmallFile(CSVData csvData) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SmallFilePath, true))) {
         
        	double finalAmount = csvData.getAmount() * percentageAfterFees_small;
        	String line = csvData.getNationalID() + "," + csvData.getName() + "," + finalAmount;
            writer.write(line);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeDataToLargeFile(CSVData csvData) {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LargeFilePath, true))) {

        	double convertedAmount = csvData.getAmount() / dollarExchangeRate;
        	double finalAmount = convertedAmount * percentageAfterFees_large;


            String line = csvData.getNationalID() + "," + csvData.getName() + "," + finalAmount;
            writer.newLine();

            writer.write(line);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isDataProcessed(CSVData csvData) {
        String dataKey = generateDataKey(csvData);
        return processedDataKeys.contains(dataKey);
    }

    private void markDataAsProcessed(CSVData csvData) {
        String dataKey = generateDataKey(csvData);
        processedDataKeys.add(dataKey);
        processedDataKeys.remove(dataKey); // Remove the data key after adding it

    }

    private String generateDataKey(CSVData csvData) {
        // Generate a unique key for the data (e.g., concatenation of relevant fields)
        return csvData.getNationalID() + "|" + csvData.getName() + "|" + csvData.getAmount();
    }


	
}
