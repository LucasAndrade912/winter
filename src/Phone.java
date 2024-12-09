import br.edu.ifpb.webFramework.persistence.annotations.Entity;
import br.edu.ifpb.webFramework.persistence.annotations.OneToOne;

import java.time.LocalDate;

@Entity
public class Phone {
    private String brand;
    private String operatingSystem;
    private Double screenSize;
    private Integer batteryCapacity;
    private LocalDate manufacturingDate;

    @OneToOne
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
