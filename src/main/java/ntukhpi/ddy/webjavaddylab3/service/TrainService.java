package ntukhpi.ddy.webjavaddylab3.service;

import ntukhpi.ddy.webjavaddylab3.entity.Train;

import java.util.List;

public interface TrainService {
    List<Train> getAllTrains();
    Train saveTrain(Train train);
    Train getTrainById(Long id);
    Train updateTrain(Long id, Train train);
    void deleteTrainById(Long id);
    Train getTrainByNumber(String number);
}
