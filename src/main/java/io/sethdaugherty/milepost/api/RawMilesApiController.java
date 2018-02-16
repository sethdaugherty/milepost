package io.sethdaugherty.milepost.api;

import java.util.Collections;
import java.util.List;
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
import org.springframework.web.bind.annotation.ResponseBody;

import io.sethdaugherty.milepost.api.data.PositionRepository;
import io.sethdaugherty.milepost.model.Position;
import io.sethdaugherty.milepost.model.external.ListPositionsResponse;
import io.sethdaugherty.milepost.playtime.model.AddPlaytimeRequest;
import io.sethdaugherty.milepost.playtime.model.AddPlaytimeResponse;
import io.sethdaugherty.milepost.playtime.model.Playtime;

@Controller
@RequestMapping("/api")
public class RawMilesApiController {

    private PositionRepository positionRepository;
    
    @Autowired
    public RawMilesApiController(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }
    
    // TODO: add security
    // TODO: get rid of the date field. It's not necessary
    @RequestMapping(value="/date/{date}", method=RequestMethod.POST)
    public String addPositionsForDate(@PathVariable("date") String date, @RequestBody List<io.sethdaugherty.milepost.model.external.Position> positionExternalModelList) {
        // TODO: add error handling
        List<Position> positionInternalModelList = positionExternalModelList.stream()
                .map(pos -> Position.fromExternal(pos))
                .collect(Collectors.toList());
        
        positionRepository.save(positionInternalModelList);
        
        // TODO: fix the return of this
        return "positionList";
        //return "{\"status\": \"success\"}";
    }
    
    @RequestMapping(value="/list", method=RequestMethod.GET, produces = "application/json; charset=UTF-8") 
    public ResponseEntity<ListPositionsResponse> listPositions() {
        List<Position> positions = positionRepository.findAll();
        
        return createListPositionResponse(positions);
    }
    
    /*    @RequestMapping(value="/list", method=RequestMethod.GET) 
    public String listPositions(Model model) {
        List<Position> positions = positionRepository.findAll();
        
        model.addAttribute("positions", positions);
        
        return "positionList";
    }
    */
    
    @RequestMapping(value="/list/{date}", method=RequestMethod.GET, produces = "application/json; charset=UTF-8") 
    public ResponseEntity<ListPositionsResponse> listPositionsForDate(@PathVariable("date") String date) {
        long startTimestamp = computeStartTimestamp(date);
        long endTimestamp = computeEndTimestamp(date);
        List<Position> positions = positionRepository.findByTimestampBetweenOrderByTimestampAsc(startTimestamp, endTimestamp);
        
        return createListPositionResponse(positions);
    }

    private ResponseEntity<ListPositionsResponse> createListPositionResponse(List<Position> positions) {
        List<io.sethdaugherty.milepost.model.external.Position> externalPositionList = positions.stream().map(Position::toExternal).collect(Collectors.toList());

        io.sethdaugherty.milepost.model.external.Segment segment = new io.sethdaugherty.milepost.model.external.Segment();
        segment.setPositions(externalPositionList);
        ListPositionsResponse response = new ListPositionsResponse();
        response.setSegments(Collections.singletonList(segment));
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Access-Control-Allow-Origin", "*");
        
        
        return new ResponseEntity<ListPositionsResponse>(response, responseHeaders, HttpStatus.CREATED);
    }
    
    
    // TODO: move these somewhere else? Also refactor them to be re-usable
    private long computeStartTimestamp(String date) {
        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(4, 6));
        int day = Integer.valueOf(date.substring(6, 8));
        
        DateTime dateTime = new DateTime(year, month, day, 0, 0);
        
        return dateTime.getMillis();
    }
    
    private long computeEndTimestamp(String date) {
        int year = Integer.valueOf(date.substring(0, 4));
        int month = Integer.valueOf(date.substring(4, 6));
        int day = Integer.valueOf(date.substring(6, 8));
        
        DateTime dateTime = new DateTime(year, month, day, 23, 59, 59, 999);
        
        return dateTime.getMillis();
    }
}
