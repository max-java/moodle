//package by.jrr.profile.admin;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestController;
//
//@ControllerAdvice
//@RestController
//public class GlobalExceptionHandler {
//
//    private class JsonResponse {
//        String message;
//
//        public JsonResponse() {
//        }
//
//        public JsonResponse(String message) {
//            super();
//            this.message = message;
//        }
//
//        public String getMessage() {
//            return message;
//        }
//
//        public void setMessage(String message) {
//            this.message = message;
//        }
//    }
//
//    @ExceptionHandler(value = Exception.class)
//    public ResponseEntity<JsonResponse> handleException(Exception e) {
//        return new ResponseEntity<JsonResponse>(new JsonResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
//    }
//}
//
//// https://stackoverflow.com/questions/38062470/return-json-on-uncaught-exceptions-in-spring-controllers
