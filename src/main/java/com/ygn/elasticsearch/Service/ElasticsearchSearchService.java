package com.ygn.elasticsearch.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.ygn.elasticsearch.Document.Person;
import com.ygn.elasticsearch.Document.Vehicle;
import com.ygn.elasticsearch.Helper.Indices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ElasticsearchSearchService {

    private static final Logger LOG = LoggerFactory.getLogger(ElasticsearchSearchService.class);
    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    public ElasticsearchSearchService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    // Match Query for Person (Search by name)
    public List<Person> searchPersonsByName(String name) {
        try {
            SearchRequest request = SearchRequest.of(s -> s
                    .index(Indices.PERSON_INDEX)
                    .query(q -> q.match(m -> m.field("name").query(name)))
            );

            SearchResponse<Person> response = elasticsearchClient.search(request, Person.class);
            return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
        } catch (IOException e) {
            LOG.error("Error searching persons: {}", e.getMessage(), e);
            return List.of();
        }
    }

    // Match Query for Vehicle (Search by number or type)
    public List<Vehicle> searchVehiclesByNumberOrType(String query) {
        try {
            SearchRequest request = SearchRequest.of(s -> s
                    .index(Indices.VEHICLE_INDEX)
                    .query(q -> q.bool(b -> b
                            .should(m -> m.match(t -> t.field("number").query(query)))
                            .should(m -> m.match(t -> t.field("type").query(query)))
                    ))
            );

            SearchResponse<Vehicle> response = elasticsearchClient.search(request, Vehicle.class);
            return response.hits().hits().stream().map(Hit::source).collect(Collectors.toList());
        } catch (IOException e) {
            LOG.error("Error searching vehicles: {}", e.getMessage(), e);
            return List.of();
        }
    }
}
