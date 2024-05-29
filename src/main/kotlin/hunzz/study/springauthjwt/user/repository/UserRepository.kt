package hunzz.study.springauthjwt.user.repository

import hunzz.study.springauthjwt.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long>