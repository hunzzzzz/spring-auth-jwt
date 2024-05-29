package hunzz.study.springauthjwt

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class SpringAuthJwtApplication

fun main(args: Array<String>) {
    runApplication<SpringAuthJwtApplication>(*args)
}
