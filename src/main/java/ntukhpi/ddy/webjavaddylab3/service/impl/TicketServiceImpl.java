package ntukhpi.ddy.webjavaddylab3.service.impl;

import ntukhpi.ddy.webjavaddylab3.entity.Ticket;
import ntukhpi.ddy.webjavaddylab3.entity.Train;
import ntukhpi.ddy.webjavaddylab3.repository.TicketRepository;
import ntukhpi.ddy.webjavaddylab3.repository.TrainRepository;
import ntukhpi.ddy.webjavaddylab3.service.TicketService;
import org.springframework.stereotype.Service;

import javax.swing.plaf.InsetsUIResource;
import java.time.LocalDate;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TrainRepository trainRepository;
    public TicketServiceImpl (TicketRepository ticketRepository, TrainRepository trainRepository){
        super();
        this.trainRepository = trainRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id).orElse(null);
    }

    @Override
    public Ticket updateTicket(Long id, Ticket ticket) {
       ticket.setId(id);
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicketById(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public Ticket getTicketByData(LocalDate localDate, int mesto, int vagon) {
        return ticketRepository.findByTimeToGoAndVagonAndMesto(localDate, mesto, vagon);
    }

    @Override
    public Ticket getTicketByOwner(String name) {
        return ticketRepository.findByOwner(name);
    }

    @Override
    public String getInfoPresenceTicketByIDTrain(Long idTrain){
        List<Ticket> list =  ticketRepository.findTicketsByTrainId(idTrain);
        return list.isEmpty()?"":"Є дані про квиток на потяг у БД!";
    }
}