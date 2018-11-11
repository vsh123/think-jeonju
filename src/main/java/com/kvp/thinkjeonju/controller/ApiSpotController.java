package com.kvp.thinkjeonju.controller;

import javax.servlet.http.HttpSession;

import com.kvp.thinkjeonju.dto.SpotDTO;
import com.kvp.thinkjeonju.security.LoginUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kvp.thinkjeonju.dto.LikeToDTO;
import com.kvp.thinkjeonju.dto.MemberDTO;
import com.kvp.thinkjeonju.exception.common.DataBaseException;
import com.kvp.thinkjeonju.service.SpotService;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/api/spots")
public class ApiSpotController {

	@Autowired
	private SpotService spotService;
	
	@PostMapping("/{spotId}/spotLike")
	public ResponseEntity<Integer> setSpotLike(@PathVariable String spotId, @LoginUser MemberDTO user) {
		log.debug("[좋아요] {} 장소 좋아요 추가", spotId);
		spotService.setSpotLike(new LikeToDTO(user.getId(), spotId, 's'));
		return new ResponseEntity(spotService.getLikeCnt(spotId), HttpStatus.OK);		
	}
	
	@DeleteMapping("/{spotId}/spotLike")
	public ResponseEntity<Integer> cancelSpotLike(@PathVariable String spotId, @LoginUser MemberDTO user) {
		log.debug("[좋아요] {} 장소 좋아요 취소", spotId);
		spotService.cancelSpotLike(new LikeToDTO(user.getId(), spotId, 's'));
		return new ResponseEntity(spotService.getLikeCnt(spotId), HttpStatus.OK);
	}

	@PostMapping("/search")
	public ResponseEntity<ArrayList<SpotDTO>> getSpotData(@RequestParam(value = "dataValue") String dataValue, HttpSession session) {

		log.debug("[Search] Schedule에서 Spot Data 검색");

		ArrayList<SpotDTO> spots = spotService.getSpotData(dataValue);
		MemberDTO m = (MemberDTO)session.getAttribute("loginUser");

		if(m != null) {
			spots = spotService.setLikeInSpotDTOs(m, spots);
		}

		return new ResponseEntity(spots, HttpStatus.OK);


	}
}
