package com.medicbk.reportutils.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@Data
@MappedSuperclass
@EqualsAndHashCode
@NoArgsConstructor
public abstract class AbstractEntity implements Serializable {
}
