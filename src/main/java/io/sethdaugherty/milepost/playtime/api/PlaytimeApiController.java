package io.sethdaugherty.milepost.playtime.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.sethdaugherty.milepost.api.data.PositionRepository;
import io.sethdaugherty.milepost.model.Position;
import io.sethdaugherty.milepost.playtime.data.PlaytimeRepository;
import io.sethdaugherty.milepost.playtime.model.AddPlaytimeRequest;
import io.sethdaugherty.milepost.playtime.model.AddPlaytimeResponse;
import io.sethdaugherty.milepost.playtime.model.GetPlaytimeResponse;
import io.sethdaugherty.milepost.playtime.model.Playtime;

/**
 * TODO: everything in the .playtime.* package really should be a separate app. It's not directly related to the MilePost app
 */
@Controller
@RequestMapping("/playtime")
public class PlaytimeApiController {

	private PlaytimeRepository playtimeRepository;
	
	@Autowired
	public PlaytimeApiController(PlaytimeRepository playtimeRepository) {
		this.playtimeRepository = playtimeRepository;
	}
	
	// TODO: add security
	/**
	 * Increase the given day's counter.
	 * 
	 * @param date
	 * @param positionExternalModelList
	 * @return
	 */
	@RequestMapping(value="/{date}", method=RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<AddPlaytimeResponse> addPlaytimeForDate(@PathVariable("date") String date, @RequestBody AddPlaytimeRequest addPlaytimeRequest) {

		Playtime playtime = new Playtime(addPlaytimeRequest.getTimestampStarted(), addPlaytimeRequest.getMillisSpent(), date, addPlaytimeRequest.getUrl());
		playtimeRepository.save(playtime);
		
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		
		
		return new ResponseEntity<AddPlaytimeResponse>(new AddPlaytimeResponse("successful"), responseHeaders, HttpStatus.CREATED);
	}
	
	/** 
	 * Get the number of millis spent today.
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{date}", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<GetPlaytimeResponse> getPlaytimeForDate(@PathVariable("date") String date) {
		
		List<Playtime> playtimeList = playtimeRepository.findAllByDate(date);
		
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.set("Access-Control-Allow-Origin", "*");
		
		if (playtimeList.isEmpty()) {
			return new ResponseEntity<GetPlaytimeResponse>(new GetPlaytimeResponse(0), responseHeaders, HttpStatus.CREATED);
		}
		
		long millisSpent = playtimeList.stream().mapToLong(playtime -> playtime.getMillisSpent()).sum();

		return new ResponseEntity<GetPlaytimeResponse>(new GetPlaytimeResponse(millisSpent), responseHeaders, HttpStatus.CREATED);
	}
}
