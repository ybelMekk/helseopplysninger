package no.nav.helse.hops.testdata

import no.nav.helse.hops.fhir.addResource
import no.nav.helse.hops.fhir.toUriType
import org.hl7.fhir.r4.model.Bundle
import org.hl7.fhir.r4.model.Communication
import org.hl7.fhir.r4.model.DeviceRequest
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.InstantType
import org.hl7.fhir.r4.model.MessageHeader
import org.hl7.fhir.r4.model.Reference
import org.hl7.fhir.r4.model.Resource
import org.hl7.fhir.r4.model.UriType
import java.util.UUID

fun createMessageBundle(messageEvent: String, data: List<Resource>) = Bundle().apply {
    id = UUID.randomUUID().toString()
    timestampElement = InstantType.withCurrentTime()
    type = Bundle.BundleType.MESSAGE
    addResource(createMessageHeader(messageEvent))
    data.forEach { addResource(it) }
}

fun createMessageHeader(messageEvent: String) = MessageHeader().apply {
    id = UUID.randomUUID().toString()
    event = UriType(messageEvent)
    source = MessageHeader.MessageSourceComponent().apply {
        endpoint = messageEvent
    }
}

fun createCommunicationAboutDeviceRequsts(
    soknadsId: String,
    data: List<Resource>
) = Communication().apply {
    id = UUID.randomUUID().toString()
    identifier = listOf(
        Identifier().apply {
            system = "http://digihot"
            value = soknadsId
        }
    )
    status = Communication.CommunicationStatus.COMPLETED
    about = data.filter {
        it.resourceType == DeviceRequest().resourceType
    }.map {
        Reference(it.idElement.toUriType().value)
    }
}
