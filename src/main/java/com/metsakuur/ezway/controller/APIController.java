package com.metsakuur.ezway.controller;

import com.metsakuur.common.exception.FRException;
import com.metsakuur.ezway.model.EZErrorResponse;
import com.metsakuur.ezway.model.FRCompareRequest;
import com.metsakuur.ezway.model.FRRegistRequest;
import com.metsakuur.ezway.model.FRVerifyRequest;
import com.metsakuur.ezway.service.EZService;
import com.metsakuur.face.model.EzResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@Slf4j
public class APIController {

    @Autowired
    private EZService ezService;

    @PostMapping("/regist")
    public ResponseEntity<Object> regist(@RequestBody FRRegistRequest req) {
        try {
            EzResponse response = ezService.registerUser(req.getCustNo(), req.getName(), req.getOsType(), req.getDepthImage(), req.getDeviceName(), req.getImages());
            return ResponseEntity.ok(response);
        } catch (FRException e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code(e.getFrResultType().getCode()) ;
            response.setResp_msg(e.getFrResultType().getMsg());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<EzResponse> verify(@RequestBody FRVerifyRequest req) {
        EzResponse response =  ezService.verfyUser(req.getCustNo(), req.getOsType(), req.getDepthImage(), req.getImage()  , req.getDeviceName());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<EzResponse> delete(@RequestBody FRVerifyRequest req) {
        EzResponse response =  ezService.deleteTemplate( req.getCustNo(), req.getOsType() );
        return ResponseEntity.ok(response);
    }


    @PostMapping("/compare")
    public ResponseEntity<EzResponse> compare(@RequestBody FRCompareRequest req) {
        EzResponse response =  ezService.verifyIdCardFace(req.getIdImage(), req.getImage(), req.getDepthImage(), req.getOsType(), req.getCustNo());
        return ResponseEntity.ok(response);
    }

}
