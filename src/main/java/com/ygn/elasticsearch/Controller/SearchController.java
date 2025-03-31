package com.ygn.elasticsearch.Controller;

import com.ygn.elasticsearch.Document.Person;
import com.ygn.elasticsearch.Document.Vehicle;
import com.ygn.elasticsearch.Service.ElasticsearchSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private ElasticsearchSearchService searchService;

    @Autowired
    public SearchController(ElasticsearchSearchService searchService)
    {
        this.searchService = searchService;
    }

    @PostMapping("/vehicle")
    public ResponseEntity<List<Vehicle>> searchVehicles(@RequestBody Map<String, String> request) {
        String query = request.get("query");  // Extracts query from JSON body
        List<Vehicle> results = searchService.searchVehiclesByNumberOrType(query);
        return ResponseEntity.ok(results);
    }

    @PostMapping("/person")
    public ResponseEntity<List<Person>> searchPerson(@RequestBody Map<String, String> request) {
        String query = request.get("name");  // Extracts query from JSON body
        List<Person> results = searchService.searchPersonsByName(query);
        return ResponseEntity.ok(results);
    }


}
