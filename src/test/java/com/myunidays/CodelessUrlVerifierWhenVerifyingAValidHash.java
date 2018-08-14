package com.myunidays;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class CodelessUrlVerifierWhenVerifyingAValidHash {

    private CodelessUrlVerifier studentApiHelper;

    @Before
    public void before() {
        String key = "tnFUmqDkq1w9eT65hF9okxL1On+d2BQWUyOFLYE3FTOwHjmnt5Sh/sxMA3/i0od3pV5EBfSAmXo//fjIdAE3cIAatX7ZZqVi0Dr8qEYGtku+ZRVbPSmTcEUTA/gXYo3KyL2JqXaZ/qhUvCMbLWyV07qRiFOjyLdOWhioHlJM5io=";

        studentApiHelper = new CodelessUrlVerifier(key);
    }

    @Test
    public void whenVerifyingUrlParametersThenTheDateIsCorrect() throws Exception {
        String ud_s = "eesNa1l1bUWKHsWfOLemXQ==";
        String ud_t = "1420070400";
        String ud_h = "qaOotWTdl1GjooDmgagETc4ov8FPo4U7rE5RDp0Gfnmo4UVe5JDQhQYDgi1CXNwYa8xSXE4B0QmM96kqf4DLsw==";

        Optional<ZonedDateTime> verified = studentApiHelper.verifyUrlParams(ud_s, ud_t, ud_h);

        assertThat(verified.isPresent(), is(true));
        assertThat(verified.get(), is(equalTo(ZonedDateTime.ofInstant(Instant.ofEpochSecond(1420070400), ZoneOffset.UTC))));
    }

    @Test
    public void whenVerifyingUrlThenTheDateIsCorrect() throws Exception {
        URI uri = URI.create("https://test.com?ud_s=eesNa1l1bUWKHsWfOLemXQ%3D%3D&ud_t=1420070400&ud_h=qaOotWTdl1GjooDmgagETc4ov8FPo4U7rE5RDp0Gfnmo4UVe5JDQhQYDgi1CXNwYa8xSXE4B0QmM96kqf4DLsw%3D%3D");

        Optional<ZonedDateTime> verified = studentApiHelper.verifyUrl(uri);

        assertThat(verified.isPresent(), is(true));
        assertThat(verified.get(), is(equalTo(ZonedDateTime.ofInstant(Instant.ofEpochSecond(1420070400), ZoneOffset.UTC))));
    }
}
