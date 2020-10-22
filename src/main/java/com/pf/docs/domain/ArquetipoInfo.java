package com.pf.docs.domain;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArquetipoInfo implements Serializable {
    private String title;
    private String description;
}
