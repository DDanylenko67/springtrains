package ntukhpi.ddy.webjavaddylab3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "train_id", nullable = false)
    private Train train;
    private String owner;
    @Column(nullable = false, length = 10)
    private String passport;
    @Column(nullable = false)
    private int vagon;
    @Column(nullable = false)
    private int mesto;

    @Column(nullable = false)

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate timeToGo;
    public Ticket(Train train, String owner, String passport, int vagon, int mesto, String timeToGo){
            if(vagon == 0 || mesto == 0){
                System.out.println("Не може бути нульового вагону або місця");
            }else{
                this.id = 0L;
                this.train = train;
                this.owner = owner;
                this.passport = passport;
                this.vagon = vagon;
                this.mesto = mesto;
                this.timeToGo = LocalDate.parse(timeToGo, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            }
    }
    public String TimeToGoTo() {
        long day = timeToGo.getDayOfMonth();
        long month = timeToGo.getMonthValue();
        long year = timeToGo.getYear();
        return String.format("%02d.%02d.%04d", day, month, year);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("" + id + ": ");
        sb.append(owner).append(" - ");
        sb.append(train.getNumber()).append(", ");
        sb.append(passport).append(", ");
        sb.append(vagon).append(", ");
        sb.append(mesto).append(", ");
        sb.append(TimeToGoTo());
        return sb.toString();
    }

}
