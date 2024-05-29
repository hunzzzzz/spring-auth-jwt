package hunzz.study.springauthjwt.user.dto

import hunzz.study.springauthjwt.user.model.User
import hunzz.study.springauthjwt.user.model.UserRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

data class SignupRequest(
    @field:NotBlank(message = "이름은 필수 입력 항목입니다.")
    val name: String,

    @field:NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @field:Email(message = "올바른 이메일 형식이 아닙니다.")
    val email: String,

    @field:NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @field:Pattern(
        regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&+=]).{8,15}$",
        message = "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상 15자 이하여야 합니다"
    )
    var password: String,

    @field:NotBlank(message = "비밀번호를 다시 한 번 입력해주세요.")
    val password2: String
) {
    constructor() : this("", "", "", "")

    fun to() = User(
        name = name,
        email = email,
        password = password,
        role = UserRole.USER
    )

    fun encryptPassword(encryptedPassword: String) {
        this.password = encryptedPassword
    }
}