package com.sweetlysweet.backend.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import com.sweetlysweet.backend.entity.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final Resend resend;

    public EmailService() {
        String apiKey = System.getenv("RESEND_API_KEY");

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("RESEND_API_KEY is missing!");
        }

        this.resend = new Resend(apiKey);
    }

    @Async
    public void sendOrderConfirmation(Order order, String toEmail) {

        try {

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("Sweetly Sweet <onboarding@resend.dev>")
                    .to(toEmail)
                    .subject("Order Confirmed - Sweetly Sweet #" + order.getId())
                    .text(
                            "Hi " + order.getFullName() + ",\n\n" +
                                    "Your order #" + order.getId() + " has been confirmed.\n\n" +
                                    "Total Amount: ₹" + order.getTotalAmount() + "\n\n" +
                                    "We'll notify you once your order ships.\n\n" +
                                    "Thank you for choosing Sweetly Sweet."
                    )
                    .build();

            CreateEmailResponse response = resend.emails().send(params);

            System.out.println("Email sent successfully!");
            System.out.println(response.getId());

        } catch (ResendException e) {

            System.out.println("========== ORDER EMAIL FAILED ==========");
            e.printStackTrace();

        }

    }

    @Async
    public void sendStatusUpdate(Order order, String toEmail) {

        try {

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from("Sweetly Sweet <onboarding@resend.dev>")
                    .to(toEmail)
                    .subject("Order Update - Sweetly Sweet #" + order.getId())
                    .text(
                            "Hi " + order.getFullName() + ",\n\n" +
                                    "Your order #" + order.getId() +
                                    " status has been updated to:\n\n" +
                                    order.getOrderStatus().replace("_", " ") +
                                    "\n\nThank you,\nSweetly Sweet"
                    )
                    .build();

            CreateEmailResponse response = resend.emails().send(params);

            System.out.println("Status email sent!");
            System.out.println(response.getId());

        } catch (ResendException e) {

            System.out.println("========== STATUS EMAIL FAILED ==========");
            e.printStackTrace();

        }

    }
}