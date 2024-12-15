package Dto;



import lombok.Data;
import java.util.Map;

@Data
public class PerformanceChartDto {
    // Example structure: label -> score
    private Map<String, Double> dataPoints;
}
