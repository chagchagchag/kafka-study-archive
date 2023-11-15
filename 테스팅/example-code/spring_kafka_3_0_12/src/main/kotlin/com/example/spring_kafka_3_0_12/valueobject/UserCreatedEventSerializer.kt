package com.example.spring_kafka_3_0_12.valueobject

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.fasterxml.jackson.databind.ser.std.UUIDSerializer

class UserCreatedEventSerializer(
    classType : Class<UserCreatedEvent>
) : StdSerializer<UserCreatedEvent>(
    classType
){
    override fun serialize(value: UserCreatedEvent?, gen: JsonGenerator?, provider: SerializerProvider?) {
//        val uuidSerializer = UUIDSerializer
//        val aa = gen?.let {
//            it.writeStartObject()
//            gen
//        }

        TODO("Not yet implemented")
    }

}