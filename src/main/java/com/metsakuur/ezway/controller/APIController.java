package com.metsakuur.ezway.controller;

import com.google.gson.Gson;
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
                req.getDeviceName() == null ||
                req.getImages() == null ||
                req.getImages().size() == 0
        ) {
            throw new IllegalArgumentException("Invalid request");
        }
        if(! hasProperOsType(req.getOsType())) {
            throw new IllegalArgumentException("Invalid osType");
        }
    }

    private void invalidateVerifyRequest(FRVerifyRequest req) {
        if (req.getCustNo() == null ||
                req.getOsType() == null ||
                req.getDeviceName() == null ||
                req.getImage() == null
        ) {
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
    public ResponseEntity<String> regist(@RequestBody FRRegistRequest req) {
        Gson gson = new Gson();
        try {
            invalidateRegRequest(req);
            EzApiResponse response = ezService.registerUser(req.getReqId() , req.getCustNo(), req.getName(), getOsType(req.getOsType()) , req.getDepthImage(), req.getDeviceName(), req.getImages());
            String resp  = gson.toJson(response);
            log.info("Regist Response : {}  "  ,  resp);
            return ResponseEntity.ok(resp);
        } catch (FRException e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code(e.getFrResultType().getCode()) ;
            response.setResp_msg(e.getFrResultType().getMsg());
            String resp  = gson.toJson(response);
            log.info("Regist Response : {}  "  ,  resp);
            return ResponseEntity.ok(resp);
        }catch (Exception e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code("9999") ;
            response.setResp_msg(e.getMessage());
            String resp  = gson.toJson(response);
            log.info("Regist Response : {}  "  ,  resp);
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody FRVerifyRequest req) {
        log.info("verify : " + req.toString());
        Gson gson = new Gson();
        try {
            invalidateVerifyRequest(req);
            //verifyUser(String custNo , OsType osType , String image , String depthImage , String deviceName)
            EzApiResponse response = ezService.verifyUser(req.getReqId() , req.getCustNo(), getOsType( req.getOsType() ) ,
                    req.getImage() , req.getDepthImage() , req.getDeviceName());
            String resp  = gson.toJson(response);
            log.info("Verify Response : {}  "  ,  resp);
            return ResponseEntity.ok(resp);
        }catch(FRException e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code(e.getFrResultType().getCode()) ;
            response.setResp_msg(e.getFrResultType().getMsg());
            String resp  = gson.toJson(response);
            log.info("Verify Response : {} " , resp);
            return ResponseEntity.ok(resp);
        }catch (Exception e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code("9999") ;
            response.setResp_msg(e.getMessage());
            String resp  = gson.toJson(response);
            log.info("Verify Response : {} " , resp);
            return ResponseEntity.ok(resp);
        }
    }

    @PostMapping("/delete")
    public ResponseEntity<EzApiResponse> delete(@RequestBody FRBasicRequest req) {
        EzApiResponse response =  ezService.deleteTemplate( req.getReqId() ,  req.getCustNo(), getOsType( req.getOsType() ) );
        return ResponseEntity.ok(response);
    }


    @PostMapping("/compare")
    public ResponseEntity<String> compare(@RequestBody FRCompareRequest req) {
        Gson gson = new Gson();
        try {
            invalidateCompareRequest(req);
            EzApiResponse response = ezService.verifyIdCardFace(req.getReqId() , req.getIdImage(), getOsType( req.getImage() ) , req.getDepthImage(), req.getOsType(), req.getCustNo());
            String resp  = gson.toJson(response);
            log.info("Compare Response : {}  "  ,  resp);
            return ResponseEntity.ok(resp);
        } catch(FRException e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code(e.getFrResultType().getCode());
            response.setResp_msg(e.getFrResultType().getMsg());
            String resp  = gson.toJson(response);
            log.info("Compare Response : {}  "  ,  resp);
            return ResponseEntity.ok(resp);
        }catch (Exception e) {
            EZErrorResponse response = new EZErrorResponse();
            response.setResp_code("9999") ;
            response.setResp_msg(e.getMessage());String resp  = gson.toJson(response);
            log.info("Compare Response : {}  "  ,  resp);
            return ResponseEntity.ok(resp);
        }
    }

}
