package com.ags.learn.testing.SpringBootTestCases.controller;

import com.ags.learn.testing.SpringBootTestCases.entity.City;
import com.ags.learn.testing.SpringBootTestCases.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CityController {

    @Autowired
    CityService cityService;

    @GetMapping("city/{id}")
    public ResponseEntity<City> get(@PathVariable("id") Long id){
        City city= cityService.get(id);
        if (city != null ) {
//            return new ResponseEntity<>(city,
//                    HttpStatus.OK);
            return  ResponseEntity.status(HttpStatus.OK).body(city);
        }else{
             throw new RuntimeException();
        }
    }

    @GetMapping("/city")
    public ResponseEntity<List<City>> getAll() {
        //return new ResponseEntity<>(cityService.getAll(),HttpStatus.OK);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(cityService.getAll());
    }

    @PostMapping("/city")
    public ResponseEntity<City> add(@RequestBody City city) {
        city.setId(0L);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(cityService.add(city));
        //return new ResponseEntity<>(cityService.add(city),HttpStatus.OK);
    }

    @PutMapping("/city/{id}")
    public ResponseEntity<City> update(@PathVariable Long id,
                                       @RequestBody City city) {
        city.setId(id);

        City findCity = cityService.get(id);
        if(findCity != null){
            City cityStatus = cityService.update(city);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(cityStatus);
            //return new ResponseEntity<>(cityStatus,HttpStatus.OK);
        }else{
            throw new RuntimeException();
        }
    }

    @DeleteMapping("/city/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        City findCity = cityService.get(id);
        if(findCity != null){
            String status = cityService.delete(findCity);
            return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(status);
            //return new ResponseEntity<>(status,HttpStatus.OK);
        }else{
            throw new RuntimeException();
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> noCityFound(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).contentType(MediaType.APPLICATION_JSON).body("No city found");
    }

}
