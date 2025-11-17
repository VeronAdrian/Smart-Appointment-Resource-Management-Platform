package com.smartplatform.user.service;

public class NotificationService {
    public static void sendEmail(String to, String subject, String body) {
        System.out.println("[EMAIL] To: " + to);
        System.out.println("[EMAIL] Subject: " + subject);
        System.out.println("[EMAIL] Body: " + body);
    }

    public static void sendSMS(String to, String message) {
        System.out.println("[SMS] To: " + to);
        System.out.println("[SMS] Message: " + message);
    }

    public static void notifyAppointmentBooked(String clientEmail, String clientPhone, String staffName, String appointmentTime) {
        sendEmail(clientEmail, "Appointment Confirmed", 
            "Your appointment with " + staffName + " is confirmed for " + appointmentTime);
        if (clientPhone != null && !clientPhone.isEmpty()) {
            sendSMS(clientPhone, "Appointment confirmed: " + staffName + " at " + appointmentTime);
        }
    }

    public static void notifyAppointmentCancelled(String clientEmail, String clientPhone, String staffName, String appointmentTime) {
        sendEmail(clientEmail, "Appointment Cancelled", 
            "Your appointment with " + staffName + " on " + appointmentTime + " has been cancelled.");
        if (clientPhone != null && !clientPhone.isEmpty()) {
            sendSMS(clientPhone, "Appointment cancelled: " + staffName + " at " + appointmentTime);
        }
    }
}

