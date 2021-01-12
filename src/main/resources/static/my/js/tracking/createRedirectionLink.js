//this sends request to tracking controller to create redirection link wich could be logged by platform once used.

class CreateRedirectionLinkRequest {
    studentProfileId;
    streamTeamProfileId;
    courseId;
    lectureId
    urlToRedirect
    eventName
    eventType
    expirationMinutes

    constructor(studentProfileId, streamTeamProfileId, courseId, lectureId, urlToRedirect, eventName, eventType, expirationMinutes) {
        this.studentProfileId = studentProfileId;
        this.streamTeamProfileId = streamTeamProfileId;
        this.courseId = courseId;
        this.lectureId = lectureId;
        this.urlToRedirect = urlToRedirect;
        this.eventName = eventName;
        this.eventType =  eventType;
        this.expirationMinutes = expirationMinutes;
    }
}

function createRedirectionLink(studentProfileId, streamTeamProfileId, courseId, lectureId, urlToRedirect, eventName, eventType, expirationMinutes) {
    var url = "/api/createRedirectionLink";
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");

    var request = new CreateRedirectionLinkRequest(
        studentProfileId,
        streamTeamProfileId,
        courseId,
        lectureId,
        urlToRedirect,
        eventName,
        eventType,
        expirationMinutes);
    alert(request);
    xhr.send(JSON.stringify(request));
}
