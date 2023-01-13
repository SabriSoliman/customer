package com.digitaltrack.customer.web.service;

import com.digitaltrack.amqp.RabbitMessageProducer;
import com.digitaltrack.clients.fraud.FraudCheckResponse;
import com.digitaltrack.clients.fraud.FraudClient;
import com.digitaltrack.clients.notification.NotificationClient;
import com.digitaltrack.clients.notification.NotificationRequest;
import com.digitaltrack.customer.model.dto.CustomerRegistrationRequest;
import com.digitaltrack.customer.repos.CustomerRepo;
import com.digitaltrack.customer.model.entities.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    public static final String FRAUDS_URL = "http://localHost:8081/frauds/{customerId}";
    private final CustomerRepo customerRepo;
    private final FraudClient fraudClient;

    private final NotificationClient notificationClient;

    private final RabbitMessageProducer rabbitMessageProducer;

    public void register(CustomerRegistrationRequest request) {
        Customer customer = Customer.builder().firstName(request.firstName()).lastName(request.lastName()).email(request.email()).build();


        //todo persist customer into db
        customerRepo.saveAndFlush(customer);
        //todo check if email not taken 'call fraud'
//        FraudCheckResponse fraudCheckResponse = restTemplate.getForObject(FRAUDS_URL,
//                FraudCheckResponse.class, customer.getId());
        FraudCheckResponse fraudulent = fraudClient.isFraudulent(customer.getId());
        Boolean isFraud = fraudulent.isFraud();
        log.info("is fraud {}", isFraud);
        if (Boolean.TRUE.equals(isFraud))
            throw new IllegalStateException("user email  may be a Fraud");
        //todo sendNotification
        NotificationRequest notificationRequest = new NotificationRequest(customer.getId()
                , customer.getFirstName(), "user Created");
//        notificationClient.sendNotification(
//                notificationRequest
//        );
        rabbitMessageProducer.publish(notificationRequest,"internal.exchange","internal.notification.routing-key");

        System.out.println("");
    }
}
