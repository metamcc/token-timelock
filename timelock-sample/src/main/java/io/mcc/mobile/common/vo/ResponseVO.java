package io.mcc.mobile.common.vo;

import java.util.HashMap;

import io.mcc.common.vo.ResultCode;
import org.slf4j.MDC;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ResponseVO {

    private String resultCode;

    private String resultMessage;

    private Object resultObject;
    
    private Object pagingObject;

    private String tranKey;

    public ResponseVO() {}
    
    public ResponseVO(String code, String message) {
    	this.resultCode = code;
        this.resultMessage = message;
    }
    
    public ResponseVO(String code, String message, String result) {
    	this.resultCode = code;
        this.resultMessage = message;
        this.resultObject = result;
    }
    

    public ResponseVO(ResultCode resultCode) {
        this.resultCode = resultCode.getCode();
        this.resultMessage = resultCode.getMsg();
        this.resultObject = new HashMap();
    }

    public ResponseVO(ResultCode resultCode, Object resultObject) {
        this.resultCode = resultCode.getCode();
        this.resultMessage = resultCode.getMsg();
        this.resultObject = resultObject;
    }

    public void setResponseStatus(final ResultCode resultCode) {
        this.resultCode = resultCode.getCode();
        this.resultMessage = resultCode.getMsg();
    }

    public String getTranKey() {
        return MDC.get("tranKey");
    }
}
