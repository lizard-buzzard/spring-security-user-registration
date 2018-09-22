# spring-security-user-registration

## Project development notes ##
The README.md of the project will be in a form of __log of notes__ (see  [this chapter](#development-log).) made to remember some important features and to avoid and fix errors met on the project progress.
The log will progress from commit to commit.

## How to run the Application ##
The simplest way is to run the command in the project's home:
```text
$ mvn spring-boot:run
```
Otherwise, you can first build a __jar__ file by
```text
$ mvn clean package
```
command, which builds target/spring-security-registration-1.0-SNAPSHOT.jar, and then you can run this jar:
```text
java -jar target/spring-security-registration-1.0-SNAPSHOT.jar

```
Both the ways make Spring to start and to run the application:
```text
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

```
After the server starts, you can enter in the browser's address bar __http://localhost:8761/login__ and the applicaion should show its first __login page__. It will start on __server.port=8761__.

## Application's landscape ##
The application is developed on __Java__, it's web pages are developed on __HTML__ with tiny inclusions of __CSS__ and __Javascript__ fragments. 
On the HTML pages CDN __Bootstrap__ v.4.1.3 stylesheets and __Thymeleaf-4__ templates are used. 

### Environement ###
An environment, used for the development, includes:
* Ubuntu 18.04.1 LTS
* java version "1.8.0_181"
* Apache Maven 3.5.2
* mysql  Ver 8.0.12 for Linux on x86_64 (MySQL Community Server - GPL)
* Google Chrome Version 68.0.3440.106 (Official Build) (64-bit)
* FireFox Quantrum 62.0 (64-bit)

### Spring Boot Maven Project ### 
Maven pom.xml refers to Spring Boot parent project version 2.0.4.RELEAS:
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.0.4.RELEASE</version>
    <relativePath/> <!-- lookup parent from repository -->
</parent>
```
And then it uses following org.springframework.boot dependencies: __spring-boot-starter-web__, __spring-boot-starter-data-jpa__, __spring-boot-starter-security__. 

This project uses [Thymeleaf](https://www.thymeleaf.org/) as a HTML pages template engine and includes __spring-boot-starter-thymeleaf__ in the dependencies.

Also the project dependencies include mysql:mysql-connector-java:5.1.46 dependency.

# Development Log #

## Commit-1 ##

### Thymleaf templates ###
__Thymleaf__ will be used as html pages template engine and for internationalization support. To use __thymeleaf__ you need
* to include dependency in pom.xml
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```
* to define __th:__ namespace on *.html pages as follows:
```
<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-4.dtd">
<html lang="en" xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
```
An example see in templates/login.html

### JpaRepository ###
There is a base repository (BaseRepository) which extends JpaRepository.
 * Spring Data JPA contains some built-in Repository implemented some common functions to work with database: findOne, findAll, save, etc.
 * should be annotated win @NoRepositoryBean, otherwise gives an BeanCreationException (Error creating bean with name 'baseRepository')
 
### MVC Configuration ###
If you want to take complete control of Spring MVC, you can add your own @Configuration annotated with @EnableWebMvc should be set ([27. Developing Web Applications](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html)).
```
public class MvcConfig implements WebMvcConfigurer
``` 
Do not use the src/main/webapp directory if your application is packaged as a jar.
Although this directory is a common standard, it works only with war packaging, and it is silently ignored by most build tools if you generate a jar ([Chapter: 27.1.5 Static Content](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-developing-web-applications.html)).
```
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry)
```
Next bean helped me to show messages in different languages depending on language choosed: 
```
@Bean
public MessageSource messageSource()
```
### PersistenceJPAConfig ###
The  source for @Bean's definition used in Persistence JPA Repository
* Path to BaseRepository, UserRepository, RoleRepository
```
@EnableJpaRepositories(basePackages = "com.lizard.buzzard.persistence.dao")
```
* Works with persistence.properties from ./resource/ folder
```
@PropertySource({ "classpath:persistence.properties" })
```
* Model's (database) entities (Role, User etc.). Used in JpaTransactionManager
```
@Bean
public LocalContainerEntityManagerFactoryBean entityManagerFactory()
```
* Jdbc datasource object, used for LocalContainerEntityManagerFactoryBean
```
@Bean
public  DataSource dataSource()
```
* Transaction Manager for Jpa model entities, use LocalContainerEntityManagerFactoryBean
```
@Bean
public JpaTransactionManager transactionManager()
```
* Get properties from persistence.properties, used in @Bean public LocalContainerEntityManagerFactoryBean entityManagerFactory()
```
protected Properties additionalProperties()
```
### MySQLDialect ###
Should be set for correct working with MySql version currently installed. Otherwise gives an error.
Works in conjanction with __persistence.properties__'s __hibernate.dialect__=CustomMySQLDialect
```
public class CustomMySQLDialect extends MySQL57Dialect
```

## Commit-2 ##
Commit-2 just fixes some inaccuracies in description of Commit-1.

## Commit-3 ##
Internationalization. Pay attention at the link between 'lang_choosed' in html form and in LocaleChangeInterceptor, it's the same.

## Commit-4 ##
Commit-4: an initial _fake_ __registration.html__ addad; Apps moved to highest package to be reached from any test class; attention to two ways of tests: integration and local