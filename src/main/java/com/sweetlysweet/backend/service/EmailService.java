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
            msg.setTo(toEmail);
            msg.setSubject("Order Confirmed — Sweetly Sweet #" + order.getId());
            msg.setText(
                    "Hi " + order.getFullName() + ",\n\n" +
                            "Your order #" + order.getId() + " has been confirmed!\n" +
                            "Total: ₹" + order.getTotalAmount() + "\n\n" +
                            "We'll notify you once it ships.\n\n" +
                            "Thanks for choosing Sweetly Sweet 🍫"
            );
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("Email failed: " + e.getMessage());
        }
    }

    @Async
    public void sendStatusUpdate(Order order, String toEmail) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setSubject("Order Update — Sweetly Sweet #" + order.getId());
            msg.setText(
                    "Hi,\n\nYour order #" + order.getId() +
                            " status is now: " + order.getOrderStatus().replace("_", " ") +
                            "\n\nThanks,\nSweetly Sweet 🍫"
            );
            mailSender.send(msg);
        } catch (Exception e) {
            System.err.println("Email failed: " + e.getMessage());
        }
    }
}