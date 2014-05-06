package jenkins.plugins.slakk;

public interface SlakkService {
    void publish(String message);

    void publish(String message, String color);
}
