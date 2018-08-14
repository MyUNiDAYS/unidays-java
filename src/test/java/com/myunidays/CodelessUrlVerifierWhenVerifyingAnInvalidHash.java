package com.myunidays;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.URI;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CodelessUrlVerifierWhenVerifyingAnInvalidHash {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private CodelessUrlVerifier studentApiHelper;

    @Before
    public void before() {
        String key = "tnFUmqDkq1w9eT65hF9okxL1On+d2BQWUyOFLYE3FTOwHjmnt5Sh/sxMA3/i0od3pV5EBfSAmXo//fjIdAE3cIAatX7ZZqVi0Dr8qEYGtku+ZRVbPSmTcEUTA/gXYo3KyL2JqXaZ/qhUvCMbLWyV07qRiFOjyLdOWhioHlJM5io=";

        studentApiHelper = new CodelessUrlVerifier(key);
    }

    @Test
    public void whenVerifyingUrlParametersThenEmptyIsReturned() throws Exception {
        String ud_s = "eesNa1l1bUWKHsWfOLemXQ==";
        String ud_t = "1420070500";
        String ud_h = "qaOotWTdl1GjooDmgagETc4ov8FPo4U7rE5RDp0Gfnmo4UVe5JDQhQYDgi1CXNwYa8xSXE4B0QmM96kqf4DLsw==";

        Optional<ZonedDateTime> verified = studentApiHelper.verifyUrlParams(ud_s, ud_t, ud_h);

        assertThat(verified.isPresent(), is(false));
    }

    @Test
    public void whenVerifyingUrlThenEmptyIsReturned() throws Exception {
        URI uri = URI.create("https://test.com?ud_s=eesNa1l1bUWKHsWfOLemXQ%3D%3D&ud_t=1420070500&ud_h=qaOotWTdl1GjooDmgagETc4ov8FPo4U7rE5RDp0Gfnmo4UVe5JDQhQYDgi1CXNwYa8xSXE4B0QmM96kqf4DLsw%3D%3D");
        Optional<ZonedDateTime> verified = studentApiHelper.verifyUrl(uri);
        assertThat(verified.isPresent(), is(false));
    }

    @Test
    public void whenVerifyingAUrlWithStudentMissingAnExceptionIsThrown() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("URL does not contain the required query parameters");
        URI uri = URI.create("https://test.com?ud_t=1420070500&ud_h=qaOotWTdl1GjooDmgagETc4ov8FPo4U7rE5RDp0Gfnmo4UVe5JDQhQYDgi1CXNwYa8xSXE4B0QmM96kqf4DLsw%3D%3D");
        studentApiHelper.verifyUrl(uri);
    }

    @Test
    public void whenVerifyingAUrlWithTimeMissingAnExceptionIsThrown() throws Exception {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("URL does not contain the required query parameters");
        URI uri = URI.create("https://test.com?ud_s=eesNa1l1bUWKHsWfOLemXQ%3D%3D&ud_h=qaOotWTdl1GjooDmgagETc4ov8FPo4U7rE5RDp0Gfnmo4UVe5JDQhQYDgi1CXNwYa8xSXE4B0QmM96kqf4DLsw%3D%3D");
        studentApiHelper.verifyUrl(uri);
    }
}

