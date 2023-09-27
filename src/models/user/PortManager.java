package models.user;

import models.port.Port;

public class PortManager extends User {
    private final Port managedPort;

    public PortManager(String username, String password, Port managedPort) {
        super(username, password);
        this.managedPort = managedPort;
    }

    public Port getManagedPort() {
        return managedPort;
    }
}