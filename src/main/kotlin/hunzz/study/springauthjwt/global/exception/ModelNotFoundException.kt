package hunzz.study.springauthjwt.global.exception

class ModelNotFoundException(value: String) : RuntimeException("해당 ${value}를 찾을 수 없습니다.")