package swd.project.swdgr3project.exception;

/**
 * Custom exception for DAO operations
 */
public class DAOException extends RuntimeException {
    
    public DAOException(String message) {
        super(message);
    }
    
    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public DAOException(Throwable cause) {
        super(cause);
    }
}
