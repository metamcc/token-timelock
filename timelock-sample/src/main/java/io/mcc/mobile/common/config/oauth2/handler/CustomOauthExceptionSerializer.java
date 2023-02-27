package io.mcc.mobile.common.config.oauth2.handler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomOauthExceptionSerializer extends StdSerializer<CustomOauthException>{
	
	public CustomOauthExceptionSerializer() {
        super(CustomOauthException.class);
    }

	@Override
	public void serialize(CustomOauthException value, JsonGenerator  gen, SerializerProvider provider) throws IOException {
//		 log.info(">>>>>>>>>>>>>>>>>[3]  CustomOauthExceptionSerializer.serialize() called...");
		
		 gen.writeStartObject();
//	     gen.writeNumberField("resultCode", value.getHttpErrorCode());
//	     gen.writeStringField("resultMsg", value.getMessage());
//	     gen.writeBooleanField("status", false);
//	     gen.writeObjectField("data", null);
	     gen.writeObjectField("errors", Arrays.asList(value.getOAuth2ErrorCode(),value.getMessage()));
	        
	     if (value.getAdditionalInformation()!=null) {
	            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
	                String key = entry.getKey();
	                String add = entry.getValue();
	                
//	                log.info(">>>>>>>>>>>>>>>>>  key={}, add={}", key, add);
	                gen.writeStringField(key, add);
	            }
	     }
	        
	     gen.writeEndObject();
		
	}

}
