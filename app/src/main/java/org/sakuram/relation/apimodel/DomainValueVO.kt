package org.sakuram.relation.apimodel

class DomainValueVO(
    val id: Long,
    val category: String,
    val value: String,
    val relationGroup: String?,
    val isInputAsAttribute: Boolean?,
    val repetitionType: String?,
    val attributeDomain: String?,
    val isInputMandatory: Boolean?,
    val validationJsRegEx: String?,
    val languageCode: String?,
    val privacyRestrictionType: String?,
    val isScriptConvertible: Boolean?,
)