package ntukhpi.ddy.webjavaddylab3.controller;

import ntukhpi.ddy.webjavaddylab3.entity.Ticket;
import ntukhpi.ddy.webjavaddylab3.entity.Train;
import ntukhpi.ddy.webjavaddylab3.enums.variantRuhu;
import ntukhpi.ddy.webjavaddylab3.service.TicketService;
import ntukhpi.ddy.webjavaddylab3.service.TrainService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TicketController {
    private final String TICKET_TEXT_INS = "Новий квиток";
    private final String TICKET_TEXT_EDIT = "Редагування квитку";
    private final String TICKET_EMPTY = "Немає жодного білета на цей потяг.";
    private final String TRAIN_GONE = "Не можлово замовити квиток на потяг, бо потяг вже поїхав.";
    private final TrainService trainService;
    private final TicketService ticketService;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String minDate = LocalDate.now().format(formatter);
    String maxDate = LocalDate.now().plusMonths(2).format(formatter);

    public TicketController(TrainService trainService, TicketService ticketService){
        this.trainService = trainService;
        this.ticketService = ticketService;
    }
    @GetMapping("/trains/tickets/{idTrain}")
    public String showTicketPage(@PathVariable Long idTrain, Model model){
        List<Ticket> list = ticketService.getAllTickets();
        List<Ticket> temp = new ArrayList<>();
        for (Ticket ticket : list) {
            if(ticket.getTrain().getId() == idTrain){
                temp.add(ticket);
            }
        }
        if(!temp.isEmpty()){
            model.addAttribute("tickets", temp);
            return "/tickets/tickets";
        }
        model.addAttribute("title", TICKET_EMPTY);
        model.addAttribute("trains", trainService.getAllTrains());
        return "/trains/trains";
    }

    @GetMapping("/trains/ticket/{idTrain}")
    public String createTicketForm(@PathVariable Long idTrain, Model model) {
        Ticket newTicket = new Ticket(trainService.getTrainById(idTrain), "", "", 1, 1, LocalDate.now().format(formatter));
        model.addAttribute("ticket", newTicket);
        model.addAttribute("titleTicket", TICKET_TEXT_INS);
        model.addAttribute("errorString", null);
        model.addAttribute("minDate", minDate);
        model.addAttribute("maxDate", maxDate);
        return "/tickets/ticket";
    }
    @GetMapping("/trains/tickets/edit/{idEdit}")
    public String editTicketForm(@PathVariable Long idEdit, Model model) {
        Ticket ticketForUpdateInDb = ticketService.getTicketById(idEdit);
        model.addAttribute("ticket", ticketForUpdateInDb);
        System.out.println(ticketForUpdateInDb);
        model.addAttribute("titleTicket", TICKET_TEXT_EDIT);
        model.addAttribute("errorString", null);
        model.addAttribute("minDate", minDate);
        model.addAttribute("maxDate", maxDate);
        return "/tickets/ticket";
    }

    @PostMapping("/tickets/save/{id}")
    public String saveOrUpdateTicket(@PathVariable Long id,
                                    @ModelAttribute("ticket") Ticket ticketSave, @RequestParam("trainId") Long trainId,
                                    Model model) {

        Train train = trainService.getTrainById(trainId);
        ticketSave.setTrain(train);
        System.out.println("_-------------------------------------");
        System.out.println(ticketSave.getTrain());
        System.out.println("_-------------------------------------");
        if (ticketSave.getTrain().getTimeToGo().isBefore(LocalTime.now()) && ticketSave.getTimeToGo().isEqual(LocalDate.now())){
            model.addAttribute("errorString", TRAIN_GONE);
            model.addAttribute("ticket", ticketSave);
            model.addAttribute("titleTicket", TICKET_TEXT_EDIT);
            return "/tickets/ticket";
        }
        Ticket ticketToSaveInDb = ticketService.getTicketByData(ticketSave.getTimeToGo(), ticketSave.getMesto(), ticketSave.getVagon());
        if(ticketSave.getTrain().getVariantRuhu().getDisplayName().equals(variantRuhu.paired.getDisplayName())) {
            if (ticketSave.getTimeToGo().getDayOfMonth() % 2 != 0) {
                model.addAttribute("errorString", "Це поїзд їздить тіль по парних днях.\n" +
                        "Змініть дані\n");
                model.addAttribute("ticket", ticketSave);
                model.addAttribute("titleTicket", TICKET_TEXT_EDIT);
                return "/tickets/ticket";
            }
        }
        if (ticketSave.getTrain().getVariantRuhu().getDisplayName().equals(variantRuhu.unpaired.getDisplayName())) {
            if (ticketSave.getTimeToGo().getDayOfMonth() % 2 == 0) {
                model.addAttribute("errorString", "Це поїзд їздить тіль по непарних днях.\n" +
                        "Змініть дані\n");
                model.addAttribute("ticket", ticketSave);
                model.addAttribute("titleTicket", TICKET_TEXT_EDIT);
                return "/tickets/ticket";
            }
        }
        if (id.equals(0L)) {
            if (ticketToSaveInDb == null) {
                ticketService.saveTicket(ticketSave);
                return "redirect:/trains";
            } else {
                model.addAttribute("ticket", ticketSave);
                model.addAttribute("titleTicket", TICKET_TEXT_INS);
                model.addAttribute("errorString", "Це місце вже зарезервоване!");
                return "/tickets/ticket";
            }
        } else {
            if ( ticketToSaveInDb == null || (ticketToSaveInDb!= null && ticketToSaveInDb.getId() == ticketSave.getId())) {
                Ticket existingTicket = ticketService.getTicketById(id);
                if (existingTicket == null) {
                    model.addAttribute("ticket", ticketSave);
                    model.addAttribute("titleTicket", TICKET_TEXT_EDIT);
                    model.addAttribute("errorString", "Такого квитка немає в базі даних!!");
                    return "/tickets/ticket";
                } else {
                    ticketService.updateTicket(existingTicket.getId(), ticketSave);
                    return "redirect:/trains";
                }
            } else {
                model.addAttribute("ticket", ticketSave);
                model.addAttribute("titleTicket", TICKET_TEXT_EDIT);
                model.addAttribute("errorString", "Квиток з такими даними вже зарезервований!");
                return "/tickets/ticket";
            }
        }
    }
    @GetMapping("/trains/ticket/del/{idTicketForDelete}")
    public String deleteTicket(@PathVariable Long idTicketForDelete, Model model) {
        String messageDeleteError = "";
        Ticket delTicketInDb = ticketService.getTicketById(idTicketForDelete);
        if (delTicketInDb != null) {
            StringBuilder sbMessageAboutPresent = new StringBuilder();
            if (sbMessageAboutPresent.toString().isEmpty()) {
                ticketService.deleteTicketById(idTicketForDelete);
            } else {
                messageDeleteError = "Object: TICKET, id=" + idTicketForDelete + sbMessageAboutPresent;
            }
        } else {
            messageDeleteError = "Object: TICKEt, id=" + idTicketForDelete
                    + "<br><br>Такого квитка немає у БД!";
        }
        List<Ticket> list = ticketService.getAllTickets();
        List<Ticket> temp = new ArrayList<>();
        for (Ticket ticket : list) {
            if(ticket.getTrain().getId() == delTicketInDb.getTrain().getId()){
                temp.add(ticket);
            }
        }
        model.addAttribute("trains", trainService.getAllTrains());
        if (!messageDeleteError.isEmpty()) {
            model.addAttribute("error_del_message", messageDeleteError);
            model.addAttribute("ret_page", "/trains");
            return "crud_error";
        } else if(!temp.isEmpty()) {
            model.addAttribute("tickets", temp);
            return "/tickets/tickets";
        }
        return "redirect:/trains";
    }
}
