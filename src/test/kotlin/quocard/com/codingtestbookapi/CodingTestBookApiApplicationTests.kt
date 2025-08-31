package quocard.com.codingtestbookapi

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(
    properties = [
        "spring.autoconfigure.exclude=" +
                "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
                "org.springframework.boot.autoconfigure.jooq.JooqAutoConfiguration"
    ]
)
class CodingTestBookApiApplicationTests {
    @Test fun contextLoads() {}
}
