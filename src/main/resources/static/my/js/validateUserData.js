//this sends request to user data validation endpoint to notify user if input data set correctly.

// input ids:
//     document.getElementById("firstAndLastName").value
//     document.getElementById("email").value
//     document.getElementById("phone").value
// error ids:
//     document.getElementById("FIRST_AND_LAST_NAME_ERROR").innerHTML
//     document.getElementById("EMAIL_ERROR").innerHTML
//     document.getElementById("PHONE_ERROR").innerHTML

class UserValidationRequest {
    firstAndLastName;
    email;
    phone;

    constructor(firstAndLastName, email, phone) {
        this.firstAndLastName = firstAndLastName;
        this.email = email;
        this.phone = phone;
    }
}

class UserValidationResponse {
    userValidationStatus;
    errors;
}

function validateForm() {
    var xhr = new XMLHttpRequest();
    var url = "/auth/validate/user";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            if(response.userValidationStatus === "ERROR") {
                setErrorFields(response);
            } else {
                clearErrorFields();
            }
        }
    };

    var request = new UserValidationRequest(
        document.getElementById("firstAndLastName").value,
        document.getElementById("email").value,
        document.getElementById("phone").value
    );
    xhr.send(JSON.stringify(request));
}

function setErrorFields(response) {
    clearErrorFields();
    if (response.errors.FIRST_AND_LAST_NAME) {
        document.getElementById("FIRST_AND_LAST_NAME_ERROR").innerHTML = response.errors.FIRST_AND_LAST_NAME;
    } else {
        document.getElementById("FIRST_AND_LAST_NAME_ERROR").innerHTML = "";
    }
    if (response.errors.EMAIL) {
        document.getElementById("EMAIL_ERROR").innerHTML = response.errors.EMAIL;
    } else {
        document.getElementById("EMAIL_ERROR").innerHTML = "";
    }
    if (response.errors.PHONE) {
        document.getElementById("PHONE_ERROR").innerHTML = response.errors.PHONE;
    } else {
        document.getElementById("PHONE_ERROR").innerHTML = "";
    }
}

function clearErrorFields() {
    document.getElementById("FIRST_AND_LAST_NAME_ERROR").innerHTML = "";
    document.getElementById("EMAIL_ERROR").innerHTML = "";
    document.getElementById("PHONE_ERROR").innerHTML = "";
}
