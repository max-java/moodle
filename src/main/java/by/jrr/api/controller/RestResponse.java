//package by.jrr.api.controller;
//
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.xml.bind.annotation.XmlRootElement;
//import java.util.List;
//
///**
// * this class is a response body for Rest API
// */
//
//
//@Data
//@XmlRootElement
//@NoArgsConstructor
//@Deprecated //use auth validation controller
//public class RestResponse { // TODO: 23/06/20 consider to make it Validation Response
//    private boolean validationPassed; // TODO: 23/06/20 create enum with statuses
//    private List<String> errorList;
//    private String error;
//
//    public RestResponse(boolean validationPassed) {
//        this.validationPassed = validationPassed;
//    }
//}
