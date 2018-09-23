package com.lizard.buzzard.validation;

import org.passay.*;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class PasswordConstraintValidatorImpl implements ConstraintValidator<PasswordConstraintValidator, String> {

    // SEE: MvcConfig @Configuration, LocaleResolver @Bean and also addInterceptors(...) method
    @Autowired
    LocaleResolver localeResolver;

    private DictionaryRule dictionaryRule;

    @Override
    public void initialize(PasswordConstraintValidator constraintAnnotation) {
        // SEE: https://memorynotfound.com/custom-password-constraint-validator-annotation/
        try {
            String invalidPasswordList = this.getClass().getResource("/prohibited-passwords.txt").getFile();
            dictionaryRule = new DictionaryRule(
                    new WordListDictionary(WordLists.createFromReader(
                            // Reader around the word list file
                            new FileReader[]{
                                    new FileReader(invalidPasswordList)
                            },
                            // True for case sensitivity, false otherwise
                            false,
                            // Dictionaries must be sorted
                            new ArraysSort()
                    )));
        } catch (IOException e) {
            throw new RuntimeException("could not load word list", e);
        }
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        // SEE: https://github.com/vt-middleware/passay/blob/master/src/test/java/org/passay/CharacterCharacteristicsRuleTest.java
        List<Rule> rules = Arrays.asList(
                new LengthRule(8, 30) // at least 8 characters
                , new CharacterRule(EnglishCharacterData.UpperCase, 1) // at least one upper-case character
                , new CharacterRule(EnglishCharacterData.LowerCase, 1) // at least one lower-case character
                , new CharacterRule(EnglishCharacterData.Digit, 1) // at least one digit character
                , new WhitespaceRule() // no whitespace
                , dictionaryRule // no common passwords
                //, new CharacterRule(EnglishCharacterData.Special, 1) // at least one symbol (special character)
        );
        MessageResolver messageResolver = getMessageResolver();
        PasswordValidator validator = messageResolver==null ? new PasswordValidator(rules) : new PasswordValidator(messageResolver,rules);

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = messages.stream().collect(Collectors.joining(", "));

        constraintValidatorContext
                .buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }

    private MessageResolver getMessageResolver() {
        String pattern = "loc-pass-messages/password_messages_%s.properties";
        HttpServletRequest httpServletRequest =
                ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // an alternative way is: httpServletRequest.getLocale().toString();
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
}
