package com.task.eVision.eVision.files;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static com.task.eVision.eVision.constant.Constants.*;

@Component
public class LargeAmountsProcessor {
    @KafkaListener(topics = "large_amounts_topic")
    public void processLargeAmounts(ConsumerRecord<String, String> record) {
        // Extract data from the Kafka message
        String[] data = record.value().split(",");
        String nationalID = data[0];
        String name = data[1];
        double amount = Double.parseDouble(data[2]);

            if (amount >= checkAmountsNumber) {

                // Convert to dollars (EGP to USD)

                double convertedAmount = amount / dollarExchangeRate;
                double finalAmount = convertedAmount * percentageAfterFees_large;


                String result = nationalID + "," + name + "," + finalAmount;
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(LargeFilePath, true))) {

                    writer.write(result);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

    }
}
