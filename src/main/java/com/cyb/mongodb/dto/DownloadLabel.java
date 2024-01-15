package com.cyb.mongodb.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DownloadLabel {
    private Integer labelIndex;
    private Double x;
    private Double y;
    private Double width;
    private Double height;
}
