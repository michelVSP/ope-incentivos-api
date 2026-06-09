package erp.ope.incentivos.exception;

public class BadRequestException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public BadRequestException(String mensaje) 
	{
		super(mensaje);
	}
}
