package cn.littleterry.exception;

import com.baomidou.mybatisplus.extension.api.IErrorCode;

/**
 * @author terry
 * @since 19/08/2019
 */
public class ApiException extends RuntimeException {

    /**
     * 错误码
     */
    private IErrorCode errorCode;

    public ApiException(IErrorCode errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

    public ApiException(String message) {
        super(message);
    }

    public ApiException(Throwable cause) {
        super(cause);
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
    }

    public IErrorCode getErrorCode() {
        return errorCode;
    }
}

