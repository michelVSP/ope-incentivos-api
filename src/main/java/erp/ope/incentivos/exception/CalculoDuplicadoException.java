package erp.ope.incentivos.exception;

public class CalculoDuplicadoException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

	public CalculoDuplicadoException(String message) {
        super(message);
    }
}