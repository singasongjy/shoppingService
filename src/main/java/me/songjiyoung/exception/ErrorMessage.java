package me.songjiyoung.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorMessage {

    DATA_UPSERT_ERROR("데이터 입력중 오류", HttpStatus.BAD_REQUEST),
    BRAND_NOT_FOUND("브랜드가 없습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND("상품이 없습니다.", HttpStatus.NOT_FOUND),
    CATEGORY_NOT_FOUND("카테고리가 없습니다.", HttpStatus.NOT_FOUND),
    BRAND_HAS_PRODUCTS("브랜드에 상품이 존재합니다.", HttpStatus.CONFLICT),
    BRAND_ALREADY_EXISTS("이미 존재하는 브랜드입니다. : %s", HttpStatus.CONFLICT),
    PRODUCT_HAS_DEPENDENCIES("상품이 다른 데이터와 연관되어 있어 삭제할 수 없습니다.", HttpStatus.CONFLICT),
    NO_BRAND_WITH_PRODUCTS_FOR_ALL_CATEGORIES("모든 카테고리에 대해 상품을 가진 브랜드가 없습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_NOT_FOUND_FOR_CATEGORY("해당 카테고리의 상품을 찾을 수 없습니다: %s", HttpStatus.NOT_FOUND);

    private final String errorMessage;
    private final HttpStatus status;

    ErrorMessage(String errorMessage, HttpStatus status) {
        this.errorMessage = errorMessage;
        this.status = status;
    }

    // 메시지 포맷팅 지원
    public String formatMessage(Object... args) {
        return String.format(errorMessage, args);
    }
}
