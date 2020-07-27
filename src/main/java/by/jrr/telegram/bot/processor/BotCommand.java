package by.jrr.telegram.bot.processor;

public enum BotCommand {
    NONE("/none", "Ничем не могу помочь"),
    START("/start", "начать общение"),
    HELP("/help", "Помощь"),
    SETTING("/settings", "Настройки"),
    NERD_TERM("/whatis", "Сленг програмиста"),
    SAY_HELLO("/hello", "Приветствие нового студента!"),
    SAVE_TERM("/запомни", "Научить бота новому и хорошему."),
    DELETE_TERM("/забудь", "Разучить бота плохому и ошибочному.");

    String command;
    String description;
    public String getCommand() {
        return command;
    }
    public String getDescription() {
        return description;
    }


    BotCommand(String command,String description) {
        this.command = command;
        this.description = description;
    }
}
