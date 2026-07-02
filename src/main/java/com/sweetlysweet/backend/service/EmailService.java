package com.sweetlysweet.backend.service;

import com.sweetlysweet.backend.entity.Order;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendOrderConfirmation(Order order, String toEmail) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();

            msg.setFrom("halogenationplus1@gmail.com");
            msg.setTo(toEmail);

            msg.setSubject(
                    "Order Confirmed — Sweetly Sweet #" + order.getId()
            );

            msg.setText(
                    "Hi " + order.getFullName() + ",\n\n" +
                            "Your order #" + order.getId() + " has been confirmed.\n\n" +
                            "Total Amount: ₹" + order.getTotalAmount() + "\n\n" +
                            "We'll notify you once your order ships.\n\n" +
                            "Thank you for choosing Sweetly Sweet 🍫"
            );

            mailSender.send(msg);

            System.out.println(
                    "Order confirmation email sent to: " + toEmail
            );

        } catch (Exception e) {
            System.out.println("Order email failed:");
            e.printStackTrace();
        }
    }

    @Async
    public void sendStatusUpdate(Order order, String toEmail) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();

            msg.setFrom("halogenationplus1@gmail.com");
            msg.setTo(toEmail);

            msg.setSubject(
                    "Order Update — Sweetly Sweet #" + order.getId()
            );

            msg.setText(
                    "Hi " + order.getFullName() + ",\n\n" +
                            "Your order #" + order.getId() +
                            " status has been updated to:\n\n" +
                            order.getOrderStatus().replace("_", " ") +
                            "\n\nThank you,\nSweetly Sweet 🍫"
            );

            mailSender.send(msg);

            System.out.println(
                    "Status update email sent to: " + toEmail
            );

        } catch (Exception e) {
            System.out.println("Status email failed:");
            e.printStackTrace();
        }
    }
}