# sociodbbatch
A Springboot Batch Application which Updates Registered Socios and their Bank Accounts

1) General Info About the Socio-Micro-Service-Demo

2) Specific Info Concerning Each Single Application


## General Info ====

The Socio Micro Services Project will consist of about 10 small (backend) Springboot applications, deployed in a Docker Container/ Linux Oracle Virtual Box. SocioRegister is the principal part of a series of four applications called: starter, mock, jpa, socioregister. Together they show a stepwise buildup to a Springboot REST application, which contains use-cases for registering and adding Socios (similar to Facebook). This line of applications goes from an almost empty Springboot shell (starter: one controller method only) to a small but full-fledged REST application: SocioRegister which will be used as a component of our micro-services.

Next you`ll find four other serving applications. The simple SocioWeather, provides a weather-report by city by consulting an external REST-service called Open Weather. SocioBank, permits money transaction between Socios, also consulting an external service for exchange rates. The SocioSecurity, a Cookie/ Token based SpringSecurity (OAUTH2), still has to be written. Finally the SocioDbBatch application is interesting because it will update, on a daily bases, the databases of SocioRegister (socio_db) and SocioBank (soicio_bank_db). The DBs run on MySQL or Postgres.

From SocioRegister-jpa one finds backend-Validation (javax) and REST-Exception Handeling of Spring (RestControllerAdvice).

Testing, in general, will have an important focus and since we are dealing with Spring(boot) there will systematically testing based on five mayor strategies:

	-@ExtendWith(MockitoExtension.class)

	-@ExtendWith(SpringExtension.class) standalone setup (two ways)

	-@ExtendWith(SpringExtension.class) server tests (@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)

	-@DataJpaTest wich is database testing on H2

	-Spring Batch testing

Testing is still "work in progress"



## Specific Info SocioDbBatch ====

The second SpringBatch sociodbbatch runs on a schedular (see ScheduledTasks class at com.artsgard.sociodbbatch) and updates several DB tables each 24 hours. The Batch runs on three seperated DBs 

  1) Batch Meta-Data db socio_batch_meta_data_db
  
  2) The SocioRegister db socio_db
  
  3) The SocioBank db socio_bank_db

For the moment they are all Postgres, but soon I will implement a different vendor for each (MySQL meat-data, Postrgres SocioRegister, Oracle-thin SocioBank).

### What are Batches in General

A Batch (Spring-Batch) can be pretty complicated, but its concept is rather simple: Process (big) data on the basis of a single line/ record. The number of records processed in memory is called a chunck (similar to paginate of a JPA or REST-service). The core-part of this process is the step which consits of a reader-processor-writer. A step can also be a tasklet which does on single thing like e.g. clearing a db-table. At com.artsgard.sociodbbatch.config BatchFlowConfig you may observe a step process of two steps each opdating a different table 1) SocioModel 2) SocioAssociatedSocio (with chunks of 20 records/ lines each):

  @Bean
  public Step socioStep() throws Exception {
    return stepBuilders.get("batchdbsocioStep-socio")
                .<SocioModel, SocioModel>chunk(20)
                .reader(socioReader)
                .processor(socioProcessor)
                .writer(socioWriter)
                .transactionManager(socioTransactionManager)
                .build();
    }
    
    @Bean
    public Step associatedSocioStep() throws Exception {
        return stepBuilders.get("batchdbsocioStep-associated")
                .<SocioAssociatedSocio, SocioAssociatedSocio>chunk(20)
                .reader(associatedReader)
                .processor(associatedProcessor)
                .writer(associatedWriter)
                .transactionManager(socioTransactionManager)
                .build();
    }

### What does SocioDbBatch do
