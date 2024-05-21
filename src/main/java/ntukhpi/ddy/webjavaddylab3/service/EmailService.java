package ntukhpi.ddy.webjavaddylab3.service;

import ntukhpi.ddy.webjavaddylab3.entity.EmailDetails;

public interface EmailService {

    String sendSimpleMail(EmailDetails details);
    String sendMailWithAttachment(EmailDetails details);
}