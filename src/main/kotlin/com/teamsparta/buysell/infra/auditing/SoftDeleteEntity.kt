package com.teamsparta.buysell.infra.auditing

import jakarta.persistence.Column
import jakarta.persistence.MappedSuperclass

@MappedSuperclass
abstract class SoftDeleteEntity : BaseEntity() {
    @Column(name = "is_deleted")
    var isDeleted: Boolean = false
}