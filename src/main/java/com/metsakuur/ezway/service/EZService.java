package com.metsakuur.ezway.service;

import com.metsakuur.common.exception.FRException;
import com.metsakuur.ezway.config.EzConfig;
import com.metsakuur.face.enums.OsType;
import com.metsakuur.face.enums.ServiceType;
import com.metsakuur.face.model.EzResponse;
import com.metsakuur.face.model.FrRequest;
import com.metsakuur.face.model.FrResponse;
import com.metsakuur.face.service.MkFrService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public EzResponse verfyUser(String custNo , String osType , String image , String depthImage , String deviceName) {
        FrRequest frRequest = new FrRequest();
        frRequest.setCustNo(custNo);
        frRequest.setServiceType(ServiceType.VERIFY);
        frRequest.setChnl_dv(config.getChannel());
        frRequest.setIp(config.getIp());
        frRequest.setDepthImage(depthImage);
        frRequest.setOsType(OsType.valueOf(osType));
        frRequest.setPort(config.getPort());
        frRequest.setUuid(config.getUuid());
        frRequest.setImage(image);
        frRequest.setDeviceName(deviceName);
        FrResponse frResponse = frService.frCall(frRequest);
        return frResponse.getEzResponse() ;
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
    public EzResponse registerUser(String custNo , String name , String osType ,String depthImage ,  String deviceName , List<String> faces)

    {
        EzResponse response = null ;
        FrRequest frRequest = new FrRequest();
        frRequest.setCustNo(custNo);
        frRequest.setCustName(name);
        frRequest.setOsType(OsType.valueOf(osType));
        frRequest.setDepthImage(depthImage);
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
                if (!"00000".equals(response.getResp_code())) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            deleteTemplate(custNo, osType);
                        }
                    }).start();
                    break;
                }
            } else if ("FAIL".equals(frResponse.getCode())) {
               break;
            }
        } ;

        return response ;

    }

    /**
     * Delete the template
     * @param custNo
     * @param osType
     * @return
     */
    public EzResponse deleteTemplate(String custNo , String osType) {
        FrRequest frRequest = new FrRequest();
        frRequest.setCustNo(custNo);
        frRequest.setServiceType(ServiceType.DELETE);
        frRequest.setChnl_dv(config.getChannel());
        frRequest.setIp(config.getIp());
        frRequest.setOsType(OsType.valueOf(osType));
        frRequest.setPort(config.getPort());
        frRequest.setUuid(config.getUuid());
        FrResponse frResponse = frService.frCall(frRequest);
        return frResponse.getEzResponse() ;
    }

    public EzResponse verifyIdCardFace(String custNo , String osType , String idImage , String image , String depthImage) {
        FrRequest frRequest = new FrRequest();
        frRequest.setCustNo(custNo);
        frRequest.setServiceType(ServiceType.UNTACT_NODB);
        frRequest.setChnl_dv(config.getChannel());
        frRequest.setIp(config.getIp());
        frRequest.setDepthImage(depthImage);
        frRequest.setOsType(OsType.valueOf(osType));
        frRequest.setPort(config.getPort());
        frRequest.setUuid(config.getUuid());
        frRequest.setImage(image);
        frRequest.setIdImage(idImage);
        FrResponse frResponse = frService.frCall(frRequest);
        return frResponse.getEzResponse() ;
    }

}