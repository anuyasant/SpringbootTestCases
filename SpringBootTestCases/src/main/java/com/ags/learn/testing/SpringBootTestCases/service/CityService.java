package com.ags.learn.testing.SpringBootTestCases.service;

import com.ags.learn.testing.SpringBootTestCases.entity.City;
import com.ags.learn.testing.SpringBootTestCases.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CityService {

    @Autowired
    CityRepository cityRepository;

    public City get(Long id){
        Optional<City> city = cityRepository.findById(id);
        if (city.isPresent()) {
            return city.get();
        }else {
            return null;
        }
    }

    public List<City> getAll(){
        return cityRepository.findAll();
    }

    public City add(City city) {
        City savedCity = save(city);
        return savedCity;
    }

    public City update(City city){
        return  save(city);
    }

    private City save(City city){
        City savedCity = cityRepository.save(city);
        return savedCity;
    }

    public String delete(City city) {
        cityRepository.delete(city);
        return "City deleted successfully!";
    }
}
