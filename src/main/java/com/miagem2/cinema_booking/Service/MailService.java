package com.miagem2.cinema_booking.Service;
import com.miagem2.cinema_booking.Model.Ticket;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.SendEmailRequest;
import com.resend.services.emails.model.SendEmailResponse;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MailService {
    private Resend resend;

    public final String email_test = "remycastro27@icloud.com";

    public MailService() {
        this.resend = new Resend("re_Z1SqLyGv_3LA34o3U7px6Yk1t2wvPRM7H");
    }

    public void sendEmailWithTickets(String email, List<Ticket> tickets) {
        // In prod: use email parameter

        SendEmailRequest sendEmailRequest = SendEmailRequest.builder()
                .from("Instant ciné <onboarding@resend.dev>")
                .to(email_test)
                .subject("Billets Instant Ciné")
                .html(buildEmailContent(tickets))
                .build();

        try {
            SendEmailResponse data = resend.emails().send(sendEmailRequest);
        } catch (ResendException e) {
            e.printStackTrace();
        }
    }

    private String buildEmailContent(List<Ticket> tickets) {
        StringBuilder content = new StringBuilder();
        float totalAmount = 0.0f;

        content.append("<h1>Nous vous confirmons la réservation de ").append(tickets.size()).append(" billets de cinéma</h1>");
        content.append("<p>Voici vos places :</p>");

        content.append("<ul>");
        for (Ticket ticket : tickets) {
            content.append("<li>").append(ticket.getSeat()).append("</li>");
            totalAmount += ticket.getAmount();
        }
        content.append("</ul>");

        content.append("<p>Montant total : ").append(totalAmount).append(" €</p>");
        return content.toString();
    }

}
