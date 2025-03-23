package com.ygn.elasticsearch.repository;

import com.ygn.elasticsearch.Person.Person;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


public interface PersonRepository extends ElasticsearchRepository<Person,String> {

}
