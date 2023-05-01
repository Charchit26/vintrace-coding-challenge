package com.winery.breakdowns.api.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Component {

    private final double percentage;
    private final int year;
    private final String variety;
    private final String region;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public Component(
            @JsonProperty("percentage") double p,
            @JsonProperty("year") int y,
            @JsonProperty("variety") String v,
            @JsonProperty("region") String r
    ) {
        this.percentage = p;
        this.year = y;
        this.variety = v;
        this.region = r;
    }
}
