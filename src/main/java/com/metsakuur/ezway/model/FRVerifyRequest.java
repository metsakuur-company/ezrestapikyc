package com.metsakuur.ezway.model;

import com.metsakuur.common.util.StringUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FRVerifyRequest {
    private String reqId ;
    private String custNo ;
    private String osType ;
    private String depthImage ;
    private String image  ;
    private String deviceName ;

    @Override
    public String toString() {
        return  "custNo : " + custNo + " , osType : " + osType + " , depthImage : " + StringUtil.isEmpty( depthImage)  + " , image : " + StringUtil.isEmpty( image ) + " , deviceName : " + deviceName ;
    }
}
