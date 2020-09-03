package net.corda.training.flow


import org.junit.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
//import net.corda.jackson.JacksonSupport
import net.corda.client.jackson.JacksonSupport


import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.core.util.DefaultIndenter
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.LinearState
import net.corda.core.contracts.UniqueIdentifier
import net.corda.core.identity.AbstractParty
import net.corda.core.schemas.MappedSchema
import net.corda.core.schemas.PersistentState
import net.corda.core.schemas.QueryableState
import net.corda.core.serialization.CordaSerializable
import java.util.*

// abstract class all our states inherit from to extract consistent fields
abstract class TinaState(
        @JsonProperty(value = "relatedCaseGroupId")
        open val relatedCaseGroupId: UUID? = null,
        @JsonProperty("linearId")
        override val linearId: UniqueIdentifier = UniqueIdentifier()
) : LinearState, QueryableState

// example state
class TestState : TinaState() {
    @get:JsonProperty("linearId")
    override val linearId: UniqueIdentifier
        get() = super.linearId

    @get:JsonProperty("relatedCaseGroupId")
    override val relatedCaseGroupId: UUID?
        get() = super.relatedCaseGroupId

    @JsonProperty
    val testValue = "yah"

    @get:JsonIgnore
    override val participants: List<AbstractParty>
        get() = emptyList()

    override fun generateMappedObject(schema: MappedSchema): PersistentState {
        return PersistentState()
    }

    override fun supportedSchemas(): Iterable<MappedSchema> {
        return listOf()
    }
}



class SerializationTest {

    @Test
    fun `Test state serialization`() {
// instance of test state
        val state = TestState()
// regular ObjectMapper to act as a control.
        val om = ObjectMapper().registerKotlinModule()
        val asJsonOm = om.writeValueAsString(state)
// extension function that calls a constant nonRPCMapper to write
// value to string.
        val mapper = JacksonSupport.createNonRpcMapper()
        val asJsonNonRPC = mapper.writeValueAsString(state)

        println("From Object Mapper: $asJsonOm")
        println("From nonRPCMapper: $asJsonNonRPC")

    }
}


