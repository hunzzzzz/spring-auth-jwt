package hunzz.study.springauthjwt.user.controller

import hunzz.study.springauthjwt.user.service.UserService
import org.springframework.stereotype.Controller

@Controller
class UserController(
    private val userService: UserService
) {
}