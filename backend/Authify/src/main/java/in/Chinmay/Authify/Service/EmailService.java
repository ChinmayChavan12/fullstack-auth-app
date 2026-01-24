package in.Chinmay.Authify.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String fromEmail;

    public void sendWelcomeEmail(String toEmail,String name ) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Welcome to our Platform "+name);
        message.setText("Hello "+name+",\n\nThanks for registering with us!\n\nRegards,\nAuthify Team");
        mailSender.send(message);
    }

    public void sendResetOtp(String toEmail ,String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Password Reset Otp");
        message.setText("Your Opt for resetting your Password is: "+otp);
        mailSender.send(message);
    }

    public void sendVerifyOtp(String toEmail,String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject("Verify OTP");
        message.setText("Your Opt for verifying your Email is: "+otp);
        mailSender.send(message);
    }

    public void sendAccountVerifiedMail(String email) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(email);
        message.setSubject("Account Verified");
        message.setText("Your Account has been verified");
        mailSender.send(message);
    }
}
