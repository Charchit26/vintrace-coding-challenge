package com.winery.breakdowns.api.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BreakdownResponse {
    private String breakdownType;
    private List<Breakdown> breakdown;
}