class MessageDto {
    id;
    timeStamp;
    lastUpdate;

    chatToken;
    userProfileId;

    telegramStatus; //NEW, IN_PROCESS, SENT
    eMailStatus;    //NEW, IN_PROCESS, SENT

    messageText;
    messageType;    // MESSAGE, CONTACT_DATA, ALERT, INCOME_DATA
}

function parse(messageDto, containerId) {
    let chatAlign = "left";
    let nameAlign = "float-left";
    let infoAlign = "float-right";
    if(messageDto.messageType == "INCOME_DATA") {
        chatAlign = "right";
        nameAlign = "float-right";
        infoAlign = "float-left";
    }

    let msgDiv = createMsgDiv(chatAlign);

    let infoDiv = createInfoDiv();

    if(messageDto.messageType == "INCOME_DATA") {
        infoDiv.appendChild(createInfoName(nameAlign, " telegram: "+messageDto.telegramStatus+"; "));
        infoDiv.appendChild(createInfoName(nameAlign, " e-mail: "+messageDto.eMailStatus+"; "));
        infoDiv.appendChild(createInfoName(nameAlign, " Id"+messageDto.id+"; "));
    } else {
        infoDiv.appendChild(createInfoName(nameAlign, " Id"+messageDto.id+"; "));
        infoDiv.appendChild(createInfoName(nameAlign, " e-mail: "+messageDto.eMailStatus+"; "));
        infoDiv.appendChild(createInfoName(nameAlign, " telegram: "+messageDto.telegramStatus+"; "));
    }

    if(messageDto.messageType == "INCOME_DATA") {
        infoDiv.appendChild(createInfoTime(infoAlign, "created: "+messageDto.timeStamp));
        infoDiv.appendChild(createInfoTime(infoAlign, "-|-updated: "+messageDto.lastUpdate));
    } else {
        infoDiv.appendChild(createInfoTime(infoAlign, "-|-updated: "+messageDto.lastUpdate));
        infoDiv.appendChild(createInfoTime(infoAlign, "created: "+messageDto.timeStamp));
    }

    let textDiv = createTextDiv(messageDto);

    msgDiv.appendChild(infoDiv);
    msgDiv.appendChild(textDiv);

    let chat = document.getElementById(containerId);
    chat.appendChild(msgDiv)
    // chat.scrollTop = chat.scrollHeight; //todo: handle scrolling
}

function createMsgDiv(align) {
    let msgDiv = document.createElement("div");
    msgDiv.classList.add('direct-chat-msg');
    msgDiv.classList.add(align);
    return msgDiv;
}

function createInfoDiv() {
    let infoDiv = document.createElement("div");
    infoDiv.classList.add('direct-chat-infos');
    infoDiv.classList.add('clearfix');
    return infoDiv;
}

function createInfoName(align, text) {
    let infoName = document.createElement("span");
    infoName.classList.add("direct-chat-name");
    infoName.classList.add(align);
    let infoNameText = document.createTextNode(text);
    infoName.appendChild(infoNameText);
    return infoName;
}

function createInfoTime(align, text) {
    let infoTime = document.createElement("span");
    infoTime.classList.add("direct-chat-timestamp");
    infoTime.classList.add(align);
    let infoTimeText = document.createTextNode(text);
    infoTime.appendChild(infoTimeText);
    return infoTime;
}

function createTextDiv(messageDto) {
    let textDiv = document.createElement("div");
    textDiv.classList.add('direct-chat-text');
    let msgText = document.createTextNode(parseMessageDtoText(messageDto));
    textDiv.appendChild(msgText);
    return textDiv;
}

function parseMessageDtoText(messageDto) {
    let text;

    if(messageDto.messageType == "INCOME_DATA") {
        try {
            let messageText = JSON.parse(messageDto.messageText);
            text = messageText.message.text;
        } catch (e) {
            text = ": parse error - " + e + messageDto.messageText;
        }
    }

    if(messageDto.messageType == "MESSAGE") {
        text = messageDto.messageText;
    }
    return text;
}

function scroll(containerId) {
    

}
