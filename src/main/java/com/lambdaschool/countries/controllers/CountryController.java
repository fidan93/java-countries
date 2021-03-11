package com.lambdaschool.countries.controllers;

import com.lambdaschool.countries.models.Country;
import com.lambdaschool.countries.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CountryController
{
    @Autowired
    CountryRepository countryRepository;
//    http://localhost:2019/names/all
//    http://localhost:2019/names/start/u
//    http://localhost:2019/population/total
//    http://localhost:2019/population/min
//    http://localhost:2019/population/max
    private List<Country> filterCountry(List<Country> theList,CheckCountry tester)
    {
        List<Country> tempList = new ArrayList<>();

        for(Country c: theList)
        {
            if(tester.test(c))
            {
                tempList.add(c);
            }
        }
        return tempList;
    }

    @GetMapping(value = "/names/all",produces = {"application/json"})
    public ResponseEntity<?> findAllCountries()
    {
        List<Country> myList = new ArrayList<>();

        countryRepository.findAll().iterator().forEachRemaining(myList::add);

        myList.sort((c1,c2)-> c1.getName().compareToIgnoreCase(c2.getName()));

        return new ResponseEntity<>(myList,
            HttpStatus.OK);
    }

    @GetMapping(value = "/names/start/{letter}",produces = "application/json")
    public ResponseEntity<?> filterCountriesByLetter(@PathVariable char letter)
    {
        List<Country> myList = new ArrayList<>();
        countryRepository.findAll().iterator().forEachRemaining(myList::add);

        List<Country> rtnList = new ArrayList<>();
        rtnList = filterCountry(myList,(c)-> c.getName().charAt(0) == Character.toUpperCase(letter));
        rtnList.sort((c1,c2) -> c1.getName().compareToIgnoreCase(c2.getName()));
        return  new ResponseEntity<>(rtnList,HttpStatus.OK);
    }

    @GetMapping(value = "/population/total",produces = "application/json")
    public ResponseEntity<?> getTotalPopulation()
    {
        List<Country> myList = new ArrayList<>();
        countryRepository.findAll().iterator().forEachRemaining(myList::add);

        double total = 0;

        for(Country c:myList)
        {
            total = total + c.getPopulation();
        }

        return new ResponseEntity<>("The Total Population is "+ Math.round(total),HttpStatus.OK);
    }

    @GetMapping(value = "/population/min", produces = "application/json")
    public ResponseEntity<?> findMinPopulationCountry()
    {
        List<Country> myList = new ArrayList<>();
        countryRepository.findAll().iterator().forEachRemaining(myList::add);

        myList.sort((c1,c2) -> (int)c1.getPopulation()-(int)c2.getPopulation());
        return new ResponseEntity<>(myList.get(0),HttpStatus.OK);
    }

    @GetMapping(value = "/population/max",produces = "application/json")
    public ResponseEntity<?> findMaxPopulationCountry()
    {
        List<Country> myList = new ArrayList<>();
        countryRepository.findAll().iterator().forEachRemaining(myList::add);

        myList.sort((c1,c2)-> (int)c2.getPopulation()-(int)c1.getPopulation());

        return new ResponseEntity<>(myList.get(0),HttpStatus.OK);
    }

    @GetMapping(value = "/population/median", produces ="application/json")
    public ResponseEntity<?> findMedianPopulationCountry()
    {
        List<Country> myList = new ArrayList<>();
        countryRepository.findAll().iterator().forEachRemaining(myList::add);

        myList.sort((c1,c2) -> (int)c1.getPopulation() - (int)c2.getPopulation());
        Country median;
        if(myList.size()%2 == 0)
        {
           median = myList.get(myList.size()/2);
        }
        else
        {
           median = myList.get(myList.size()/2+1);
        }

        return new ResponseEntity<>(median,HttpStatus.OK);
    }
}
