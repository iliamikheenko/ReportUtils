package com.medicbk.reportutils.model;

import com.desprice.medicbk.commons.si_model_dto.model.ns.patient_summary.enums.ProcessingStatus;
import com.desprice.medicbk.commons.si_model_dto.model.report_manager.response.PatientReportFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "reports", schema = "report_manager", catalog = "medicbk")
public class Report extends AbstractEntity {
    private static final long serialVersionUID = -4891431056629488419L;

    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Column(name = "multi_key")
    private String multiKey;

    @Column(name = "patient_id")
    private Long patientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProcessingStatus status;

    @Column(name = "files")
    @Type(type = "jsonb")
    private List<PatientReportFile> files = new ArrayList<>();

    @Column(name = "modified_date")
    private Instant modified;

    @Column(name = "expire_date")
    private Instant expire;

    @Column(name = "descr")
    private String description;

    @Column(name = "route_callback")
    private String routeCallback;

    @Column(name = "start_processing")
    private Instant startProcessing;


}

