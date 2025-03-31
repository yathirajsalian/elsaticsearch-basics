package com.ygn.elasticsearch.Controller;

import com.ygn.elasticsearch.Service.IndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class IndexController {

    private final IndexService service;

    @Autowired
    public IndexController(IndexService service) {
        this.service = service;
    }

    @PostMapping("/recreate")
    public void recreateAllIndices()
    {
        service.recreateIndices();
    }
}
