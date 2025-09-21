package br.com.igorfurtado.authcnab;

import org.springframework.batch.core.JobExecutionException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.item.file.FlatFileParseException;
import org.springframework.batch.item.file.transform.FlatFileFormatException;
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

    @ExceptionHandler(FlatFileParseException.class)
    private ResponseEntity<Object> handleFlatFileItemReaderException(FlatFileParseException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body("arquivo com formato inválido " + ex.getLineNumber() + " : " + ex.getInput());
    }

    @ExceptionHandler(JobExecutionException.class)
    private ResponseEntity<Object> handleJobFailedException(JobExecutionException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao processar o arquivo: " + ex.getMessage());
    }
}
