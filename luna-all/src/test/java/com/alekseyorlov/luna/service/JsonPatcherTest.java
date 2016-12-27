package com.alekseyorlov.luna.service;

import com.alekseyorlov.luna.config.TestConfig;
import com.alekseyorlov.luna.dto.Patch;
import com.alekseyorlov.luna.dto.patch.Operation;
import com.alekseyorlov.luna.util.ObjectPatcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {TestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
public class JsonPatcherTest {

    @Autowired
    private ObjectPatcher patcher;

    @Test
    public void shouldAddPropertyValue() throws IOException {

        // given
        TestDTO testDTO = new TestDTO();
        testDTO.setA("a");

        Patch patch = new Patch();
        Operation operation = new Operation();
        operation.setType(Operation.Type.REPLACE);
        operation.setPath("/a");
        operation.setValue("b");
        patch.setOperations(Arrays.asList(operation));

        // when
        TestDTO patchedTestDTO = patcher.patch(
                testDTO,
                patch,
                TestDTO.class);

        // then
        assertEquals("b", patchedTestDTO.getA());
    }

}

class TestDTO {

    private String a;

    public String getA() {
        return a;
    }

    public void setA(String a) {
        this.a = a;
    }
}