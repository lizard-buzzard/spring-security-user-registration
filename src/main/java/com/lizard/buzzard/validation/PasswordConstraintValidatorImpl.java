package com.lizard.buzzard.validation;

import org.passay.*;
import org.passay.dictionary.WordListDictionary;
import org.passay.dictionary.WordLists;
import org.passay.dictionary.sort.ArraysSort;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PasswordConstraintValidatorImpl implements ConstraintValidator<PasswordConstraintValidator, String> {

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
        PasswordValidator validator = new PasswordValidator(
                Arrays.asList(
                        new LengthRule(8, 30) // at least 8 characters
                        , new CharacterRule(EnglishCharacterData.UpperCase, 1) // at least one upper-case character
                        , new CharacterRule(EnglishCharacterData.LowerCase, 1) // at least one lower-case character
                        , new CharacterRule(EnglishCharacterData.Digit, 1) // at least one digit character
                        , new WhitespaceRule() // no whitespace
                        , dictionaryRule // no common passwords
                        //, new CharacterRule(EnglishCharacterData.Special, 1) // at least one symbol (special character)
                )
        );

        RuleResult result = validator.validate(new PasswordData(password));

        if (result.isValid()) {
            return true;
        }

        List<String> messages = validator.getMessages(result);
        String messageTemplate = messages.stream().collect(Collectors.joining(","));

        constraintValidatorContext
                .buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation();
        return false;
    }
}
