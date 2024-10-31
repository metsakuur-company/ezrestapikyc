package com.metsakuur.ezway.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List ;

@Getter
@Setter
public class FRRegistRequest {

    private String name ;
    private String custNo ;
    private String osType ;
    private String depthImage ;
    private List<String> images  ;

}
