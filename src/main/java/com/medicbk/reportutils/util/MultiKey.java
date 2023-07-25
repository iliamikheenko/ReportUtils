package com.medicbk.reportutils.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MultiKey {
    private Long patientId;
    private List<Long> nosologies;
    private final boolean ignoreData = true;
    private final String locale = "ru";

    public String getMD5sum(){
        return DigestUtils.md5DigestAsHex(this.toString().getBytes(StandardCharsets.UTF_8));
    }
}
