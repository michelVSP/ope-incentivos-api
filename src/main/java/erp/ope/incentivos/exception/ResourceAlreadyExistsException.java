package erp.ope.incentivos.exception;

public class ResourceAlreadyExistsException extends RuntimeException 
{
	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistsException(String mensaje) 
	{
		super(mensaje);
	}
	
}
