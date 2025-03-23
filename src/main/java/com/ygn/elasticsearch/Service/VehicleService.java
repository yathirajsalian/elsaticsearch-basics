package com.ygn.elasticsearch.Service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ygn.elasticsearch.Document.Vehicle;
import com.ygn.elasticsearch.Helper.Indices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class VehicleService {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final Logger LOG = LoggerFactory.getLogger(VehicleService.class);

    private final ElasticsearchClient elasticsearchClient;

    @Autowired
    public VehicleService(ElasticsearchClient elasticsearchClient) {
        this.elasticsearchClient = elasticsearchClient;
    }

    public Boolean index(final Vehicle vehicle) {
        /*
                    IndexRequest.Builder<Vehicle> builder = new IndexRequest.Builder<>();
                    builder.index(Indices.VEHICLE_INDEX);
                    builder.id(vehicle.getId());
                    builder.document(vehicle);

                    IndexRequest<Vehicle> request = builder.build();

         */
        try {
            final String vehicleAsString = MAPPER.writeValueAsString(vehicle);

            // Properly build the IndexRequest
            IndexRequest<Vehicle> request = IndexRequest.of(i -> i
                    .index(Indices.VEHICLE_INDEX)
                    .id(vehicle.getId())
                    .document(vehicle)
            );

            if (vehicle.getNumber() == null) {
                vehicle.setNumber("Default Name"); // Just for testing
            }

            IndexResponse response = elasticsearchClient.index(request);

            if (response.result().name().equals("Created")) {
                LOG.info("Successfully indexed vehicle with ID: {}", vehicle.getId());
                return true;
            } else {
                LOG.warn("Failed to index vehicle with ID: {}", vehicle.getId());
                return false;
            }
        } catch (IOException e) {
            LOG.error("Error indexing vehicle: {}", e.getMessage(), e);
            return false;
        }
    }

    public Vehicle getById(final String vehicleId) {
        /*
            GetRequest.Builder builder = new GetRequest.Builder();
            builder.index(Indices.VEHICLE_INDEX);
            builder.id(vehicleId);

            GetRequest request = builder.build();
            Vehicle vehicle = elasticsearchClient.get(request, Vehicle.class).source();
        */
        try {
            return elasticsearchClient.get(g -> g
                            .index(Indices.VEHICLE_INDEX)
                            .id(vehicleId),
                    Vehicle.class
            ).source();
        } catch (Exception e) {
            LOG.error("Error fetching vehicle with ID: {}", vehicleId, e);
            return null;
        }
    }

}
