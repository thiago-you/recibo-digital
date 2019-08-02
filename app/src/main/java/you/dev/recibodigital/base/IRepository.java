package you.dev.recibodigital.base;

public interface IRepository {
    /**
     * Check if record is already persisted
     */
    boolean exists(int id);

    /**
     * Save record (create/update)
     */
    IModel save(IModel entity);

    /**
     * Create new record
     */
    IModel create(IModel entity);

    /**
     * Update record
     */
    IModel update(IModel entity);

    /**
     * Delete record
     */
    void delete(IModel entity);

    /**
     * Find record
     */
    IModel find(int id);
}
