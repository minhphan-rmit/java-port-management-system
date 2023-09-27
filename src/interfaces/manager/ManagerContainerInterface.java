package interfaces.manager;

public interface ManagerContainerInterface extends ManagerInterface {
    void loadContainerFlow();
    void unloadContainerFlow();
    void addExistingContainer();
}
