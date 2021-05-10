package by.jrr.api;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "keycloak", url = "https://sso.tutrit.com/auth/realms/tutrit.jrr.by/protocol/openid-connect/")
public interface KeycloakProxy {

    @GetMapping("/token")
    Object getToken();

}

//curl -i -X POST \
//        -H "Content-Type:application/x-www-form-urlencoded" \
//        -d "client_id=moodle" \
//        -d "username=user@dev" \
//        -d "password=mypass" \
//        -d "grant_type=password" \
//        'https://sso.tutrit.com/auth/realms/tutrit.jrr.by/protocol/openid-connect/token'


