package br.com.igorfurtado.authcnab;

import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(JobInstanceAlreadyCompleteException.class)
    private ResponseEntity<Object> handleJobInstanceAlreadyCompleteException(JobInstanceAlreadyCompleteException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("O arquivo já foi executado com sucesso anteriormente");
    }
}
