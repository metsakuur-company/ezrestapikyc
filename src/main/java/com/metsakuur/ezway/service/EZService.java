package com.metsakuur.ezway.service;

import com.metsakuur.common.exception.FRException;
import com.metsakuur.ezway.config.EzConfig;
import com.metsakuur.ezway.config.GlobalConstantData;
import com.metsakuur.ezway.model.EzApiResponse;
import com.metsakuur.face.enums.FrResultType;
import com.metsakuur.face.enums.OsType;
import com.metsakuur.face.enums.ServiceType;
import com.metsakuur.face.model.EzResponse;
import com.metsakuur.face.model.FrRequest;
import com.metsakuur.face.model.FrResponse;
import com.metsakuur.face.service.MkFrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class EZService
{

    @Autowired
    private EzConfig config ;

    private MkFrService frService = new MkFrService();

    /**
     * Verify the user
     * @param custNo
     * @param osType
     * @param image
     * @param depthImage
     * @return
     */
    public EzApiResponse verifyUser(String reqId , String custNo , OsType osType , String image , String depthImage , String deviceName) throws FRException{
        if(GlobalConstantData.SIMULATE_MODE) {
            return defaultResponse(reqId);
        }
        FrRequest frRequest = new FrRequest();
        frRequest.setCustNo(custNo);
        frRequest.setServiceType(ServiceType.VERIFY);
        frRequest.setChnl_dv(config.getChannel());
        frRequest.setIp(config.getIp());
        if(depthImage != null) frRequest.setDepthImage(depthImage);
        frRequest.setOsType(osType);
        frRequest.setPort(config.getPort());
        frRequest.setUuid(config.getUuid());
        frRequest.setImage(image);
        frRequest.setDeviceName(deviceName);
        FrResponse frResponse = frService.frCall(frRequest);
        return EzApiResponse.fromEzResponse(reqId , frResponse.getEzResponse()) ;
    }

    /**
     * Register the user
     * @param custNo
     * @param name
     * @param osType
     * @param depthImage
     * @param faces
     * @return
     * @throws FRException
     */
    public EzApiResponse registerUser(String reqId , String custNo , String name , OsType osType ,String depthImage ,  String deviceName , List<String> faces) throws FRException
    {

        if(GlobalConstantData.SIMULATE_MODE) {
            return defaultResponse(reqId);
        }

        List<EzResponse> failures = new ArrayList<>();
        List<EzResponse> successes = new ArrayList<>();

        EzResponse response = null ;
        FrRequest frRequest = new FrRequest();
        frRequest.setCustNo(custNo);
        frRequest.setCustName(name);
        frRequest.setOsType( osType);
        if(depthImage != null) frRequest.setDepthImage(depthImage);
        frRequest.setServiceType(ServiceType.REGIST);
        frRequest.setChnl_dv(config.getChannel());
        frRequest.setIp(config.getIp());
        frRequest.setPort(config.getPort());
        frRequest.setUuid(config.getUuid());
        frRequest.setDeviceName(deviceName);

        for(String face : faces)  {
            frRequest.setImage(face);
            FrResponse frResponse = frService.frCall(frRequest);
            log.info("Response : {}  , ezResp: {}  ", frResponse.getCode(), frResponse.getEzResponse().getResp_code());
            response= frResponse.getEzResponse();
            if ("OK".equals(frResponse.getCode())) {
                if ("00000".equals(response.getResp_code())) {
                    successes.add(response);
                } else {
                    failures.add(response);
                }
            }else {
                failures.add(response);
            }
        } ;

        int cutoff =  (int) (faces.size() * 0.75)  ;
        if(successes.size() < cutoff) {
            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    deleteTemplate(reqId , custNo, osType);
                } catch (InterruptedException e) {
                   //NOthing to do
                }
            }).start();
            throw new FRException(FrResultType.from(failures.get(0).getResp_code()));
        }
        return EzApiResponse.fromEzResponse(reqId ,  successes.get(0) ) ;

    }

    /**
     * Delete the template
     * @param custNo
     * @param osType
     * @return
     */
    public EzApiResponse deleteTemplate(String reqId, String custNo , OsType osType) {
        if(GlobalConstantData.SIMULATE_MODE) {
            return defaultResponse(reqId);
        }
        FrRequest frRequest = new FrRequest();
        frRequest.setCustNo(custNo);
        frRequest.setServiceType(ServiceType.DELETE);
        frRequest.setChnl_dv(config.getChannel());
        frRequest.setIp(config.getIp());
        frRequest.setOsType(osType);
        frRequest.setPort(config.getPort());
        frRequest.setUuid(config.getUuid());
        FrResponse frResponse = frService.frCall(frRequest);
        return EzApiResponse.fromEzResponse(reqId , frResponse.getEzResponse()) ;
    }

    public EzApiResponse verifyIdCardFace(String reqId , String custNo , OsType osType , String idImage , String image , String depthImage) throws FRException {
        if(GlobalConstantData.SIMULATE_MODE) {
            return defaultResponse(reqId);
        }
        FrRequest frRequest = new FrRequest();
        frRequest.setCustNo(custNo);
        frRequest.setServiceType(ServiceType.UNTACT_NODB);
        frRequest.setChnl_dv(config.getChannel());
        frRequest.setIp(config.getIp());
        frRequest.setDepthImage(depthImage);
        frRequest.setOsType(osType);
        frRequest.setPort(config.getPort());
        frRequest.setUuid(config.getUuid());
        frRequest.setImage(image);
        frRequest.setIdImage(idImage);
        FrResponse frResponse = frService.frCall(frRequest);
        return EzApiResponse.fromEzResponse(reqId , frResponse.getEzResponse()) ;
    }

    private EzApiResponse defaultResponse(String reqId) {
        return EzApiResponse.builder()
                .reqId(reqId)
                .resp_code("00000")
                .resp_msg("Simulation Mode")
                .build();
    }

}