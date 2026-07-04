package com.ems.service;

import com.ems.model.Employee;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(Employee employee) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(employee.getEmail());
            message.setSubject("Welcome to the Company, " + employee.getFirstName() + "!");
            message.setText(
                "Dear " + employee.getFirstName() + " " + employee.getLastName() + ",\n\n" +
                "Welcome to the team! We are thrilled to have you with us.\n\n" +
                "Here are your employment details:\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                "  Department : " + employee.getDepartment() + "\n" +
                "  Position   : " + employee.getPosition() + "\n" +
                "  Join Date  : " + (employee.getJoinDate() != null ? employee.getJoinDate() : "To be confirmed") + "\n" +
                "  Status     : " + employee.getStatus() + "\n" +
                "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                "Please reach out to HR if you have any questions.\n\n" +
                "We look forward to working with you!\n\n" +
                "Warm regards,\n" +
                "HR Team\n" +
                "Employee Management System"
            );

            mailSender.send(message);
            System.out.println("✅ Welcome email sent to: " + employee.getEmail());

        } catch (Exception e) {
            System.out.println("⚠️ Email could not be sent: " + e.getMessage());
        }
    }
}