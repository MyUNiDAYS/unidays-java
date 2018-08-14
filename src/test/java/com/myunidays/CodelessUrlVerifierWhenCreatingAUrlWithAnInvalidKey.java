package com.myunidays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class CodelessUrlVerifierWhenCreatingAUrlWithAnInvalidKey {

    private final String key;
    private final String expectedMessage;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    public CodelessUrlVerifierWhenCreatingAUrlWithAnInvalidKey(String key, String expectedMessage) {

        this.key = key;
        this.expectedMessage = expectedMessage;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"", "Key cannot be empty"},
                {null, "Key cannot be null"}
        });
    }

    @Test
    public void ThenAnArgumentExceptionIsThrown() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(this.expectedMessage);

        new CodelessUrlVerifier(key);
    }
}
