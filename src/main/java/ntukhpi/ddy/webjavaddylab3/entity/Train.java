package ntukhpi.ddy.webjavaddylab3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ntukhpi.ddy.webjavaddylab3.enums.variantRuhuConverter;
import ntukhpi.ddy.webjavaddylab3.enums.variantRuhu;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "train")
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10,unique = true)
    private String number;

    @Column(nullable = false, length = 50)
    private String pointVid;

    @Column(nullable = false, length = 50)
    private String pointDo;

    @Enumerated(EnumType.STRING)
    @Column(name = "variantRuhu", nullable = false, length = 12)
    @Convert(converter = variantRuhuConverter.class)
    private variantRuhu VariantRuhu;

    @Column(nullable = false)
    private LocalTime timeToGo;

    @Column(nullable = false)
    private LocalTime duration;

    public Train(String number, String pointVid, String pointDo, String VariantRuhu, String timeToGo, String duration){
        this.id = 0L;
        this.number = number;
        this.pointVid = pointVid;
        this.pointDo = pointDo;
        this.VariantRuhu = variantRuhu.getByVariant(VariantRuhu);
        this.timeToGo = LocalTime.parse(timeToGo, DateTimeFormatter.ofPattern("HH:mm"));
        this.duration = LocalTime.parse(duration, DateTimeFormatter.ofPattern("HH:mm"));
    }
    public Train(String number){
        this.id = 0L;
        this.number = number;
        this.pointVid = "Київський Вокзал";
        this.pointDo = "Харьківський Вокзал";
        this.VariantRuhu = variantRuhu.getVariantById(2);
        this.timeToGo = LocalTime.of(10, 55);
        this.duration = LocalTime.of(05, 10);
    }
    public String TimeToGoToSting() {
        long hours = timeToGo.getHour();
        long minutes = timeToGo.getMinute();
        return String.format("%02d:%02d", hours, minutes);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Train train = (Train) o;

        return number.equals(train.number);
    }

    @Override
    public int hashCode() {
        return number.hashCode();
    }
    public String getVRName() {
        return VariantRuhu.getDisplayName();
    }
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("" + id + ": ");
        sb.append("Номер потягу - ").append(number).append(", \n");
        sb.append("Місце відправлення: ").append(pointVid).append(", \n");
        sb.append("Місце призначення: ").append(pointDo).append(", \n");
        sb.append("Час відправлення: ").append(getTimeToGo()).append(", \n");
        sb.append("Вірант руху: ").append(VariantRuhu.getDisplayName()).append(": \n");
        sb.append("Час у дорозі: ").append(getDuration()).append(". \n").append("-------------\n");
        return sb.toString();
    }
    public LocalTime getDurationTime() {
        return duration;
    }
}
