package hunzz.study.springauthjwt.global.exception

class DuplicatedValueException(value: String) : RuntimeException("중복된 ${value}입니다.")