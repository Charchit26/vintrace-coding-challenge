package com.winery.breakdowns.api.services;

import com.winery.breakdowns.api.models.Breakdown;
import com.winery.breakdowns.api.models.BreakdownResponse;
import com.winery.breakdowns.api.models.Component;
import com.winery.breakdowns.api.models.ComponentKeys;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BreakdownService {

    public BreakdownResponse build(ComponentKeys key, Stream<Component> cds) {
        switch (key) {
            case YEAR:
                return buildYearBreakdown(cds);
            case REGION:
                return buildRegionBreakdown(cds);
            case VARIETY:
                return buildVarietyBreakdown(cds);
            case YEAR_VARIETY:
                return buildYearVarietyBreakdown(cds);
        }

        throw new IllegalArgumentException("Received invalid type: " + key);
    }

    private BreakdownResponse buildYearBreakdown(Stream<Component> cds) {
        Map<String, Double> totalPercentageByYear = cds
                .collect(Collectors.groupingBy(component -> String.valueOf(component.getYear()),
                        Collectors.summingDouble(Component::getPercentage)));

        return buildBreakdownResponse("year", totalPercentageByYear);
    }

    private BreakdownResponse buildVarietyBreakdown(Stream<Component> cds) {
        Map<String, Double> totalPercentageByVariety = cds
                .collect(Collectors.groupingBy(Component::getVariety,
                        Collectors.summingDouble(Component::getPercentage)));

        return buildBreakdownResponse("variety", totalPercentageByVariety);
    }

    private BreakdownResponse buildRegionBreakdown(Stream<Component> cds) {
        Map<String, Double> totalPercentageByRegion = cds
                .collect(Collectors.groupingBy(Component::getRegion,
                        Collectors.summingDouble(Component::getPercentage)));

        return buildBreakdownResponse("region", totalPercentageByRegion);
    }

    private BreakdownResponse buildYearVarietyBreakdown(Stream<Component> cds) {
        Map<String, Double> totalPercentageByYearVariety = cds
                .collect(Collectors.groupingBy(component -> String.format("%d-%s", component.getYear(), component.getVariety()),
                        Collectors.summingDouble(Component::getPercentage)));

        return buildBreakdownResponse("year-variety", totalPercentageByYearVariety);
    }

    private BreakdownResponse buildBreakdownResponse(String key, Map<String, Double> componentMap) {
        List<Breakdown> breakdowns = componentMap.entrySet().stream()
                .map(o -> new Breakdown(String.valueOf(o.getValue()), o.getKey()))
                .collect(Collectors.toList());

        return new BreakdownResponse(key, breakdowns);
    }

}
