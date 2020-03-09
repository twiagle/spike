package com.twigle.spike.exception;

import com.twigle.spike.result.CodeMsg;

public class GlobalException extends RuntimeException {
    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
