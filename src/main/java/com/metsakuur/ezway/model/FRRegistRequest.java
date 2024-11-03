package com.metsakuur.ezway.model;

import com.metsakuur.common.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

import java.util.List ;

@Getter
@Setter
public class FRRegistRequest {

    private String reqId ;
    private String name ;
    private String custNo ;
    private String osType ;
    private String depthImage ;
    private List<String> images  ;
    private String deviceName ;

    @Override
    public String toString() {
        return  "name : " + name + " , custNo : " + custNo + " , osType : " + osType + " , depthImage : " + StringUtil.isEmpty( depthImage)  + " , images : " + images.size() + " , deviceName : " + deviceName ;
    }

}
