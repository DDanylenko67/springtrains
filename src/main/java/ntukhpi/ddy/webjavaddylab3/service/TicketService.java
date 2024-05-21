package ntukhpi.ddy.webjavaddylab3.service;

import ntukhpi.ddy.webjavaddylab3.entity.Ticket;

import java.time.LocalDate;
import java.util.List;

public interface TicketService {
    List<Ticket> getAllTickets();
    Ticket saveTicket(Ticket ticket);
    Ticket getTicketById(Long id);
    Ticket updateTicket(Long id, Ticket ticket);
    void deleteTicketById(Long id);
    Ticket getTicketByData(LocalDate localDate, int mesto, int vagon);
    Ticket getTicketByOwner(String name);
    String getInfoPresenceTicketByIDTrain(Long idTrain);
}
