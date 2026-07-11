package com.ems.service;

import com.ems.model.Employee;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendWelcomeEmail(Employee employee) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setFrom("gspriya147@gmail.com", "EMS Portal");
            helper.setTo(employee.getEmail());
            helper.setSubject("Welcome to the Company, " + employee.getFirstName() + "!");
            helper.setText(
                    "Dear " + employee.getFirstName() + " " + employee.getLastName() + ",\n\n" +
                            "Welcome to the team! We are thrilled to have you with us.\n\n" +
                            "Here are your employment details:\n" +
                            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n" +
                            "  Department : " + employee.getDepartment() + "\n" +
                            "  Position   : " + employee.getPosition() + "\n" +
                            "  Join Date  : "
                            + (employee.getJoinDate() != null ? employee.getJoinDate() : "To be confirmed") + "\n" +
                            "  Status     : " + employee.getStatus() + "\n" +
                            "━━━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n" +
                            "Please reach out to HR if you have any questions.\n\n" +
                            "We look forward to working with you!\n\n" +
                            "Warm regards,\n" +
                            "EMS Portal\n" +
                            "HR Team");

            mailSender.send(mimeMessage);
            System.out.println("✅ Welcome email sent to: " + employee.getEmail());

        } catch (Exception e) {
            System.out.println("⚠️ Email could not be sent: " + e.getMessage());
        }
    }
}