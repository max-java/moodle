package by.jrr.telegram.bot.processor;

public enum BotCommand {
    NONE("/none", "Ничем не могу помочь"),
    START("/start", "начать общение"),
    HELP("/help", "Помощь"),
    SETTING("/settings", "Настройки"),
    NERD_TERM("/что такое ", "Сленг програмиста");

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
