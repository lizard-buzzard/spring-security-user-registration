# spring-security-user-registration

## Project development notes ##
The README.md of the project will be in a form of __log of notes__ (see [this chapter](#development-log).), made to remember some important features and to avoid and fix errors met on the project progress.
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

## --Commit-1-- ##

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

## --Commit-2-- ##
Commit-2 just fixes some inaccuracies in description of Commit-1.

## --Commit-3-- ##
Internationalization. Pay attention at the link between 'lang_choosed' in html form and in LocaleChangeInterceptor, it's the same.

## --Commit-4-- ##
Commit-4: an initial _fake_ __registration.html__ addad; Apps moved to highest package to be reached from any test class; attention to two ways of tests: integration and local

## --Commit-5-- ##
The registration form shows three fields, internationalized messages added in resource files. Remember @Controller vs. @RestController difference
* __@Controller__ vs. __@RestController__ difference

    NOTE: [Spring rest controller not returning html](https://stackoverflow.com/questions/43263667/spring-rest-controller-not-returning-html/43371228).

    __RestController__ annotation returns the json from the method, not HTML or JSP. It is the combination of __@Controller__ and __@ResponseBody__ in one. The main purpose of __@RestController__ is to create __RESTful__ web services. For returning html or jsp, simply annotated the controller class with __@Controller__.

## --Commit-6-- ##
A preparation for registration of a new user: 
- ApplicationListener<ContextRefreshedEvent> to initialize DB entities; 
- utility methods in BaseRepository for finding objects; 
- new RolesPrivilege @Entity joined with Role @Entity; 
- User @Entity extended by email field; 
- first not working draft of UserService; 
- separation of @Entity and DTO for User, ViewFormUser introduced for server-side fields check

### ApplicationListener<ContextRefreshedEvent> ###
 * ContextRefreshedEvent. This event is published when the ApplicationContext is either initialized or refreshed ([Event Handling in Spring](https://www.tutorialspoint.com/spring/event_handling_in_spring.htm)).
 * We use this to create and populate DB tables

### DTO User class ViewFormUser ###
Entity is class mapped to table. Dto is class mapped to "view" layer mostly. What needed to store is entity & which needed to 'show' on web page is DTO ([JAVA: Difference between Entity and DTO](https://stackoverflow.com/questions/39397147/java-difference-between-entity-and-dto)).
 
## --Commit-7-- ##
New fields and buttons on registration form were added

## --Commit-8-- ##
The commit works with the simple Hibernate Validator-JSR 380 and Thymeleaf template engine for internationalizing HTML forms' fields names and error messaging. __Neither BindingResult nor plain target object for bean name ‘mybean’ available as request attribute.__ error resolved.

### Thymeleaf Java library ###
__Thymeleaf__ is a Java library. It is an __XML/XHTML/HTML5 template engine__ able to apply a set of transformations to template files in order to display data and/or text produced by your applications.
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```
### JSR 380 specification, the validation-api dependency ###
JSR 380 specification, the validation-api dependency
* [java bean validation 2.0 vs hibernate validator](https://stackoverflow.com/questions/49606316/java-bean-validation-2-0-vs-hibernate-validator)
* For html forms validation we use Thymeleaf views - see: <artifactId>spring-boot-starter-thymeleaf</artifactId>

Hibernate Validator requires an implementation of the Unified Expression Language (JSR 341) for evaluating dynamic expressions in constraint violation messages (see Section 4.1, “Default message interpolation”).
When your application runs in a Java EE container such as JBoss AS, an EL implementation is already provided by the container. In a Java SE environment, however, you have to add an implementation as dependency to your POM file.
 ([Hibernate Validator 6.0.13.Final - JSR 380 Reference Implementation: Reference Guide](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/))
 
__ViewFormUser__ uses Hibernate Validator annotations.

### Exception: 'Neither BindingResult nor plain target object for bean name ‘mybean’ available as request attribute' ###
If you are trying to bind a __Spring MVC form__ to a __bean__, then you might have come across the below exception.
To solve this, you have to add a __@ModelAttribute annotation__ before your bean argument in the controller action with the __POST__ request method.

Here is __viewFormUser__ object of the form:
```html
<form action="#" th:action="@{/registration}" th:object="${viewFormUser}" method="post">
```
In the controller we first have to initialize it by adding an __attribute__ with the same name (__viewFormUser__) to the __Model__ by 'empty' bean's (DTO) object. 
```
@RequestMapping(value = "/registration", method = RequestMethod.GET)
public String showRegistrationForm(ViewFormUser viewFormUser, Model model) {
    model.addAttribute("viewFormUser", new ViewFormUser());
    return "registration";
}
```
Then in the __RequestMethod.POST__ method we have to to add a __@ModelAttribute("viewFormUser")__ annotation before bean argument:
```
@RequestMapping(value = "/registration", method = RequestMethod.POST)
public String checkPersonInfo(@Valid @ModelAttribute("viewFormUser") ViewFormUser viewFormUser, BindingResult bindingResult) {...}
```
See also:
 * [Neither BindingResult nor plain target object available as request attribute](https://codingexplained.com/coding/java/spring-framework/neither-bindingresult-nor-plain-target-object-available-as-request-attribute).
 * [Neither BindingResult nor plain target object for bean name available as request attribute [duplicate]](https://stackoverflow.com/questions/8781558/neither-bindingresult-nor-plain-target-object-for-bean-name-available-as-request)
 * [Spring MVC - Form Validation with JSR-349 Bean Validation](https://www.logicbig.com/tutorials/spring-framework/spring-web-mvc/spring-mvc-form-validation.html)

### @Valid annotation before the parameter in controller's method ###
__@Valid__ before __ViewFormUser viewFormUser__ means that __JSR-303__ bean validation with the Hibernate Validator implementation is used. If you do not, then you can simply remove the annotations from the bean class as well as the @Valid annotation in the arguments of the controller action.
In order to provide validation in the __@Controller__ apply __@Valid__ annotation. This is __JSR-349__ specific annotation.
The annotation __@Valid__ used on instance or parameter marks it to be validated by the framework. For __@Valid__ annotation see 
[Hibernate Validator 6.0.12.Final - JSR 380 Reference Implementation: Reference Guide](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#preface)

### No mapping found for HTTP request with URI in DispatcherServlet with name 'dispatcherServlet' ###
If __o.s.web.servlet.PageNotFound : No mapping found for HTTP request with URI in DispatcherServlet with name 'dispatcherServlet'__ message appears in the Spring log then

* [Why does Spring MVC respond with a 404 and report “No mapping found for HTTP request with URI […] in DispatcherServlet”?](https://stackoverflow.com/questions/41577234/why-does-spring-mvc-respond-with-a-404-and-report-no-mapping-found-for-http-req/42785538)
* [Annotation Configuration Replacement for mvc:resources - Spring](https://stackoverflow.com/questions/14861720/annotation-configuration-replacement-for-mvcresources-spring)

## --Commit-9-- ##
The login form got new fields with Hibernate Validator-JSR 380 and test Thymeleaf messaging.

## --Commit-10-- ##
Spring MVC Custom Validation of User's DTO Email field: on basis of ConstraintValidator interface implementation, Hibernate Validator-JSR 380, Thymeleaf for internationalized error messaging.

Added dependency for e-mail sending (see [Guide to Spring Email](https://www.baeldung.com/spring-email) how to send emails from a Spring Boot application):
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```
### Spring MVC Custom Validation ###
An examlpe of custom validation annotation is the __EmailConstraintValidator__ interface: 
```
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailConstraintValidatorImpl.class)
public @interface EmailConstraintValidator {
    String message() default "{constraint.validator.user.registration.email}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
```
Normally error message keys are being searched in the file called __ValidationMessages.properties__ that should be available on the application class path (see [Custom validation annotation in Spring](http://dolszewski.com/spring/custom-validation-annotation-in-spring/)), but in our case it was sufficient to define message key in Thymeleaf's __messages_xx.properties__.
Also see [Spring MVC Custom Validation](https://www.baeldung.com/spring-mvc-custom-validator).

## --Commit-11-- ##
The commit supports Spring MVC Custom Validation of User's DTO __Email__ field. __ConstraintValidator__ interface was implemented by __EmailConstraintValidatorImpl__ class, it uses __Hibernate Validator-JSR 380__, __Thymeleaf__ for internationalized __email__ field error message on registration form.

## --Commit-12-- ##
The commit supports Spring MVC Custom Validation of User's DTO __password__ field. __ConstraintValidator__ interface was implementated, Hibernate Validator-JSR 380, Thymeleaf are used for internationalized error messages.
On how to implement the password validation see [Registration – Password Strength and Rules](https://www.baeldung.com/registration-password-strength-and-rules).

Include this dependency in your pom.xml:
```
<dependency>
    <groupId>org.passay</groupId>
    <artifactId>passay</artifactId>
    <version>${passay.version}</version>
</dependency>
```

For implementation of __public void initialize(PasswordConstraintValidator constraintAnnotation)__ method of __PasswordConstraintValidatorImpl__ I recommend to refer to [Custom Password Constraint Validator Annotation Example](https://memorynotfound.com/custom-password-constraint-validator-annotation/).
And for implementation of __public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext)__ method refer to [passay/src/test/java/org/passay/CharacterCharacteristicsRuleTest.java](https://github.com/vt-middleware/passay/blob/master/src/test/java/org/passay/CharacterCharacteristicsRuleTest.java).





