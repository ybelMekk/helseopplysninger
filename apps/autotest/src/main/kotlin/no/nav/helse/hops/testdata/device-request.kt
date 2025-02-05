package no.nav.helse.hops.testdata

import no.nav.helse.hops.fhir.Constants
import no.nav.helse.hops.models.HjelpemiddelRequest
import org.hl7.fhir.r4.model.Annotation
import org.hl7.fhir.r4.model.Device
import org.hl7.fhir.r4.model.DeviceRequest
import org.hl7.fhir.r4.model.Identifier
import org.hl7.fhir.r4.model.MarkdownType
import org.hl7.fhir.r4.model.Patient
import org.hl7.fhir.r4.model.Practitioner
import org.hl7.fhir.r4.model.Reference
import java.time.ZoneId
import java.util.Date
import java.util.UUID

/**
 * @see https://www.hl7.org/fhir/devicerequest.html
 */
fun createDeviceRequest(hmRequest: HjelpemiddelRequest) = DeviceRequest().apply {
    id = UUID.randomUUID().toString()
    groupIdentifier = Identifier().apply { value = hmRequest.soknadId }
    status = DeviceRequest.DeviceRequestStatus.DRAFT
    intent = DeviceRequest.RequestIntent.ORDER
    code = createDeviceReference(hmRequest.produktHmsNr, hmRequest.produktNavn)
    subject = createPatientReference(hmRequest.brukerFnr, hmRequest.brukerNavn)
    requester = createPractitionerReference(hmRequest.innsenderFnr, hmRequest.innsenderNavn)
    authoredOn = Date.from(hmRequest.soknadDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
    note = listOf(Annotation(MarkdownType(hmRequest.produktEkstra)))
}

fun createDeviceReference(hmsnr: String, name: String?) = Reference().apply {
    display = name
    type = Device().fhirType()
    identifier = Identifier().apply {
        system = "https://www.hjelpemiddeldatabasen.no"
        value = hmsnr
    }
}

fun createPatientReference(fnr: String, name: String?) = Reference().apply {
    display = name
    type = Patient().fhirType()
    identifier = Identifier().apply {
        system = Constants.OID_FNR
        value = fnr
        use = Identifier.IdentifierUse.OFFICIAL
    }
}

fun createPractitionerReference(fnr: String, name: String?) = Reference().apply {
    display = name
    type = Practitioner().fhirType()
    identifier = Identifier().apply {
        system = Constants.OID_FNR
        value = fnr
    }
}
