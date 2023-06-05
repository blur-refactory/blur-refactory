package com.blur.blurprofile.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.blur.blurprofile.repository.UserInterestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final UserInterestRepository userInterestRepository;

    public List<String> findPopularInterestsByInterestName(String interestName) {
        List<Object[]> results = userInterestRepository.findPopularInterestsExcludingSelectedOne(interestName);
        List<String> popularInterests = new ArrayList<>();

        for (Object[] result : results) {
            popularInterests.add((String) result[0]);
        }

        return popularInterests;
    }
}
