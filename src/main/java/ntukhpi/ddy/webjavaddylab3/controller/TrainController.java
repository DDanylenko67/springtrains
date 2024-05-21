package ntukhpi.ddy.webjavaddylab3.controller;

import ntukhpi.ddy.webjavaddylab3.entity.Train;
import ntukhpi.ddy.webjavaddylab3.service.TicketService;
import ntukhpi.ddy.webjavaddylab3.service.TrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ntukhpi.ddy.webjavaddylab3.enums.variantRuhu;

import java.time.LocalTime;
import java.util.Collection;

@Controller
public class TrainController {
    private final TrainService trainService;
    private final TicketService ticketService;
    @Autowired
    public TrainController(TrainService trainService, TicketService ticketService) {
        this.trainService = trainService;
        this.ticketService = ticketService;
    }

    @GetMapping("/trains")
    public String listTrains(Model model) {
        attributes(model);
        model.addAttribute("trains", trainService.getAllTrains());
        return "/trains/trains";
    }
    private final String TRAIN_TEXT_INS = "Новий потяг";
    private final String TRAIN_TEXT_EDIT = "Редагування потягу";



    @GetMapping("/trains/new")
    public String createTrainForm(Model model) {
        attributes(model);
        Train newTrain = new Train("", "", "",
                variantRuhu.daily.toString(), "00:00", "00:00");
        model.addAttribute("train", newTrain);
        model.addAttribute("titleTrain", TRAIN_TEXT_INS);
        model.addAttribute("errorString", null);
        return "/trains/train";
    }


    @GetMapping("/trains/edit/{idEdit}")
    public String editTrainForm(@PathVariable Long idEdit, Model model) {
        attributes(model);
        Train trainForUpdateInDB = trainService.getTrainById(idEdit);
        model.addAttribute("train", trainForUpdateInDB);
        System.out.println(trainForUpdateInDB);
        model.addAttribute("titleTrain", TRAIN_TEXT_EDIT);
        model.addAttribute("errorString", null);
        return "/trains/train";
    }

    @PostMapping("/trains/save/{id}")
    public String saveOrUpdateTrain(@PathVariable Long id,
                                       @ModelAttribute("train") Train trainToSave,
                                       Model model) {
        Train trainToSaveInDB = trainService.getTrainByNumber(trainToSave.getNumber());
        attributes(model);
        if (id.equals(0L)) {
            if (trainToSaveInDB == null) {
                trainService.saveTrain(trainToSave);
                return "redirect:/trains";
            } else {
                model.addAttribute("train", trainToSave);
                model.addAttribute("titleTrain", TRAIN_TEXT_INS);
                model.addAttribute("errorString", "Train with such key was found in DB!");
                return "/trains/train";
            }
        } else {
            if ( trainToSaveInDB == null || (trainToSaveInDB != null && trainToSaveInDB.getId() == trainToSave.getId())) {
                Train existingTrain = trainService.getTrainById(id);
                if (existingTrain == null) {
                    model.addAttribute("train", trainToSave);
                    model.addAttribute("titleTrain", TRAIN_TEXT_EDIT);
                    model.addAttribute("errorString", "Train for update was not found in DB!");
                    return "/trains/train";
                } else {
                    trainService.updateTrain(existingTrain.getId(), trainToSave);
                    return "redirect:/trains";
                }
            } else {
                model.addAttribute("train", trainToSave);
                model.addAttribute("titleTrain", TRAIN_TEXT_EDIT);
                model.addAttribute("errorString", "Train with such key was found in DB!");
                return "/trains/train";
            }
        }
    }

    // handler method to handle delete Employee request
    @GetMapping("/trains/del/{idTrainForDelete}")
    public String deleteTrain(@PathVariable Long idTrainForDelete, Model model) {
        attributes(model);
        String messageDeleteError = "";

        Train delTrainInDB = trainService.getTrainById(idTrainForDelete);
        if (delTrainInDB != null) {
            StringBuilder sbMessageAboutPresent = new StringBuilder();
            String ticketPresent = ticketService.getInfoPresenceTicketByIDTrain(idTrainForDelete);
            if (!ticketPresent.isEmpty()) {
                sbMessageAboutPresent.append("<br><br>").append(ticketPresent);
            }
            if (sbMessageAboutPresent.toString().isEmpty()) {
                trainService.deleteTrainById(idTrainForDelete);
            } else {
                messageDeleteError = "Object: TRAIN, id=" + idTrainForDelete + sbMessageAboutPresent;
            }
        } else {
            messageDeleteError = "Object: TRAIN, id=" + idTrainForDelete
                    + "<br><br>Такого потяга немає у БД!";
        }
        if (!messageDeleteError.isEmpty()) {
            model.addAttribute("error_del_message", messageDeleteError);
            model.addAttribute("ret_page", "/trains");
            return "crud_error";
        } else {
            return "redirect:/trains";
        }

    }
    public void attributes(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String username = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
            model.addAttribute("username", username);
            model.addAttribute("roles", authorities);
        }
    }
}
