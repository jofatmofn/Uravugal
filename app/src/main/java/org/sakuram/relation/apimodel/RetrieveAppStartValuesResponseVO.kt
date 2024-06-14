package org.sakuram.relation.apimodel

class RetrieveAppStartValuesResponseVO(
    val domainValueVOList: List<DomainValueVO>,
    val loggedInUser: String?,
    val inUseProject: String?,
    val inUseLanguage: Long,
)