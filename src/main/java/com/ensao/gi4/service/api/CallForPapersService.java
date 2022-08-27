package com.ensao.gi4.service.api;

import java.util.Optional;

import com.ensao.gi4.model.CallForPapers;

public interface CallForPapersService {
	int add(CallForPapers callForPapers, Long confernceId);
	Optional<CallForPapers> findByConferenceId(Long confernceId); 
	boolean existsByConferenceId(Long confernceId);

}
