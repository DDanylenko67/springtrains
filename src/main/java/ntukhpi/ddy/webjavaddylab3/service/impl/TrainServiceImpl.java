package ntukhpi.ddy.webjavaddylab3.service.impl;

import ntukhpi.ddy.webjavaddylab3.entity.Train;
import ntukhpi.ddy.webjavaddylab3.repository.TrainRepository;
import ntukhpi.ddy.webjavaddylab3.service.TrainService;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class TrainServiceImpl implements TrainService {
    private final TrainRepository trainRepository;
    public TrainServiceImpl (TrainRepository trainRepository){
        super();
        this.trainRepository = trainRepository;
    }

    @Override
    public List<Train> getAllTrains() {
        return trainRepository.findAll();
    }

    @Override
    public Train saveTrain(Train train) {
        return trainRepository.save(train);
    }

    @Override
    public Train getTrainById(Long id) {
        return trainRepository.findById(id).orElse(null);
    }

    @Override
    public Train updateTrain(Long id, Train train) {
        train.setId(id);
        return trainRepository.save(train);
    }
    @Override
    public void deleteTrainById(Long id) {
        trainRepository.deleteById(id);
    }

    @Override
    public Train getTrainByNumber(String number) {
        return trainRepository.findByNumber(number);
    }
}
