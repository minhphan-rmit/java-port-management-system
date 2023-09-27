package interfaces.CRUD;

/**
 * Interface defining the CRUD operations for containers.
 * CRUD stands for Create, Read, Update, and Delete, which are the basic operations for persistent storage.
 */
public interface ContainerCRUD {

    /**
     * Creates a new container.
     */
    void createNewContainer();

    /**
     * Finds a specific container based on certain criteria.
     */
    void findContainer();

    /**
     * Displays all the containers.
     */
    void displayAllContainers();

    /**
     * Updates the details of a specific container.
     */
    void updateContainer();

    /**
     * Deletes a specific container.
     */
    void deleteContainer();
}
