package ntukhpi.ddy.webjavaddylab3.controller;

import ntukhpi.ddy.webjavaddylab3.entity.EmailDetails;
import ntukhpi.ddy.webjavaddylab3.entity.Ticket;
import ntukhpi.ddy.webjavaddylab3.entity.Train;
import ntukhpi.ddy.webjavaddylab3.service.EmailService;
import ntukhpi.ddy.webjavaddylab3.service.TicketService;
import ntukhpi.ddy.webjavaddylab3.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
public class EmailController {
    @Autowired
    private EmailService emailService;
    private final TrainService trainService;
    private final TicketService ticketService;
    @Autowired
    public EmailController(TrainService trainService, TicketService ticketService) {
        this.trainService = trainService;
        this.ticketService = ticketService;
    }


    @PostMapping("/sendMail")
    public String sendMail(@RequestParam(name = "recipient") String recipient)
    {
        EmailDetails details = new EmailDetails();
        details.setRecipient(recipient);
        details.setSubject("Звіт");
        details.setMsgBody("Звіт про потяги");
        String status
                = emailService.sendSimpleMail(details);
        System.out.println(status);
        return "redirect:/trains";
    }

    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestParam(name = "recipient") String recipient)
    {
        EmailDetails details = new EmailDetails();
        details.setRecipient(recipient);
        details.setSubject("Звіт");
        details.setMsgBody("Звіт про потяги.");
        details.setAttachment(saveToFile());
        String status
                = emailService.sendMailWithAttachment(details);
        System.out.println(status);
        return "redirect:/trains";
    }

    public String saveToFile() {
        String path = "C:\\Users\\deadt\\IdeaProjects\\WebJavaDDYLab3\\src\\main\\resources\\report.html";
        try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(path), "UTF-8"))) {
            writeHeader(writer, "HTML-Звіт", new Date(),  "Звіт, сформований на базі лабораторної роботи номер 5:");
            writeComponents(writer, trainService.getAllTrains(), ticketService.getAllTickets());
            writeFooter(writer, "Даниленко Денис", "КН-221В");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
    private void writeHeader(PrintWriter writer, String title, Date date, String otherInfo) {
        writer.println("<html>");
        writer.println("<head><title>" + title + "</title> ");
        writer.println("<style type=\"text/css\">\n" +
                "    content {\n" +
                "        max-width: 1000px;\n" +
                "        margin: auto;\n" +
                "    }\n" +
                "\n" +
                "    TABLE {\n" +
                "        width: 100%; /* Ширина таблицы */\n" +
                "        border: 2px solid black; /* Рамка вокруг таблицы */\n" +
                "    }\n" +
                "\n" +
                "    TD, TH {\n" +
                "        padding: 3px; /* Поля вокруг содержимого ячеек */\n" +
                "        border: 2px solid black; /* Рамка вокруг таблицы */\n" +
                "    }\n" +
                "\n" +
                "    TH {\n" +
                "        text-align: left; /* Выравнивание по левому краю */\n" +
                "        background: bisque; /* Цвет фона */\n" +
                "        color: blue; /* Цвет текста */\n" +
                "    }\n" +
                "</style>");
        writer.println("</head>");
        writer.println("<body>");
        writer.println("<h1>" + title + "</h1>");
        writer.println("<p>Дата: " + new SimpleDateFormat("dd-MM-yyyy").format(date) + "</p>");
        writer.println("<p>" + otherInfo + "</p>");
    }

    private void writeFooter(PrintWriter writer, String name, String group) {
        writer.println("</table>");
        writer.println("<p>Виконав: " + name + "</p>");
        writer.println("<p>Група: " + group + "</p>");
        writer.println("<p>НТУ ХПИ</p>");
        writer.println("</body>");
        writer.println("</html>");
    }

    public void writeComponents(PrintWriter writer, List<Train> trainList, List<Ticket> ticketList) {
        int temp = 0;
        writer.println("<table>" +
                "    <tr>" +
                "        <th>Номер</th>\n" +
                "        <th>Місце відправки</th>\n" +
                "        <th>Місце прибуття</th>\n" +
                "        <th>Варіант руху</th>\n" +
                "        <th>Час відправки</th>\n" +
                "        <th>Час у дорозі</th>\n" +
                "    </tr>");

        for (Train train : trainList) {
            writer.println(" <tr style=\"color: #008000\t\">" +
                    "        <td>" + train.getNumber() + "</td>\n" +
                    "        <td>" + train.getPointVid() + "</td>\n" +
                    "        <td>" + train.getPointDo() + "</td>\n" +
                    "        <td>" + train.getVariantRuhu().getDisplayName() + "</td>\n" +
                    "        <td>" + train.getTimeToGo() + "</td>\n" +
                    "        <td>" + train.getDuration() + "</td>\n" +
                    "        </tr>");

            for (Ticket ticket : ticketList) {
                if (ticket.getTrain().getId() == train.getId()) {
                    if (temp == 0) {
                        writer.println("    <tr>\n" +
                                "        <th>Дата відправки</th>\n" +
                                "        <th>Час відправки</th>\n" +
                                "        <th>Власник</th>\n" +
                                "        <th>Номер паспорту</th>\n" +
                                "        <th>Вагон</th>\n" +
                                "        <th>Місце</th>\n" +
                                "    </tr>\n");
                        temp++;
                    }
                    writer.println("<tr style=\"color: #FFA500\">" +
                            "        <td>" + ticket.getTimeToGo() + "</td>\n" +
                            "        <td>" + ticket.getTrain().getTimeToGo() + "</td>\n" +
                            "        <td>" + ticket.getOwner() + "</td>\n" +
                            "        <td>" + ticket.getPassport() + "</td>\n" +
                            "        <td>" + ticket.getVagon() + "</td>\n" +
                            "        <td>" + ticket.getMesto() + "</td>\n" +
                            "        </tr>");
                }
            }
            temp = 0;
        }

        writer.println("</table>");
    }
}