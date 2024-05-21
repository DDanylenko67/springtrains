package ntukhpi.ddy.webjavaddylab3.repository;

import ntukhpi.ddy.webjavaddylab3.entity.Train;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface TrainRepository extends JpaRepository<Train, Long> {

    Train findByNumber(String number);
}
