package com.realestate.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.realestate.dto.PropertySearchRequest;
import com.realestate.model.SearchHistory;
import com.realestate.model.User;
import com.realestate.repository.SearchHistoryRepository;
import com.realestate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SearchHistoryService {
    
    @Autowired
    private SearchHistoryRepository searchHistoryRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    public SearchHistory saveSearchHistory(Long userId, PropertySearchRequest searchRequest, int resultsCount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        
        SearchHistory history = new SearchHistory();
        history.setUser(user);
        history.setSearchCity(searchRequest.getCity());
        history.setSearchState(searchRequest.getState());
        history.setSearchPropertyType(searchRequest.getPropertyType());
        history.setSearchListingType(searchRequest.getListingType());
        history.setMinPrice(searchRequest.getMinPrice());
        history.setMaxPrice(searchRequest.getMaxPrice());
        history.setMinBedrooms(searchRequest.getMinBedrooms());
        history.setMaxBedrooms(searchRequest.getMaxBedrooms());
        history.setMinBathrooms(searchRequest.getMinBathrooms());
        history.setMaxBathrooms(searchRequest.getMaxBathrooms());
        history.setMinSquareFeet(searchRequest.getMinSquareFeet());
        history.setMaxSquareFeet(searchRequest.getMaxSquareFeet());
        history.setResultsCount(resultsCount);
        
        try {
            history.setSearchCriteria(objectMapper.writeValueAsString(searchRequest));
        } catch (Exception e) {
            // Ignore JSON serialization errors
        }
        
        return searchHistoryRepository.save(history);
    }
    
    public List<SearchHistory> getUserSearchHistory(Long userId) {
        return searchHistoryRepository.findByUserIdOrderBySearchedAtDesc(userId);
    }
    
    public List<SearchHistory> getUserRecentSearches(Long userId, int limit) {
        return searchHistoryRepository.findByUserIdOrderBySearchedAtDesc(userId, PageRequest.of(0, limit));
    }
    
    public void deleteSearchHistory(Long historyId) {
        searchHistoryRepository.deleteById(historyId);
    }
    
    public void clearUserSearchHistory(Long userId) {
        searchHistoryRepository.deleteByUserId(userId);
    }
    
    public void deleteOldSearchHistory(int daysOld) {
        LocalDateTime cutoffDate = LocalDateTime.now().minusDays(daysOld);
        searchHistoryRepository.deleteBySearchedAtBefore(cutoffDate);
    }
    
    public List<Map.Entry<String, Long>> getMostSearchedCities(int limit) {
        List<Object[]> results = searchHistoryRepository.findMostSearchedCities(PageRequest.of(0, limit));
        return results.stream()
                .map(row -> Map.entry((String) row[0], (Long) row[1]))
                .collect(Collectors.toList());
    }
    
    public List<Map.Entry<String, Long>> getMostSearchedPropertyTypes() {
        List<Object[]> results = searchHistoryRepository.findMostSearchedPropertyTypes();
        return results.stream()
                .map(row -> Map.entry(row[0].toString(), (Long) row[1]))
                .collect(Collectors.toList());
    }
    
    public Long getUserSearchCount(Long userId) {
        return searchHistoryRepository.countByUserId(userId);
    }
}
