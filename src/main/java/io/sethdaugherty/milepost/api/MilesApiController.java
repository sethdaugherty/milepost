package io.sethdaugherty.milepost.api;

import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.sethdaugherty.milepost.api.data.PositionRepository;
import io.sethdaugherty.milepost.date.DateUtils;
import io.sethdaugherty.milepost.model.Position;
import io.sethdaugherty.milepost.model.external.ListPositionsResponse;
import io.sethdaugherty.milepost.playtime.model.AddPlaytimeRequest;
import io.sethdaugherty.milepost.playtime.model.AddPlaytimeResponse;
import io.sethdaugherty.milepost.playtime.model.Playtime;
import io.sethdaugherty.milepost.service.PositionService;

@Controller
@RequestMapping("/miles")
public class MilesApiController {


    private PositionService positionService;

    @Autowired
    public MilesApiController(PositionService positionService) {
        this.positionService = positionService;
    }

    /**
     * Get the polished, outliers-removed version of miles for the particular date
     * 
     * @param date
     * @return
     */
    @RequestMapping(value = "/list/{date}", method = RequestMethod.GET, produces = "application/json; charset=UTF-8")
    public ResponseEntity<ListPositionsResponse> listPositionsForDate(@PathVariable("date") String date,
            @RequestParam("timezone") String timezoneString) {
        TimeZone timeZone = TimeZone.getTimeZone(timezoneString);

        DateTime startDateTime = DateUtils.computeStartDateTime(date, timeZone);
        DateTime endDateTime = DateUtils.computeEndDateTime(date, timeZone);

        // TODO: move this to an offline, batch process that runs once a day and stores
        // the results in a table
        List<Position> positions = positionService.findPositionsWithoutOutliers(startDateTime, endDateTime);

        return createListPositionResponse(positions);
    }
    
    private ResponseEntity<ListPositionsResponse> createListPositionResponse(List<Position> positions) {
        List<io.sethdaugherty.milepost.model.external.Position> externalPositionList = positions.stream()
                .map(Position::toExternal).collect(Collectors.toList());
        ListPositionsResponse response = new ListPositionsResponse();
        response.setPositions(externalPositionList);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");

        return new ResponseEntity<ListPositionsResponse>(response, responseHeaders, HttpStatus.CREATED);
    }
}
