package com.metsakuur.ezway.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FRVerifyRequest {
    private String reqId ;
    private String custNo ;
    private String osType ;
    private String depthImage ;
    private String image  ;
    private String deviceName ;
}
