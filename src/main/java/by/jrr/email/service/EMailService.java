package by.jrr.email.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EMailService {

    @Autowired
    private JavaMailSender emailSender;

    public void sendTestEmail() {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("6666350@gmail.com");
            message.setSubject("test message from Moodle");
            message.setText("Hello! This is a test message from Moodle");
            emailSender.send(message);
    }

    public void sendQuickRegostrationConfirmation(String to, String password, String firstAndLastName) {
        sendQuickRegostrationInfoAndLogin(to, firstAndLastName);
        sendQuickRegostrationPassword(to, password, firstAndLastName);
    }
    private void sendQuickRegostrationPassword(String to, String password, String firstAndLastName) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Добрый день, "+firstAndLastName+"!");
        messageText.append("\n\nЭто продолжение сообщения об успешной регистрации на образовательной платформе https://moodle.jrr.by!");
        messageText.append("\nДля входа в аккаунт используйте пароль.");
        messageText.append("\n\n\t пароль:"+password);
        messageText.append("\n\nДо встречи на занятиях!");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("{JG} Подтверждение регистрации");
        message.setText(messageText.toString());
        emailSender.send(message);
    }
    private void sendQuickRegostrationInfoAndLogin(String to, String firstAndLastName) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Добрый день, "+firstAndLastName+"!");
        messageText.append("\n\nВы успешно зарегистрировались на образовательной платформе https://moodle.jrr.by!");
        messageText.append("\nИспользуйте адрес электронной почты для входа в аккаунт.");
        messageText.append("\n\n\t имя пользователя:"+to);
        messageText.append("\n\nПароль для входа в аккаунт был сгенерирован автоматически.");
        messageText.append("\nВ таких случаях одним из базовых принципов безопасности является передача логина и пароля в разных отправлениях.");
        messageText.append("\nПароль для входа в аккаунт в следующем письме.");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("{JG} Подтверждение регистрации");
        message.setText(messageText.toString());
        emailSender.send(message);
    }
    public void amoCrmTrigger(String userEmail, String firstAndLastName, String userPhone) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Заявка с Java Bootcamp");
        messageText.append("\nОт: "+firstAndLastName);
        messageText.append("\nЕ-почта: "+userEmail);
        messageText.append("\nТелефон: "+userPhone);
        messageText.append("\nЗаявка на курс: Java free stream 5");
        messageText.append("\n");
        messageText.append("\nhttp://javaguru.by/java-bootcamp");
        messageText.append("\n--");
        messageText.append("\nThis e-mail was sent from a contact form on JavaGuru");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("28750003.118405@parser.amocrm.ru");
        message.setSubject("[JavaGuruRequest] - Заявка на бесплантый курс Java free stream 4");
        message.setText(messageText.toString());
        emailSender.send(message);
    }
}
