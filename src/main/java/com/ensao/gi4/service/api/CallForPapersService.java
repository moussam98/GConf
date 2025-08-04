package com.ensao.gi4.service.api;

import com.ensao.gi4.dto.CallForPapersDto;
import com.ensao.gi4.dto.CallForPapersRequestDto;

import java.util.Optional;

public interface CallForPapersService {
	Optional<CallForPapersDto> add(CallForPapersRequestDto callForPapersRequestDto, Long conferenceId);
	Optional<CallForPapersDto> findByConferenceId(Long conferenceId);

}
