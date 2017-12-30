package io.sethdaugherty.milepost.api;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import io.sethdaugherty.milepost.api.data.PositionRepository;
import io.sethdaugherty.milepost.model.Position;

@Controller
@RequestMapping("/api")
public class RawMilesApiController {

	private PositionRepository positionRepository;
	
	@Autowired
	public RawMilesApiController(PositionRepository positionRepository) {
		this.positionRepository = positionRepository;
	}
	
	// TODO: add security
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
	
	@RequestMapping(value="/list", method=RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) 
	public @ResponseBody String listPositions(Model model) {
		List<Position> positions = positionRepository.findAll();
		
		model.addAttribute("positions", positions);
		
		return "positionList";
	}
}
