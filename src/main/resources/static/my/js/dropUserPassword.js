class DropUserPasswordRequest {
    userId;

    constructor(userId) {
        this.userId = userId;
    }
}

class DropUserPasswordResponse {
    password;
    error;
}

function dropUserPassword(userId) {
    var xhr = new XMLHttpRequest();
    var url = "/auth/dropUserPassword";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === 4 && xhr.status === 200) {
            var response = JSON.parse(xhr.responseText);
            if (response.password) {
                alert(response.password);
            } else {
                alert(response.error);
            }
        }
    };

        var request = new DropUserPasswordRequest(userId);
        xhr.send(JSON.stringify(request));
}
