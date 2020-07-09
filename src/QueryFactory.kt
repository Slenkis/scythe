package com.slenkis

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Time : Table() {
    override val primaryKey: PrimaryKey?
        get() = PrimaryKey(id)
    val id = integer("id").default(0)
    val value = integer("value").default(0)
}

class QueryFactory(url: String, driver: String, username: String, password: String) {
    init {
        Database.connect(url, driver, username, password)

        transaction {
            SchemaUtils.create(Time)
            try {
                Time.insert {
                    it[id] = 0
                    it[value] = 0
                }
            } catch (e: Exception) {
                // ignore if row exist
            }
        }
    }

    fun addMinutes(minutes: Int) {
        transaction {
            Time.update {
                it[id] = 0
                it[value] = minutes + getMinutes()
            }
        }
    }

    fun getMinutes() = transaction {
        Time.select { Time.id eq 0 }.first()[Time.value]
    }

    fun resetMinutes() {
        transaction {
            Time.update {
                it[id] = 0
                it[value] = 0
            }
        }
    }

}