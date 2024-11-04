package com.metsakuur.ezway.model;

import com.metsakuur.face.model.EzResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EzApiResponse {

    private String reqId ;
    private String resp_code;
    private String resp_msg;
    private String resp_score;
    private String resp_age;
    private String resp_gender;
    private String resp_threshold;
    private String resp_cust_no;
    private String resp_chnl_dv;
    private String resp_ids;
    private String resp_id;
    private String resp_name;
    private String resp_json;

    public static EzApiResponse fromEzResponse(String reqId,  EzResponse ezResponse) {
        return EzApiResponse.builder()
                .reqId(reqId)
                .resp_code(ezResponse.getResp_code())
                .resp_msg(ezResponse.getResp_msg())
                .resp_score(ezResponse.getResp_score())
                .resp_age(ezResponse.getResp_age())
                .resp_gender(ezResponse.getResp_gender())
                .resp_threshold(ezResponse.getResp_threshold())
                .resp_cust_no(ezResponse.getResp_cust_no())
                .resp_chnl_dv(ezResponse.getResp_chnl_dv())
                .resp_ids(ezResponse.getResp_ids())
                .resp_id(ezResponse.getResp_id())
                .resp_name(ezResponse.getResp_name())
                .resp_json(ezResponse.getResp_json())
                .build();
    }

}
