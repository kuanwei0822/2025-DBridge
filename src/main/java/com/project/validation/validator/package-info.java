package com.project.validation.validator;
/**
 * 驗證器套件，包含所有連線驗證器的介面和實作。
 *
 * <p>
 * 核心介面 : Validator 介面
 * - 此套件提供了所有 DB 連線驗證器的介面 Validator。
 * - 它定義了驗證器的基本行為，包括 取得驗證器類型 和 執行驗證邏輯。
 *
 * 核心 Enum : ValidatorTarget Enum
 * - 此套件提供了所有驗證器的種類，使用 Enum 來管理。
 * - 它定義了驗證器的目標類型，例如 PostgreSQL 和 MongoDB。
 *
 * 多個驗證器實作 Validator :
 * - 此套件包含多個驗證器實作類別，這些類別實現了 Validator 介面。
 * - 這些實作類別負責具體的驗證邏輯，例如檢查資料庫連線是否有效。
 *
 * 核心管理類別 : ValidationCheck
 * - 此套件提供了 ValidationCheck 類別，用於管理所有驗證器。
 * - 它使用 EnumMap 儲存所有的 Validator Bean 實例，
 * - 並提供 check(ValidatorTarget) 方法來執行所有種類的驗證。
 *
 * --------------------------------------------------------
 * 如何新增驗證器 :
 * 1. 在 Enum ValidatorTarget 新增一個驗證器類型。
 * 2. 創建一個新的驗證器類別，實現 Validator 介面。
 * 3. 在類別中實現 target() 方法，返回對應的 ValidatorTarget。
 * 4. 在類別中實現 validate() 方法，執行具體的驗證邏輯。
 *
 * --------------------------------------------------------
 * 如何使用驗證器 :
 * 1. 呼叫 ValidationCheck.check(ValidatorTarget) 方法來執行驗證。
 *
 */