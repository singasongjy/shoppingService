package me.songjiyoung.exception;

public class ApplicationException extends RuntimeException {

    private final ErrorMessage errorMessage;
    private final String formattedMessage;

    public ApplicationException(ErrorMessage errorMessage, Object... args) {
        super(errorMessage.formatMessage(args)); // 상위 클래스 메시지 설정
        this.errorMessage = errorMessage;
        this.formattedMessage = errorMessage.formatMessage(args);
    }

    @Override
    public String getMessage() {
        return formattedMessage; // 포맷팅된 메시지를 반환
    }

    public ErrorMessage getErrorMessageType() {
        return errorMessage; // ErrorMessage Enum 반환
    }
}
