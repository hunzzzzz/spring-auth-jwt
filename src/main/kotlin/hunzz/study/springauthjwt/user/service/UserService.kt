package hunzz.study.springauthjwt.user.service

import hunzz.study.springauthjwt.global.exception.DuplicatedValueException
import hunzz.study.springauthjwt.global.exception.IncorrectPasswordException
import hunzz.study.springauthjwt.global.exception.ModelNotFoundException
import hunzz.study.springauthjwt.global.jwt.JwtUtil
import hunzz.study.springauthjwt.user.dto.LoginRequest
import hunzz.study.springauthjwt.user.dto.SignupRequest
import hunzz.study.springauthjwt.user.repository.UserRepository
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Description
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {
    @Description("유저의 이메일로 User 객체 획득")
    private fun getUserByEmail(email: String) =
        userRepository.findByEmail(email) ?: throw ModelNotFoundException("유저")

    @Description("회원가입")
    fun signup(request: SignupRequest) =
        kotlin.runCatching {
            request.let {
                checkDuplicationOfEmail(it)
                checkSecondaryPassword(it)
                it.encryptPassword(passwordEncoder.encode(it.password))
                it.to()
            }.let { userRepository.save(it) }
        }

    @Description("회원가입 시, 이메일 중복 확인")
    private fun checkDuplicationOfEmail(request: SignupRequest) {
        if (userRepository.existsByEmail(request.email)) throw DuplicatedValueException("이메일")
    }

    @Description("회원가입 시, 비밀번호 재확인")
    private fun checkSecondaryPassword(request: SignupRequest) {
        if (request.password != request.password2) throw IncorrectPasswordException()
    }

    @Description("로그인")
    fun login(request: LoginRequest, response: HttpServletResponse) =
        kotlin.runCatching {
            request.let {
                checkEmail(it)
                checkPassword(it)
                getUserByEmail(it.email)
            }.let {
                jwtUtil.createToken(it.email, it.role)
            }.let {
                jwtUtil.addJwtToCookie(it, response)
            }
        }

    @Description("로그인 시, 이메일에 해당하는 사용자 여부 확인")
    private fun checkEmail(request: LoginRequest) {
        if (!userRepository.existsByEmail(request.email)) throw ModelNotFoundException("유저")
    }

    @Description("로그인 시, 비밀번호 일치 여부 확인")
    private fun checkPassword(request: LoginRequest) {
        if (!passwordEncoder.matches(
                request.password,
                getUserByEmail(request.email).password
            )
        ) throw IncorrectPasswordException()
    }
}