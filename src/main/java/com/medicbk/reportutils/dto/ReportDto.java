package com.medicbk.reportutils.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class ReportDto {
    private UUID uuid;
    private Long patientId;
    private List<Long> nosologyIds;
}
