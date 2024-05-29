package hunzz.study.springauthjwt.user.service

import hunzz.study.springauthjwt.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {
}