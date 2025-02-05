package no.nav.helse.hops.cases

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import no.nav.helse.hops.ResourceLoader
import no.nav.helse.hops.fhir.toJson
import no.nav.helse.hops.models.HjelpemiddelRequest
import no.nav.helse.hops.models.digihot.DigihotKvittering
import no.nav.helse.hops.testdata.createDeviceRequest
import no.nav.helse.hops.testdata.createMessageBundle
import org.hl7.fhir.r4.model.DeviceRequest
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertTrue

internal class DigihotTilFhirMapperTest {

    @Test
    fun `Skal mappe digithot til FHIR`() {
        val digihot = ResourceLoader.asString("/digihot.json")
        val deviceRequests = mutableListOf<DeviceRequest>()
        val digihotKvittering = Json.decodeFromString<DigihotKvittering>(digihot)
        digihotKvittering.soknad.soknad.hjelpemidler.hjelpemiddelListe.forEach { hjelpemiddel ->
            deviceRequests.add(
                createDeviceRequest(
                    HjelpemiddelRequest(
                        beskrivelse = hjelpemiddel.beskrivelse,
                        brukerFnr = digihotKvittering.fodselNrBruker,
                        brukerNavn = digihotKvittering.soknad.soknad.bruker.fornavn + " " + digihotKvittering.soknad.soknad.bruker.etternavn,
                        innsenderFnr = digihotKvittering.fodselNrInnsender,
                        innsenderNavn = "Frode Fysioterapaut",
                        produktEkstra = hjelpemiddel.tilleggsinformasjon,
                        produktHmsNr = hjelpemiddel.hmsNr,
                        produktNavn = hjelpemiddel.navn,
                        soknadDate = LocalDate.parse(digihotKvittering.soknad.soknad.date),
                        soknadId = digihotKvittering.soknad.soknad.id,
                    )
                )
            )
            hjelpemiddel.tilbehorListe?.forEach { tilbehor ->
                deviceRequests.add(
                    createDeviceRequest(
                        HjelpemiddelRequest(
                            beskrivelse = null,
                            brukerFnr = digihotKvittering.fodselNrBruker,
                            brukerNavn = digihotKvittering.soknad.soknad.bruker.fornavn + " " + digihotKvittering.soknad.soknad.bruker.etternavn,
                            innsenderFnr = digihotKvittering.fodselNrInnsender,
                            innsenderNavn = "Frode Fysioterapaut",
                            produktEkstra = null,
                            produktHmsNr = tilbehor.hmsnr,
                            produktNavn = tilbehor.navn,
                            soknadDate = LocalDate.parse(digihotKvittering.soknad.soknad.date),
                            soknadId = digihotKvittering.soknad.soknad.id,
                        )
                    )
                )
            }
        }
        val bundle = createMessageBundle("hjelpemiddelKvittering", deviceRequests)
        val bundleJson = bundle.toJson()
        assertTrue(bundleJson.contains("Bundle"))
        assertTrue(bundleJson.startsWith("{"))
        assertTrue(bundleJson.endsWith("}"))
    }
}
