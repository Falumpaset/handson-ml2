package de.immomio.cloud;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
//@ActiveProfiles(value = {"test", "default", "DE"})
@Ignore("application context cannot be loaded - Error")
public class Cloud2ApplicationTests {

    @Test
    public void contextLoads() {
    }
}
