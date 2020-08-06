package by.jrr.email.service;


import by.jrr.moodle.bean.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

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
        System.out.println(">> In sendQuickRegostrationPassword ");
        StringBuffer messageText = new StringBuffer();
        messageText.append("Добрый день, " + firstAndLastName + "!");
        messageText.append("\n\nЭто продолжение сообщения об успешной регистрации на образовательной платформе https://moodle.jrr.by !");
        messageText.append("\nДля входа в аккаунт используйте пароль.");
        messageText.append("\n\n\t пароль:" + password);
        messageText.append("\n\nДо встречи на занятиях!");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("{JG} Подтверждение регистрации");
        message.setText(messageText.toString());
        emailSender.send(message);
    }

    private void sendQuickRegostrationInfoAndLogin(String to, String firstAndLastName) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Добрый день, " + firstAndLastName + "!");
        messageText.append("\n\nВы успешно зарегистрировались на образовательной платформе https://moodle.jrr.by!");
        messageText.append("\nИспользуйте адрес электронной почты для входа в аккаунт.");
        messageText.append("\n\n\t имя пользователя:" + to);
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
        System.out.println(">> In amoCrmTrigger");
        StringBuffer messageText = new StringBuffer();
        messageText.append("Заявка с Java Bootcamp");
        messageText.append("\nОт: " + firstAndLastName);
        messageText.append("\nЕ-почта: " + userEmail);
        messageText.append("\nТелефон: " + userPhone);
        messageText.append("\nЗаявка на курс: Java A-Z"); // TODO: 27/07/20 set course name dynamically
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

    public void sendStreamSubscriptionConfirmation(String firstAndLastName,
                                                   String teamStreamName,
                                                   String streamTeamLink,
                                                   String telegramLink,
                                                   String to) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Добрый день, " + firstAndLastName + "!");
        messageText.append("\n\nВаша заявка на участие в группе " + teamStreamName + " одобрена.");
        messageText.append("\nТеперь Вам доступны материалы группы.");
        messageText.append("\n\n\t адрес группы: " + streamTeamLink);
        messageText.append("\n\n");
        messageText.append("\nОбязательно вступите в чат-телеграмм группы, что бы получать актуальную информацию!");
        messageText.append("\n\n\t telegram: " + telegramLink);
        messageText.append("\n");
        messageText.append("\nПервое занятие пройдет 6 июля 2020 в формате Zoom конференции.");
        messageText.append("\nДата: 6 июля 2020 года.");
        messageText.append("\nВремя: 19:00 - 22:00.");
        messageText.append("\nМесто: ссылка в чате за 15 минут до начала.");
        messageText.append("\nСтоимость: бесплатно.");
        messageText.append("\nВаш куратор: Илья +375(29) 3333-600.");
        messageText.append("\n");
        messageText.append("\nУ Вас есть есть возможность бесплатно обучиться на курсе Java Intensive!");
        messageText.append("\nДля этого нужно:");
        messageText.append("\n1. Посещать подготовительные занятия;");
        messageText.append("\n2. Быть готовым всё свое свободное время на ближайшие три месяца посвятить изучению программирования;");
        messageText.append("\n3. Не бояться трудностей и быть готовым к новым вызовам и испытаниям;");
        messageText.append("\n4. Освоить навыки командной работы, принципы современной методологии разработки и инструменты production development за 1 месяц подготовительного курса;");
        messageText.append("\n5. Сделать все возможное и не возможное, что бы достигнуть отличных результатов за время подготовки к Java Intensive!");
        messageText.append("\n");
        messageText.append("\nНачните готовиться уже сейчас, что бы получить возможность обучиться на курсе Java Intensive с отсрочкой платежа до трудоустройства!");
        messageText.append("\nДля этого нужно:");
        messageText.append("\n");
        messageText.append("\n1. Подписаться на чат школы в Telegram https://t.me/joinchat/CxUOGRRNPKd3vDxzt9YI4A");
        messageText.append("\n- здесь уже сейчас Вы можете начать общаться с нашими выпускниками");
        messageText.append("\n");
        messageText.append("\n2. Подписаться на чат группы в Telegram " + telegramLink);
        messageText.append("\n");
        messageText.append("\n3. Зарегистрироваться в github.com");
        messageText.append("\n");
        messageText.append("\nНаша задача - освоить навыки командной работы и инструменты production development за 1 месяц!");
        messageText.append("\n");
        messageText.append("\nЖду Вас на первом занятии!");
        messageText.append("\nС уважением,");
        messageText.append("\nУправляющий JavaGuru в Беларуси");
        messageText.append("\nМаксим Шелкович");
        messageText.append("\n+375(44) 750 6666");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("{JG} Подтверждение подписки на группу");
        message.setText(messageText.toString());
        emailSender.send(message);
    }

    public void sendJavaAZSubscriptionConfirmation(String firstAndLastName,
                                                   String teamStreamName,
                                                   String streamTeamLink,
                                                   String telegramLink,
                                                   String to) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Добрый день, " + firstAndLastName + "!");
        messageText.append("\n\nВаша заявка на участие в группе " + teamStreamName + " одобрена.");
        messageText.append("\nТеперь Вам доступны материалы группы.");
        messageText.append("\n\n\t адрес группы: " + streamTeamLink);
        messageText.append("\n\n");
        messageText.append("\nОбязательно вступите в чат-телеграмм группы, что бы получать актуальную информацию!");
        messageText.append("\n\n\t telegram: " + telegramLink);
        messageText.append("\n");
        messageText.append("\nЗанятие пройдет в формате Zoom конференции.");
        messageText.append("\nРасписание в профиле группы");
        messageText.append("\nВаш куратор: Илья +375(29) 3333-600.");
        messageText.append("\n");
        messageText.append("\nУ Вас есть есть возможность бесплатно обучиться на курсе Java Intensive!");
        messageText.append("\nДля этого нужно:");
        messageText.append("\n1. Записаться подготовительные занятия preIntensive;");
        messageText.append("\n2. Быть готовым всё свое свободное время на ближайшие 4 месяца посвятить изучению программирования;");
        messageText.append("\n3. Не бояться трудностей и быть готовым к новым вызовам и испытаниям;");
        messageText.append("\n4. Освоить навыки командной работы, принципы современной методологии разработки и инструменты production development за 1 месяц подготовительного курса;");
        messageText.append("\n5. Сделать все возможное и не возможное, что бы достигнуть отличных результатов за время подготовки к Java Intensive!");
        messageText.append("\n");
        messageText.append("\nНачните готовиться уже сейчас, что бы получить возможность обучиться на курсе Java Intensive с отсрочкой платежа до трудоустройства!");
        messageText.append("\nДля этого нужно:");
        messageText.append("\n");
        messageText.append("\n1. Подписаться на чат школы в Telegram https://t.me/joinchat/CxUOGRRNPKd3vDxzt9YI4A");
        messageText.append("\n- здесь уже сейчас Вы можете начать общаться с нашими выпускниками");
        messageText.append("\n");
        messageText.append("\n2. Подписаться на чат группы в Telegram " + telegramLink);
        messageText.append("\n");
        messageText.append("\n3. Зарегистрироваться в github.com");
        messageText.append("\n");
        messageText.append("\nНаша задача - освоить навыки командной работы и инструменты production development за 1 месяц!");
        messageText.append("\n");
        messageText.append("\nДо встречи на занятиях!");
        messageText.append("\nС уважением,");
        messageText.append("\nУправляющий JavaGuru в Беларуси");
        messageText.append("\nМаксим Шелкович");
        messageText.append("\n+375(44) 750 6666");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("{JG} Подтверждение подписки на группу");
        message.setText(messageText.toString());
        emailSender.send(message);
    }

    public void sendTeamSubscriptionConfirmation(String firstName,
                                                 String teamStreamName,
                                                 String streamTeamLink,
                                                 String to) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Привет, " + firstName + "!");
        messageText.append("\n\nТвоя заявка на участие в группе " + teamStreamName + " одобрена.");
        messageText.append("\nНе забывай, что теперь тебе нужно принимать участие в стендапах ежедневно.");
        messageText.append("\nОткрывать ссылку на стендап ОБЯЗАТЕЛЬНО через платформу, что бы твой контрибьют в комьюнити был залогирован");


        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("{JG} Добро пожаловать в команду!");
        messageText.append("\nТеперь тебе доступны ресурсы группы.");
        messageText.append("\n\n\t сохрани в закладки прямую ссылку на группу: " + streamTeamLink);
        message.setText(messageText.toString());
        emailSender.send(message);
        System.out.println(message); // TODO: 07/07/20 log emails
    }

    public void sendAdminRegisterYouEmailConfirmation(String firstName, String emailTo, String password) {
        StringBuffer messageText = new StringBuffer();
        messageText.append("Добрый день, " + firstName + "!");
        messageText.append("\n\nВы получили это письмо по одной из двух причин:");
        messageText.append("\n1. оставляли заявку или даже приходили на наши бесплатные курсы по Java, но по какой-то причине передумали и пропали из команды.\"");
        messageText.append("\n2. хотите участвовать в челледже gotogoogle.\"");
        messageText.append("\n\n");
        messageText.append("\nС участием наших студентов создана образовательная платформа, где можно:");
        messageText.append("\n1. Подготовиться к собеседованию в режиме вопрос-ответ");
        messageText.append("\n2. Посмотреть, какие задачи сейчас задают на технических интерью");
        messageText.append("\n3. Проработать и прокачать свой профиль в соцсети с помощью живых инструкций");
        messageText.append("\n4. Получить реальный production опыт в проектах комьюнити, который так часто спрашивают HRы");
        messageText.append("\n5. Участвовать в еженедельных разборах, техтоках и хакатонах");
        messageText.append("\n\n");
        messageText.append("\n А главное - присоединиться к нашей движухе - дорога в Google!");
        messageText.append("\n\n");
        messageText.append("\n Я сделал для Вас аккаунт:");
        messageText.append("\n\t логин:" + emailTo);
        messageText.append("\n\t пароль:" + password);
        messageText.append("\nадрес новой платформы - https://moodle.jrr.by/");

        messageText.append("\n\nЕсли что-то не получается - позвоните Илье: +375(29) 3333-600.");
        messageText.append("\nЕсли что-то не получается - позвоните Илье: +375(29) 3333-600.");
        messageText.append("\nВопросы можно задать и боту: @JavaQuestionsBot");

        messageText.append("\nС уважением,");
        messageText.append("\nУправляющий JavaGuru в Беларуси");
        messageText.append("\nМаксим Шелкович");
        messageText.append("\n+375(44) 750 6666");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailTo);
        message.setSubject("{JG} JavaGuru комьюнити! Мы растем!!!");
        message.setText(messageText.toString());
        emailSender.send(message);
    }

    public void sendLecture1GoToGoogleEmail(String email, Lecture lecture) {
        try {
            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("{JG} " + lecture.getTitle() + " " + lecture.getSubtitle());
            helper.setText(lecture.getText(), true);

            emailSender.send(message);
        } catch (Exception ex) {
            System.out.println("ex = " + ex);
        }
    }
}

