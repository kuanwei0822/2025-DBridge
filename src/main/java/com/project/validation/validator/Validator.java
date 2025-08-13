package com.project.validation.validator;

/**
 * Validator 介面，所有連線驗證器都應實現此介面。
 */
public interface Validator {

    /**
     * 取得 驗證器的類型。
     */
    ValidatorTarget target();

    /**
     * 執行 驗證邏輯。
     */
    boolean validate();

}
