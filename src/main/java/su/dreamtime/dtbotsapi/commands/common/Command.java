package su.dreamtime.dtbotsapi.commands.common;

public class Command {
    public String name;
    public String data;
    public boolean response = false;
    public Command(String name, String data) {
        this.name = name;
        this.data = data;
    }

    public boolean isResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getName() {
        return name;
    }

    public String getData() {
        return data;
    }
}
