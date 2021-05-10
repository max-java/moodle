package by.jrr.api;

import by.jrr.api.model.MessageDto;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//@FeignClient(name = "${tutrit.api}")
@FeignClient(name = "tutrit.api", url = "http://localhost:8078/")
public interface TutritApiProxy {

    @GetMapping("/myjwt")
    String getCurrentUserJwt();
}
//
//'Authorization': 'Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJMOGx1U0gxLUFVa0dhOGo4ZzNKdlE5Z0dUeEZlUkhxZWVjU09oaEt4Smg4In0'
//'Accept': 'application/json'
//
//        curl -i -X GET \
//        -H "'Authorization':'Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJMOGx1U0gxLUFVa0dhOGo4ZzNKdlE5Z0dUeEZlUkhxZWVjU09oaEt4Smg4In0'" \
//        -H "'Accept':'application/json'" \
//        'http://localhost:8078/event'

