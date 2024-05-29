package hunzz.study.springauthjwt.user.service

import hunzz.study.springauthjwt.global.exception.DuplicatedValueException
import hunzz.study.springauthjwt.global.exception.IncorrectPasswordException
import hunzz.study.springauthjwt.user.dto.SignupRequest
import hunzz.study.springauthjwt.user.repository.UserRepository
import org.springframework.context.annotation.Description
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) {
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
}