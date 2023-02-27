package io.mcc.mobile.common.config.oauth2.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;

import java.util.Map;

@Slf4j
public class CustomCheckTokenConverter extends DefaultAccessTokenConverter {
  
    @Override
    public OAuth2Authentication extractAuthentication(Map<String, ?> map) {

        OAuth2Authentication Oath2auth = super.extractAuthentication(map);
        
     //   log.debug("Oath2auth = {}", map);
      //  log.debug("Oath2auth = {}, {}, {}", Oath2auth.getOAuth2Request(), Oath2auth.getCredentials(), Oath2auth.getAuthorities());
        
        return new OAuth2Authentication(Oath2auth.getOAuth2Request(), new UsernamePasswordAuthenticationToken(map, Oath2auth.getCredentials(), Oath2auth.getAuthorities()));
     //   return new OAuth2Authentication(Oath2auth.getOAuth2Request(), new UsernamePasswordAuthenticationToken(Oath2auth.getPrincipal(), Oath2auth.getCredentials(), Oath2auth.getAuthorities()));
    }
 

}