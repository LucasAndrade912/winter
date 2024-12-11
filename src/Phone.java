import br.edu.ifpb.webFramework.persistence.annotations.Column;
import br.edu.ifpb.webFramework.persistence.annotations.Entity;

import java.time.LocalDate;

@Entity(name = "phones")
public class Phone {
    @Column(name = "id", primaryKey = true)
    private Long id;

    @Column(name = "brand", notNull = true)
    private String brand;

    @Column(name = "operatingSystem")
    private String operatingSystem;

    @Column(name = "screenSize")
    private Double screenSize;

    @Column(name = "batteryCapacity")
    private Integer batteryCapacity;

    @Column(name = "manufacturingDate", notNull = true)
    private LocalDate manufacturingDate;

    @Column(name = "user_id", foreignKey = true, references = "users", referenceId = "id")
    private Person person;

    public Phone(String brand, String operatingSystem, Double screenSize, Integer batteryCapacity, LocalDate manufacturingDate) {
        this.brand = brand;
        this.operatingSystem = operatingSystem;
        this.screenSize = screenSize;
        this.batteryCapacity = batteryCapacity;
        this.manufacturingDate = manufacturingDate;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(String operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public Double getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Double screenSize) {
        this.screenSize = screenSize;
    }

    public Integer getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(Integer batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public LocalDate getManufacturingDate() {
        return manufacturingDate;
    }

    public void setManufacturingDate(LocalDate manufacturingDate) {
        this.manufacturingDate = manufacturingDate;
    }
}
