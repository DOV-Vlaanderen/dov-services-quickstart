
package be.vlaanderen.dov.services.hfmetingen.example;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt;
import be.vlaanderen.dov.services.hfmetingen.dto.Meetpunt.Meetstatus;

/**
 * Demo class to demonstrate how to upload multiple measurement points.
 *
 * @author Patrick De Baets
 *
 */
public class UploadSensorMeetpunten extends UploadSensorMeetpunt {

    private int nItems;

    public UploadSensorMeetpunten() {
        nItems = 10;
    }

    public UploadSensorMeetpunten(int items) {
        this.nItems = items;
    }

    @Override
    protected List<Meetpunt> createBody() {
        List<Meetpunt> results = new ArrayList<>();
        OffsetDateTime today = OffsetDateTime.now();

        // add nItems random points, with 1s interval
        IntStream.rangeClosed(1, nItems).forEach(i -> {
            results.add(new Meetpunt(today.plusSeconds(i).format(Meetpunt.FORMATTER), 10.66 * Math.random(),
                    Meetstatus.GEVALIDEERD));
        });
        return results;
    }
}
