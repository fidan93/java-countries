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
    private List<Country> findNamesU(List<Country> myList,CheckCountry tester)
    {
        List<Country> tempList = new ArrayList<>();
        for(Country c : myList)
        {
            if(tester.test(c))
            {
                tempList.add(c);
            }
        }
        return tempList;
    }
    @Autowired
    CountryRepository countryrep;
  //http://localhost:2021/names/all
    @GetMapping(value = "/names/all",produces = {"application/json"})
    public ResponseEntity<?> listAllCountries()
    {
        List <Country> myList = new ArrayList<>();
        countryrep.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((n1,n2)-> n1.getName().compareToIgnoreCase(n2.getName()));
        return new ResponseEntity<>(myList,
            HttpStatus.OK);
    }
   //http://localhost:2019/names/start/u
   @GetMapping(value = "/names/start/{letter}", produces = {"application/json"})
   public ResponseEntity<?> listCountryFname(@PathVariable char letter)
   {
       List<Country> myList = new ArrayList<>();
       countryrep.findAll().iterator().forEachRemaining(myList::add);

       List<Country> rtnList = findNamesU(myList, e-> e.getName().toLowerCase().charAt(0) == letter);
       rtnList.sort((n1,n2) -> n1.getName().compareToIgnoreCase(n2.getName()));
       for (Country e : rtnList)
       {
           System.out.println(e);
       }
       // total of all salaries
//       double total = 0;
//       for (Employee e : myList)
//       {
//           total = total + e.getSalary();
//       }
//       System.out.println(total);

       return new ResponseEntity<>(rtnList, HttpStatus.OK);
   }
   @GetMapping(value = "/population/total",produces = {"application/json"})
   public ResponseEntity <?> getPopulationTotal()
   {
       List<Country> myList = new ArrayList<>();
       countryrep.findAll().iterator().forEachRemaining(myList::add);
       long total = 0;
       for(Country c: myList)
       {
           total = total + c.getPopulation();
       }
//       System.out.println(total);
       return new ResponseEntity<>(("The Total Population is "+total),HttpStatus.OK);
   }
   @GetMapping(value = "/population/min",produces = {"application/json"})
    public ResponseEntity<?> minPopulation()
   {
       List<Country> myList = new ArrayList<>();
       countryrep.findAll().iterator().forEachRemaining(myList::add);
       myList.sort((c1,c2)-> (int)(c1.getPopulation()-c2.getPopulation()));

       return new ResponseEntity<>(myList.get(0),HttpStatus.OK);
   }
   @GetMapping(value = "population/max",produces = {"application/json"})
    public ResponseEntity <?> getPopulationMax()
   {
       List<Country> myList = new ArrayList<>();
       countryrep.findAll().iterator().forEachRemaining(myList::add);
       myList.sort((c1,c2)-> (int)(c2.getPopulation()-c1.getPopulation()));

       return new ResponseEntity<>(myList.get(0),HttpStatus.OK);
   }
    @GetMapping(value = "population/median",produces = {"application/json"})
    public ResponseEntity <?> getPopulationMedian()
    {
        List<Country> myList = new ArrayList<>();
        countryrep.findAll().iterator().forEachRemaining(myList::add);
        myList.sort((c1,c2)-> (int)(c1.getPopulation()-c2.getPopulation()));
        int size = myList.size();
        System.out.println(size/2);
        return new ResponseEntity<>(myList.get(size/2),HttpStatus.OK);
    }
}
