package ntukhpi.ddy.webjavaddylab3.repository;

import ntukhpi.ddy.webjavaddylab3.entity.Ticket;
import ntukhpi.ddy.webjavaddylab3.entity.Train;
import ntukhpi.ddy.webjavaddylab3.service.TicketService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Ticket findByTimeToGoAndVagonAndMesto(LocalDate date, int vagon, int mesto);
    Ticket findByOwner(String name);
    List<Ticket> findTicketsByTrainId(Long idTrain);
}
