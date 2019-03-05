package no.nav.kafka;

import java.util.Objects;

public class Poststed {
    private final String postnummer;
    private final String stedsnavn;
    private final String kommune;

    public Poststed(String postnummer, String stedsnavn, String kommune) {
        this.postnummer = postnummer;
        this.stedsnavn = stedsnavn;
        this.kommune = kommune;
    }

    public String getPostnummer() {
        return postnummer;
    }

    public String getStedsnavn() {
        return stedsnavn;
    }

    public String getKommune() {
        return kommune;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poststed poststed = (Poststed) o;
        return postnummer.equals(poststed.postnummer) &&
            stedsnavn.equals(poststed.stedsnavn) &&
            kommune.equals(poststed.kommune);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postnummer, stedsnavn, kommune);
    }

    @Override
    public String toString() {
        return "Poststed{" +
            "postnummer='" + postnummer + '\'' +
            ", stedsnavn='" + stedsnavn + '\'' +
            ", kommune='" + kommune + '\'' +
            '}';
    }
}
