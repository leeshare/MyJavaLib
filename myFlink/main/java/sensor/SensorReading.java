package sensor;

public class SensorReading {
    public String id;
    public Long timestamp;
    public Double temperature;

    public SensorReading(String id, Long timestamp, Double temperature) {
        this.id = id;
        this.timestamp = timestamp;
        this.temperature = temperature;
    }
}
