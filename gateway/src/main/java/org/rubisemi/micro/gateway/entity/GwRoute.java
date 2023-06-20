package org.rubisemi.micro.gateway.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GwRoute implements Serializable {
    private Long id;
    private String path;
    private String uri;
    private String regexp;
    private String replacement;
}
