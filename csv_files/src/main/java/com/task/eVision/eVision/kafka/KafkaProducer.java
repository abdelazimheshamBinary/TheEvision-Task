package com.task.eVision.eVision.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.task.eVision.eVision.model.CSVData;

import static com.task.eVision.eVision.constant.Constants.dollarExchangeRate;
import static com.task.eVision.eVision.constant.Constants.percentageAfterFees_large;


@Component

public class KafkaProducer {
	
    private final KafkaTemplate<String, String> kafkaTemplate;
    
    
    

    @Autowired
    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    
    public void sendSmallAmountMessage(CSVData csvData) {
        String topic = "small_amounts_topic";
        String message = 
        		csvData.getNationalID() + "," +
        csvData.getName() + "," + csvData.getAmount();
        kafkaTemplate.send(topic, message);
    }

    public void sendLargeAmountMessage(CSVData csvData) {
        // Convert to dollars (EGP to USD)
        double convertedAmount = csvData.getAmount() / dollarExchangeRate;
        double finalAmount = convertedAmount * percentageAfterFees_large;

        String topic = "large_amounts_topic";
        String message = csvData.getNationalID() + "," + csvData.getName() + "," + finalAmount;
        kafkaTemplate.send(topic, message);
    }

}
