package com.winery.breakdowns.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Breakdown {
    private String percentage;
    private String key;
}