package com.blur.bluruser.profile.service;

import com.blur.bluruser.profile.repository.UserInterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
