package com.travel.role.domain.wantplace.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.travel.role.domain.wantplace.entity.WantPlace;
import com.travel.role.domain.wantplace.repository.WantPlaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WantPlaceReadService {
	private WantPlaceRepository wantPlaceRepository;

	public List<WantPlace> findWantPlaceByRoomId(Long roomId) {

		return wantPlaceRepository.findWantPlaceByRoomId(roomId);
	}
}
