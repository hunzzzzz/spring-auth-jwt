package hunzz.study.springauthjwt.user.controller

import hunzz.study.springauthjwt.global.exception.DuplicatedValueException
import hunzz.study.springauthjwt.global.exception.IncorrectPasswordException
import hunzz.study.springauthjwt.user.dto.LoginRequest
import hunzz.study.springauthjwt.user.dto.SignupRequest
import hunzz.study.springauthjwt.user.service.UserService
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.context.annotation.Description
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/users")
class UserController(
    private val userService: UserService,
) {
    @Description("로그인 페이지")
    @GetMapping("/login")
    fun login(model: Model): String {
        model.addAttribute("loginRequest", LoginRequest())
        return "login"
    }

    @Description("로그인")
    @PostMapping("/login")
    fun login(
        model: Model,
        @Valid @ModelAttribute("loginRequest") request: LoginRequest,
        result: BindingResult,
        response: HttpServletResponse
    ): String {
        if (result.hasErrors()) return "login"
        userService.login(request, response)
            .onFailure { return "redirect:/users/login?error" }
        return "redirect:/"
    }

    @Description("회원가입 페이지")
    @GetMapping("/signup")
    fun signupPage(model: Model): String {
        model.addAttribute("signupRequest", SignupRequest())
        return "signup"
    }

    @Description("회원가입")
    @PostMapping("/signup")
    fun signup(
        model: Model,
        @Valid @ModelAttribute("signupRequest") request: SignupRequest,
        result: BindingResult
    ): String {
        if (result.hasErrors()) return "signup"
        userService.signup(request)
            .onFailure {
                when (it.javaClass.simpleName) {
                    DuplicatedValueException::class.simpleName ->
                        result.rejectValue("email", it.javaClass.simpleName, it.message!!)

                    IncorrectPasswordException::class.simpleName ->
                        result.rejectValue("password2", it.javaClass.simpleName, it.message!!)
                }
                return "signup"
            }
        return "redirect:/users/login"
    }
}