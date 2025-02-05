package no.nav.helse.hops.fhir.messages

import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.MessageHeader
import org.hl7.fhir.r4.model.Resource

/** See https://www.hl7.org/fhir/messaging.html **/
abstract class BaseMessage(val bundle: Bundle) {
    init {
        require(bundle.entry != null && bundle.entry.count() > 0) { "Message cannot be empty." }
        requireEntry<MessageHeader>(0)
    }

    val header: MessageHeader get() = resource(0)

    protected fun requireEntryCount(count: Int) =
        require(bundle.entry?.count() == count) { "Should be 3 entries." }
    protected inline fun <reified T : Resource> requireEntry(index: Int) =
        require(bundle.entry[index].resource is T) { "Entry[$index] must be a ${T::class.java.simpleName}." }
    protected inline fun <reified R : Resource> resource(index: Int) =
        bundle.entry[index].resource as R
}
