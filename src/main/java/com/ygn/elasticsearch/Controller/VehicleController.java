package com.ygn.elasticsearch.Controller;

import com.ygn.elasticsearch.Document.Vehicle;
import com.ygn.elasticsearch.Service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicle")
public class VehicleController {

    private final VehicleService service;

    @Autowired
    public VehicleController(VehicleService service)
    {
        this.service = service;
    }

    @PostMapping
    public void index(@RequestBody final Vehicle vehicle)
    {
        service.index(vehicle);
    }

    @GetMapping("/{id}")
    public Vehicle getById(@PathVariable final String id)
    {
        return service.getById(id);
    }


}
