package com.metsakuur.ezway.controller;

import com.metsakuur.ezway.model.FRRegistRequest;
import com.metsakuur.ezway.model.FRVerifyRequest;
import com.metsakuur.ezway.service.EZService;
import com.metsakuur.face.model.EzResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class APIController {

    private EZService ezService;

    @PostMapping("/regist")
    public ResponseEntity<EzResponse> regist(@RequestBody FRRegistRequest req) {
        EzResponse response =  ezService.registerUser(req.getCustNo(), req.getName(), req.getOsType(), req.getDepthImage(), req.getDeviceName() ,  req.getImages());
        return ResponseEntity.ok(response);
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


}
