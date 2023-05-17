package com.travel.role.domain.schedule.controller;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.travel.role.domain.schedule.dto.request.ScheduleModifyRequestDTO;
import com.travel.role.domain.schedule.dto.request.ScheduleRequestDTO;
import com.travel.role.domain.schedule.dto.response.ScheduleResponseDTO;
import com.travel.role.domain.schedule.service.ScheduleService;
import com.travel.role.global.auth.token.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/room/{roomId}/schedule")
public class ScheduleController {
	private final ScheduleService scheduleService;

	@GetMapping
	public List<ScheduleResponseDTO> getSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
		return scheduleService.getSchedule(userPrincipal.getEmail(), roomId, date);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void addSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestBody @Valid ScheduleRequestDTO scheduleRequestDTO) {
		scheduleService.addSchedule(userPrincipal.getEmail(), roomId, scheduleRequestDTO);
	}

	@DeleteMapping
	public void deleteSchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestParam("ids") List<Long> scheduleIds) {
		scheduleService.deleteSchedule(userPrincipal.getEmail(), roomId, scheduleIds);
	}

	@PutMapping
	public void modifySchedule(@AuthenticationPrincipal UserPrincipal userPrincipal,
		@PathVariable("roomId") Long roomId,
		@RequestBody @Valid ScheduleModifyRequestDTO scheduleModifyRequestDTO) {
		scheduleService.modifySchedule(userPrincipal.getEmail(), roomId, scheduleModifyRequestDTO);
	}
}
