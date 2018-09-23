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
For the HTML pages CDN __Bootstrap__ v.4.1.3 stylesheets and __Thymeleaf-4__ templates are used. 

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


## The Database configuration and creation ##
To run the code you should have MySQL server installed. In order to install it please refer, for example, to [How To Install MySQL on Ubuntu 14.04](https://www.digitalocean.com/community/tutorials/how-to-install-mysql-on-ubuntu-14-04) and [B.5.3.2 How to Reset the Root Password](https://dev.mysql.com/doc/refman/5.7/en/resetting-permissions.html). After the server is installed you need to create a user to connect to the database:
```sql
mysql -u root -p 
> CREATE USER 'user1'@'localhost' IDENTIFIED BY 'user123';
> GRANT ALL PRIVILEGES ON *.* TO 'user1'@'localhost';
> FLUSH PRIVILEGES;
``` 
__/resources/persistence.properties__ file contains properties which allow to create and to connect to the database with name of __'lizard_users_db'__: 
```
jdbc.driverClassName=com.mysql.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/lizard_users_db?createDatabaseIfNotExist=true
jdbc.user=user1
jdbc.pass=user123
init-db=false
################### Hibernate Configuration ##########################
hibernate.dialect=com.lizard.buzzard.persistence.CustomMySQLDialect
hibernate.show_sql=false
hibernate.hbm2ddl.auto=update

hibernate.id.new_generator_mappings = false
```
The database __'lizard_users_db'__ created (spring.jpa.hibernate.ddl-auto=update property) if it not yet exists and __'user1'__ has access to the database (see [81. Database Initialization](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-database-initialization.html) and [Spring boot ddl auto generator](https://stackoverflow.com/questions/21113154/spring-boot-ddl-auto-generator)). 
The property spring.jpa.properties.hibernate.dialect refers to CustomMySQLDialect class.
```java
public class CustomMySQLDialect extends MySQL57Dialect {
    @Override
    public boolean dropConstraints() {
        return false;
    }
}
``` 
It's important to have this class corresponds to the dialect of the database you work with (see [SQL Dialects in Hibernate](https://www.javatpoint.com/dialects-in-hibernate)). As on the computer where the development was done 

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
It works together with __persistence.properties__'s __hibernate.dialect__=CustomMySQLDialect
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

## --Commit-13-- ##
The commit supports Spring MVC Custom Validation of User's DTO 'password' field: passay.org library's custom Message Resolver for different languages, depending on __'lang_choosed'__ login.html HttpRequest parameter.

In order to understand how it works, please refers to [Guide to Internationalization in Spring Boot](https://www.baeldung.com/spring-boot-internationalization)

Pay attention to next pair of methods of the __MvcConfig__ class which implements __WebMvcConfigurer__ interface. The __addInterceptors()__ method adds the interceptor which will switch to a new locale based on the value of the __lang_choosed__ parameter appended to a request:
```
@Override
public void addInterceptors(final InterceptorRegistry registry) {
    final LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
    localeChangeInterceptor.setParamName("lang_choosed");
    registry.addInterceptor(localeChangeInterceptor);
}
```
In order for our application to be able to determine which locale is currently being used, we need to add a __LocaleResolver__ bean:
```
@Bean
public LocaleResolver localeResolver() {
    final CookieLocaleResolver cookieLocaleResolver = new CookieLocaleResolver();
    cookieLocaleResolver.setDefaultLocale(Locale.ENGLISH);
    return cookieLocaleResolver;
}	
```
The __@Autowired LocaleResolver__ 
```
@Autowired
LocaleResolver localeResolver;
```
is used in __RegisterController__ class to print the Locale selected on login.html
```
@RequestMapping(value = {"/login", "/"}, method = RequestMethod.GET)
public String getLoginPage(ViewFormLogin viewFormLogin, Model model, HttpServletRequest httpServletRequest) {
    LOGGER.debug("Locale selected on login.html ==>  " + localeResolver.resolveLocale(httpServletRequest).toString());
```
and it is used in __PasswordConstraintValidatorImpl__ class which implements __ConstraintValidator<PasswordConstraintValidator, String>__, in __MessageResolver getMessageResolver()__ method to get the name of a file with password's error messages:
```
String pattern = "loc-pass-messages/password_messages_%s.properties";
HttpServletRequest httpServletRequest =
        ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
String resourceName =
        String.format(pattern,localeResolver.resolveLocale(httpServletRequest).toString());

MessageResolver resolver = null;
InputStream resourceInputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
InputStreamReader reader = new InputStreamReader(resourceInputStream, Charset.forName("UTF-8"));
try {
    Properties props = new Properties();
    props.load(reader);
    resolver = new PropertiesMessageResolver(props);
} catch (IOException e) {
    e.printStackTrace();
}
return resolver;
}
```
## --Commit-14-- ##
Some clean up was done in the messages_en.properties, messages_ru_RU.properties and in templates/login.html.

## --Commit-15-- ##
The __password confirmation validator__ (the Validation on a Class object, not on a class's field !), Thymeleaf's __global errors__ processing were added.

In order to be able to compare two fields of the same DTO class, we have to implement an annotation with __ElementType.TYPE__ argument in its __@Target__ annotation.

The __PasswordConfirmationValidator__ annotation interface and __PasswordConfirmationValidatorImpl__ class were added, they implement the validation of that the __confirmed password__ field is the same as the __password__ field.
For annotating of classes with this annotation, it has __ElementType.TYPE__ argument in its __@Target__ annotation:
```
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.TYPE})
```
In order to check whether the confirmed password field is the same as the password field, we have to annotate our __ViewFormUser__ DTO class with the annotation __@PasswordConfirmationValidator__:
```
@PasswordConfirmationValidator
public class ViewFormUser {
``` 
For the sake of experiment, the following code was added in the __checkPersonInfo()__ method of the __RegisterController__ class:
```
ObjectError confirmedPasswordErrMsg = bindingResult.getGlobalError();
if(confirmedPasswordErrMsg != null) {
    LOGGER.debug("Global error (@PasswordConfirmationValidator) ==>" + confirmedPasswordErrMsg.getDefaultMessage());
    model.addAttribute("confirmedPasswordError", confirmedPasswordErrMsg.getDefaultMessage());
}
```
This code is not used, it's an alternative to __Thymeleaf__'s global-errors prosessing (see [Thymeleaf: 7.3 Global errors](https://www.thymeleaf.org/doc/tutorials/3.0/thymeleafspring.html#global-errors)).

In html <form> of templates/registration.html this  __Thymeleaf__'s code checks for the global error and shows it in __<span id="confirmedPassportError" ...>__

```html
<span id="confirmedPassportError" class="alert alert-danger col-sm-4" th:if="${#fields.hasErrors('global')}" th:errors="*{global}">confirmed password error</span>

```
## --Commit-16-- ##
An intermediate, not finished transition toward Spring security. The dependency is added:
```html
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
The main code changes are in:
- RegisterController: 
    - call UserServiceImpl.saveUserInRepository(), 
    - publish ApplicationEvent (AfterUserRegisteredEvent.class); 
- SecurityConfig adds two methods for PasswordEncoder and configure(HttpSecurity http) to allow anonymous access to login.html; 
- User class adds @Column private boolean enabled; 
- VerificationToken @Entity joined @OneToOne to User.class holds Date expirationDate field; 
- UserServiceImpl.class with saveUserInRepository(ViewFormUser dtoUser); 
- class UserAlreadyExistException extends RuntimeException.

Custom property __lizard.config.properties__ added.
__UserAlreadyExistException extends RuntimeException__ added.

On this step a development of a verification procedure with help of the link in the e-mail, which includes the verification token, started.
Please refer to [Registration – Activate a New Account by Email](https://www.baeldung.com/registration-verify-user-by-email) for additional reading. 

In the controller's __@RequestMapping(value = "/registration", method = RequestMethod.POST)__ method has the code for generating the event on basis of which an e-mail with verification token will be send:
```
User newRegisteredUser = userService.saveUserInRepository(viewFormUser);
ApplicationEvent event = new AfterUserRegisteredEvent(newRegisteredUser, request.getLocale(), getAppUri(request));
applicationEventPublisher.publishEvent(event);
```
This code includes [Spring Events](https://www.baeldung.com/spring-events) (additional reading is [Better application events in Spring Framework 4.2](https://spring.io/blog/2015/02/11/better-application-events-in-spring-framework-4-2))

The new entity which is intended to store a verification token in the database is added. The verification token is a key artifact through which a user is verified ([Registration – Activate a New Account by Email](https://www.baeldung.com/registration-verify-user-by-email))
```
@Entity
@Table(name = "verification_property")
@PropertySource("classpath:lizard.config.properties")
@ConfigurationProperties(prefix = "lizard")
public class VerificationToken {
```
There are some changes for User class. The most important is: be aware that the BCrypt algorithm generates a String of length 60, so we need to make sure that the password will be stored in a column that can accommodate it
([Registration with Spring Security – Password Encoding](https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt)).
```
@NotNull
@Column(length = 60)
private String password;
```
A simple version of __class SecurityConfig extends WebSecurityConfigurerAdapter__ is started: 
```
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
```
The WebSecurityConfig class is annotated with __@EnableWebSecurity__ to enable Spring Security’s web security support and provide the __Spring MVC integration__. It also extends __WebSecurityConfigurerAdapter__ and overrides a couple of its methods to set some specifics of the web security configuration ([Securing a Web Application]https://spring.io/guides/gs/securing-web/).

This class includes __@Bean public PasswordEncoder__ for encoding the users' passwords: 
```
@Bean
public PasswordEncoder passwordEncoder() {
  return new BCryptPasswordEncoder(17);
}
```
__BCryptPasswordEncoder__'s parameter is a strength - the log rounds to use, between 4 and 31, "strength" (a.k.a. log rounds in BCrypt) and a SecureRandom instance. The larger the strength parameter the more work will have to be done (exponentially) to hash the passwords.

## --Commit-17-- ##
The fix of small bugs.


