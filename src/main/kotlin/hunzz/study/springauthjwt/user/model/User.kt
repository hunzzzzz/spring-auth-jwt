package hunzz.study.springauthjwt.user.model

import jakarta.persistence.*

@Entity
@Table(name = "users")
class User(
    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "password", nullable = false)
    val password: String,

    @Column(name = "role", nullable = false)
    @Enumerated(value = EnumType.STRING)
    var role: UserRole = UserRole.USER
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, unique = true)
    val id: Long? = null
}