package br.com.igorfurtado.authcnab;


import java.math.BigDecimal;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.TaskExecutorJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.Range;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {
  private final PlatformTransactionManager transactionManager;
  private final JobRepository jobRepository;

  public BatchConfig(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
    this.transactionManager = transactionManager;
    this.jobRepository = jobRepository;
  }

  @Bean
  Job job(Step step) {
    return new JobBuilder("job", jobRepository)
        .start(step)
        .build();
  }

  @Bean
  Step step(
      FlatFileItemReader<TransacaoCNAB> reader,
      ItemProcessor<TransacaoCNAB, Transacao> processor,
      ItemWriter<Transacao> writer) {
    return new StepBuilder("step", jobRepository)
        .<TransacaoCNAB, Transacao>chunk(1000, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }

  @StepScope
  @Bean
  FlatFileItemReader<TransacaoCNAB> reader(
      @Value("#{jobParameters['cnabFile']}") Resource resource) {
    return new FlatFileItemReaderBuilder<TransacaoCNAB>()
        .name("reader")
        .resource(resource)
        .linesToSkip(1)
        .fixedLength()
        .columns(
          new Range(1, 3), 
          new Range(4, 4), 
          new Range(5, 20), 
          new Range(21, 36),
          new Range(37, 52), 
          new Range(53, 80))
        .names("tipo","tipotran", "valor", "contaorigem", "contadestino", "reservado")
        .targetType(TransacaoCNAB.class)
        .build();
  }

  @Bean
  ItemProcessor<TransacaoCNAB, Transacao> processor() {
    return item -> {
      if ("003".equals(item.tipo())) { // Ou outro critério
        return null; // Retornar null faz com que o item seja ignorado
      }else{
      String somenteDigitos = item.valor().replaceAll("_", "");
      somenteDigitos = somenteDigitos.trim();
      BigDecimal valor = new BigDecimal(somenteDigitos);
      var valorNormalizado = valor
          .divide(new BigDecimal(100));

      var transacao = new Transacao(
          null, 
          item.tipo(), 
          item.tipotran(),
          valorNormalizado,
          item.contaorigem(),
          item.contadestino(),
          item.reservado() 
          );

      return transacao;
      }
    };
  }

  @Bean
  JdbcBatchItemWriter<Transacao> writer(DataSource dataSource) {
    return new JdbcBatchItemWriterBuilder<Transacao>()
        .dataSource(dataSource)
        .sql("""
              INSERT INTO transacao (
                tipo, tipotran, valor, conta_origem, conta_destino, reservado
              ) VALUES (
                :tipo, :tipotran, :valor, :contaorigem, :contadestino, :reservado
              )
            """)
        .beanMapped()
        .build();
  }

  @Bean
  JobLauncher jobLauncherAsync(JobRepository jobRepository) throws Exception {
    TaskExecutorJobLauncher jobLauncher = new TaskExecutorJobLauncher();
    jobLauncher.setJobRepository(jobRepository);
    jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor());
    jobLauncher.afterPropertiesSet();
    return jobLauncher;
  }
}