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
public class SmallAmountsProcessor {
    @KafkaListener(topics = "small_amounts_topic")
    public void processSmallAmounts(ConsumerRecord<String, String> record) {
        // Extract data from the Kafka message
        String[] data = record.value().split(",");
        String nationalID = data[0];
        String name = data[1];
        double amount = Double.parseDouble(data[2]);


            if (amount < checkAmountsNumber) {
                double finalAmount = amount * percentageAfterFees_small;
                String result = nationalID + "," + name + "," + finalAmount;

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(SmallFilePath, true))) {
                    writer.write(result);
                    writer.newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
         }
}

