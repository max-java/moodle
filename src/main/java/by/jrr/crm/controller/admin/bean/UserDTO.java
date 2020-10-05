package by.jrr.crm.controller.admin.bean;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long id;
    //        private String userName; // TODO: 09/06/20 refactor to "login"
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private List<String> errors;
}
/*
{
        "id": 0,
        "name": "",
        "lastName": "",
        "email": "",
        "phone": "",
        "errors":[]
        }

 */
