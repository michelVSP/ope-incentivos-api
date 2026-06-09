package erp.ope.incentivos.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler
{

    @ExceptionHandler(CalculoDuplicadoException.class)
    public ResponseEntity<ErrorResponse> handleCargaDuplicada(CalculoDuplicadoException ex,HttpServletRequest  request) {
    	log.error("Ya existe un calculo del incentivo para el periodo y año ingresado", ex);
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "CONFLICT",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarRecursoNoEncontrado(RecursoNoEncontradoException ex,HttpServletRequest  request) {
    	log.error("Error Recurso no encontrado", ex);
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Recurso no encontrado",
                ex.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyExists(ResourceAlreadyExistsException ex,HttpServletRequest  request)
    {
    	log.error("Error Recurso ya existe", ex);
    	ErrorResponse resp = new ErrorResponse(
    			LocalDateTime.now(),
    			HttpStatus.CONFLICT.value(),
    			"Error Recurso ya existe",
    			ex.getMessage(),
                request.getRequestURI()
    			);
        return new ResponseEntity<ErrorResponse>(resp, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex,HttpServletRequest  request)
    {
    	log.error("Error Peticion invalida", ex);
    	ErrorResponse resp = new ErrorResponse(
    			LocalDateTime.now(),
    			HttpStatus.BAD_REQUEST.value(),
    			"Error Peticion invalida",
    			ex.getMessage(),
                request.getRequestURI()
    			);
        return new ResponseEntity<ErrorResponse>(resp, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex,HttpServletRequest  request) {
        log.error("Error inesperado no controlado", ex);

        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Error",
                "Ocurrió un error interno al procesar la solicitud.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}