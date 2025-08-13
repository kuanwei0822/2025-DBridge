package com.project.validation;

import com.project.validation.validator.Validator;
import com.project.validation.validator.ValidatorTarget;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;

/**
 * ValidationCheck 類別，用於檢查連線的有效性。
 * 它使用 EnumMap(ValidatorTarget) 儲存所有的 Validator Bean 實例。
 * 以 ValidatorTarget 作為輸入參數，取得對應的 Validator 實例。
 * 這樣可以根據不同的 ValidatorTarget 來選擇對應的驗證器。
 */
@Slf4j
@Component
public class ValidationCheck {

    private final EnumMap<ValidatorTarget, Validator> validators;

    // 建構函數，將所有 Validator 集合轉換為 EnumMap
    public ValidationCheck(List<Validator> validators) {

        EnumMap<ValidatorTarget, Validator> validatorMap = new EnumMap<>(ValidatorTarget.class);

        for (Validator validator : validators) {
            validatorMap.putIfAbsent(validator.target(), validator);
        }
        this.validators = validatorMap;
    }

    // 檢查連線的有效性，傳入 ValidatorTarget 作為參數
    public boolean check(ValidatorTarget target) {
        // 檢查是否有對應的 Validator
        Validator validator = validators.get(target);
        if (validator == null) { // 如果沒有對應的 Validator，則拋出異常
            throw new IllegalArgumentException("No validator for ValidatorTarget");
        }
        // Validator 存在，則執行驗證邏輯
        return validator.validate();
    }
}
