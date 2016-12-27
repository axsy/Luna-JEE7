package com.alekseyorlov.luna;

import com.alekseyorlov.luna.config.TestConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles({"test", "integration"})
public class ApplicationTest {

    @Autowired
    private ApplicationContext context;

    @Test
    public void shouldInstantiateApplicationContext() {
        assertThat(context).isNotNull();
    }

}