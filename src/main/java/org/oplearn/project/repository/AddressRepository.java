package org.oplearn.project.repository;

import org.oplearn.project.dto.response.AddressResponse;
import org.oplearn.project.entity.Addresses;
import org.oplearn.project.entity.Users;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;

public interface AddressRepository extends MongoRepository<Addresses, Integer> {

  @Aggregation(pipeline = {
        """
              {
                $match: {
                  id: {
                    $eq: ?0
                  }
                }
              }""",
        """
                            {
              $count : "total"
                            }
                           \s"""
  })
  int count(Integer id);

  @Aggregation(pipeline = {
        """
                {
                  $match: {
                    _id: { $eq: ?0},
                    is_deleted: false,
                  }
                }
              """,
        """
              {
                  $project: {
                    _id : 1,
                    province:1,
                    district:1,
                    ward: 1
                  }
               }
              """


  })
  AddressResponse getById(Integer id);

  @Aggregation(pipeline = {
        """
                              {
                                $match: {
                                  is_deleted: false,
                                }
                              }
                            
              """,
        """
               {
                  $project: {
                   is_deleted : 1
                  }
                }
                           
              """
  }
  )
  List<AddressResponse> getAll();


  @Query("{'id': ?0}")
  @Update("'$set' : {'is_deleted': true }")
  void deleted(Integer id);
}
