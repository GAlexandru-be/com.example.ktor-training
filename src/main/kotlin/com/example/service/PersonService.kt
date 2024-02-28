package com.example.service

import com.example.data.Person
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.id.toId

class PersonService {
    private val client = KMongo.createClient()
    private val database = client.getDatabase("person")
    private val personCollection = database.getCollection<Person>()

    fun create(person: Person): Id<Person>? {
        personCollection.insertOne(person)
        return person.id
    }

    fun findById(id: String): Person? {
        val bsonId: Id<Person> = ObjectId(id).toId()
        return personCollection
            .findOne(Person::id eq bsonId)
    }

    fun findAll(): List<Person> =
        personCollection.find()
            .toList()
}