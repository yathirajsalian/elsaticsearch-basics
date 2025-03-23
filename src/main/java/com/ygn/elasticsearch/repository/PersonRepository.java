package com.ygn.elasticsearch.repository;

import com.ygn.elasticsearch.Document.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


public interface PersonRepository extends ElasticsearchRepository<Person,String> {

}
