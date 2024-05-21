package ntukhpi.ddy.webjavaddylab3;

import ntukhpi.ddy.webjavaddylab3.entity.Train;
import ntukhpi.ddy.webjavaddylab3.enums.variantRuhu;
import ntukhpi.ddy.webjavaddylab3.repository.TrainRepository;
import ntukhpi.ddy.webjavaddylab3.service.TrainService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
class TestCRUDTrain {
    @Autowired
    private TrainRepository trainRepository;
    @Autowired
    private TrainService trainService;


    @Test
    void contextLoads() {
    }
    @Test
    void showDate(){
        List<Train> list = trainRepository.findAll();
        list.stream().forEach(System.out::println);
        System.out.println("=====================");
        list = trainService.getAllTrains();
        list.stream().forEach(System.out::println);
    }
    @Test
    void testFindByID() {
        assertEquals("KV712", trainService.getTrainById(3L).getNumber());
        assertEquals(null, trainService.getTrainById(7L));
    }

    @Test
    void testFindByName() {
        assertEquals(3L, trainService.getTrainByNumber("KV712").getId());
        assertEquals(null, trainService.getTrainByNumber("LS112"));
    }

    String nameTrain1 = "KV100";
    String nameTrain2 = "KV200";
    void testInsertTrainModified(String nameTrainForIns) {
        System.out.println("\nДодавання нового потягу ... ");
        try {
            Train trainToIns = new Train(nameTrainForIns);
            System.out.println("Потяг для додавання: " + trainToIns);
            Train trainToInsInDB = trainService.getTrainByNumber(nameTrainForIns);
            if (trainToInsInDB == null) {
                System.out.println("Потяг із таким ключем відсутній у бд");
                trainService.saveTrain(trainToIns);
                trainToInsInDB = trainService.getTrainByNumber(nameTrainForIns);
                System.out.println("Потяг успішно доданий: " + trainToInsInDB);
            } else {
                System.out.println("Відхілено === Дані по Потягу із таким ключем є у бд");
            }
            assertNotEquals(null, trainService.getTrainByNumber(nameTrainForIns));
        } catch (Exception ex) {
            System.out.println("Помилка роботи із БД === Додавання не виконане");
        }

    }

    @Test
    void testUpdateTrain() {
        System.out.println("\nРедагування потягу ... ");
        try {
            Train emplForUpdateInDB = trainService.getTrainByNumber(nameTrain1);
            Train emplToUpdateInDB = null;
            if (emplForUpdateInDB != null) {
                System.out.println("Потяг, що оновлюється: id=" + emplForUpdateInDB.getId() + ": " + emplForUpdateInDB.getNumber());
                Train emplToUpdate = new Train(nameTrain2);
                emplToUpdate.setPointDo("Одеський вокзал");;
                emplToUpdate.setVariantRuhu(variantRuhu.daily);
                System.out.println("Потяг для оновлення: " + emplToUpdate);
                emplToUpdateInDB = trainService.getTrainByNumber(nameTrain2);
                if (emplToUpdateInDB == null || (emplToUpdateInDB != null && emplToUpdateInDB.getId() == emplForUpdateInDB.getId())) {
                    System.out.println("Потяг із таким ключем відсутній у бд");
                    trainService.updateTrain(emplForUpdateInDB.getId(), emplToUpdate);
                    emplToUpdateInDB = trainService.getTrainByNumber(nameTrain2);
                    System.out.println("Потяг успішно оновлений: " + emplToUpdateInDB);
                } else {
                    System.out.println("Оновлення відхілене === Дані по потягуу із таким ключем є у бд");
                }
            } else {
                System.out.println("Оновлення відхілене === Потягу немає у БД");
            }
        } catch (Exception ex) {
            System.out.println("Помилка роботи із БД === Оновлення не виконане");
        }
        assertNotEquals(null, trainService.getTrainByNumber(nameTrain2));

    }

    @Test
    void testDeleteTrain() {
        System.out.println("\nВилучення потягу ... ");
        try {
            Train emplForDeleteInDB = trainService.getTrainByNumber(nameTrain2);
            if (emplForDeleteInDB != null) {
                System.out.println("Потяг, що вилучається: id=" +
                        emplForDeleteInDB.getId() + ": " + emplForDeleteInDB.getNumber());
                trainService.deleteTrainById(emplForDeleteInDB.getId());
                assertEquals(null, trainService.getTrainByNumber(nameTrain2));
                System.out.println("Потяг успішно вилучений!");
            } else {
                System.out.println("Вилучення відхілене === Потяг немає у БД");
            }
        } catch (Exception ex) {
            System.out.println("Помилка роботи із БД === Вилучення не виконане");
        }
    }

    @Test
    void insertAndUpdate() {
        testDeleteTrain();
        testUpdateTrain();
    }


    @Test
    void insertAndUpdateAndDelete() {
        testInsertTrainModified(nameTrain1);
        testUpdateTrain();
        testDeleteTrain();
    }



}
