package com.metsakuur.ezway.controller;

import com.metsakuur.common.exception.FRException;
import com.metsakuur.ezway.model.*;
import com.metsakuur.ezway.service.EZService;
import com.metsakuur.face.enums.OsType;
import com.metsakuur.face.model.EzResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.yaml.snakeyaml.util.EnumUtils;

@Controller
@Slf4j
public class APIController {

    @Autowired
    private EZService ezService;

    private void invalidateRegRequest(FRRegistRequest req) {
        if (req.getCustNo() == null ||
                req.getName() == null ||
                req.getOsType() == null ||
                req.getDeviceName() == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        if(! hasProperOsType(req.getOsType())) {
            throw new IllegalArgumentException("Invalid osType");
        }
    }

    private void invalidateVerifyRequest(FRVerifyRequest req) {
        if (req.getCustNo() == null ||
                req.getOsType() == null ||
                req.getDeviceName() == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        if(! hasProperOsType(req.getOsType())) {
            throw new IllegalArgumentException("Invalid osType");
        }
    }

    private void invalidateCompareRequest(FRCompareRequest req) {
        if (req.getIdImage() == null ||
                req.getImage() == null ||
                req.getOsType() == null ||
                req.getCustNo() == null) {
            throw new IllegalArgumentException("Invalid request");
        }
        if(! hasProperOsType(req.getOsType())) {
            throw new IllegalArgumentException("Invalid osType");
        }
    }

    private boolean hasProperOsType(String osType) {
        try {
          EnumUtils.findEnumInsensitiveCase(OsType.class, osType);
          return true ;
        }catch (IllegalArgumentException e) {
            return false;
        }
    }

    private OsType getOsType(String osType) {
        return EnumUtils.findEnumInsensitiveCase(OsType.class, osType);
    }


    @PostMapping("/regist")
    public ResponseEntity<Object> regist(@RequestBody FRRegistRequest req) {
        try {
            invalidateRegRequest(req);
            EzResponse response = ezService.registerUser(req.getCustNo(), req.getName(), getOsType(req.getOsType()) , req.getDepthImage(), req.getDeviceName(), req.getImages());
            return ResponseEntity.ok(response);
        } catch (FRException e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code(e.getFrResultType().getCode()) ;
            response.setResp_msg(e.getFrResultType().getMsg());
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code("9999") ;
            response.setResp_msg(e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<Object> verify(@RequestBody FRVerifyRequest req) {
        log.info("verify : " + req.toString());
        try {
            invalidateVerifyRequest(req);
            EzResponse response = ezService.verifyUser(req.getCustNo(), getOsType( req.getOsType() ) , req.getDepthImage(), req.getImage(), req.getDeviceName());
            return ResponseEntity.ok(response);
        }catch(FRException e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code(e.getFrResultType().getCode()) ;
            response.setResp_msg(e.getFrResultType().getMsg());
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code("9999") ;
            response.setResp_msg(e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<EzResponse> delete(@RequestBody FRBasicRequest req) {
        EzResponse response =  ezService.deleteTemplate( req.getCustNo(), getOsType( req.getOsType() ) );
        return ResponseEntity.ok(response);
    }


    @PostMapping("/compare")
    public ResponseEntity<Object> compare(@RequestBody FRCompareRequest req) {
        try {
            invalidateCompareRequest(req);
            EzResponse response = ezService.verifyIdCardFace(req.getIdImage(), getOsType( req.getImage() ) , req.getDepthImage(), req.getOsType(), req.getCustNo());
            return ResponseEntity.ok(response);
        } catch(FRException e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code(e.getFrResultType().getCode());
            response.setResp_msg(e.getFrResultType().getMsg());
            return ResponseEntity.ok(response);
        }catch (Exception e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code("9999") ;
            response.setResp_msg(e.getMessage());
            return ResponseEntity.ok(response);
        }
    }

}
