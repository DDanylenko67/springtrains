package ntukhpi.ddy.webjavaddylab3;

import ntukhpi.ddy.webjavaddylab3.entity.Ticket;
import ntukhpi.ddy.webjavaddylab3.entity.Train;
import ntukhpi.ddy.webjavaddylab3.enums.variantRuhu;
import ntukhpi.ddy.webjavaddylab3.repository.TicketRepository;
import ntukhpi.ddy.webjavaddylab3.repository.TrainRepository;
import ntukhpi.ddy.webjavaddylab3.service.TicketService;
import ntukhpi.ddy.webjavaddylab3.service.TrainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class TestCRUDTicket {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private TicketService ticketService;


    @Test
    void contextLoads() {
    }

    @Test
    void showDate() {
        List<Ticket> list = ticketRepository.findAll();
        list.stream().forEach(System.out::println);
    }

    @Test
    void testFindByID() {
        assertEquals("Даниленко Денис", ticketService.getTicketById(1L).getOwner());
        assertEquals(null, ticketService.getTicketById(20L));
    }

    String nameOwner1 = "Владислав Головатюк";
    String nameOwner2 = "Владислав Ісаєнко";

    void testInsertTicketModified(String nameTrainForIns) {
        System.out.println("\nДодавання нового квитку ... ");
        try {
            Ticket trainToIns = new Ticket(trainRepository.findByNumber("KV210"), nameTrainForIns, "12345678", 1, 1, "2023-11-14");
            System.out.println("Квиток для додавання: " + trainToIns);
            Ticket trainToInsInDB = ticketService.getTicketByOwner(nameTrainForIns);
            if (trainToInsInDB == null) {
                System.out.println("Квиток із таким ключем відсутній у бд");
                ticketService.saveTicket(trainToIns);
                trainToInsInDB = ticketService.getTicketByOwner(nameTrainForIns);
                System.out.println("Квиток успішно доданий: " + trainToInsInDB);
            } else {
                System.out.println("Відхілено === Дані по Квитоку із таким ключем є у бд");
            }
            assertNotEquals(null, ticketService.getTicketByOwner(nameTrainForIns));
        } catch (Exception ex) {
            System.out.println("Помилка роботи із БД === Додавання не виконане");
        }

    }

    @Test
    void testUpdateTicket() {
        System.out.println("\nРедагування квиткуу ... ");
        try {
            Ticket emplForUpdateInDB = ticketService.getTicketByOwner(nameOwner1);
            Ticket emplToUpdateInDB = null;
            if (emplForUpdateInDB != null) {
                System.out.println("Квиток, що оновлюється: id=" + emplForUpdateInDB.getId() + ": " + emplForUpdateInDB.getOwner());
                Ticket emplToUpdate = new Ticket(trainRepository.findByNumber("KV712"), nameOwner2, "12345678", 1, 1, "2023-11-14");
                emplToUpdate.setMesto(10);
                emplToUpdate.setVagon(5);
                System.out.println("Квиток для оновлення: " + emplToUpdate);
                emplToUpdateInDB = ticketService.getTicketByOwner(nameOwner2);
                if (emplToUpdateInDB == null || (emplToUpdateInDB != null && emplToUpdateInDB.getId() == emplForUpdateInDB.getId())) {
                    System.out.println("Квиток із таким ключем відсутній у бд");
                    ticketService.updateTicket(emplForUpdateInDB.getId(), emplToUpdate);
                    emplToUpdateInDB = ticketService.getTicketByOwner(nameOwner2);
                    System.out.println("Квиток успішно оновлений: " + emplToUpdateInDB);
                } else {
                    System.out.println("Оновлення відхілене === Дані по Квитоку із таким ключем є у бд");
                }
            } else {
                System.out.println("Оновлення відхілене === Квитоку немає у БД");
            }
        } catch (Exception ex) {
            System.out.println("Помилка роботи із БД === Оновлення не виконане");
        }
    }

    @Test
    void testDeleteTicket() {
        System.out.println("\nВилучення потягу ... ");
        try {
            Ticket emplForDeleteInDB = ticketService.getTicketByOwner(nameOwner2);
            if (emplForDeleteInDB != null) {
                System.out.println("Квиток, що вилучається: id=" +
                        emplForDeleteInDB.getId() + ": " + emplForDeleteInDB.getOwner());
                ticketService.deleteTicketById(emplForDeleteInDB.getId());
                assertEquals(null, ticketService.getTicketByOwner(nameOwner2));
                System.out.println("Квиток успішно вилучений!");
            } else {
                System.out.println("Вилучення відхілене === Квитоку немає у БД");
            }
        } catch (Exception ex) {
            System.out.println("Помилка роботи із БД === Вилучення не виконане");
        }
    }

    @Test
    void insertAndUpdate() {
        testDeleteTicket();
        testUpdateTicket();
    }


    @Test
    void insertAndUpdateAndDelete() {
        testInsertTicketModified(nameOwner1);
        testUpdateTicket();
        testDeleteTicket();
    }
}
