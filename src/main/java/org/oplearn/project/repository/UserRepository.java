package org.oplearn.project.repository;

import jakarta.transaction.Transactional;
import org.oplearn.project.dto.response.UserResponse;
import org.oplearn.project.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.Update;

import java.util.List;

public interface UserRepository extends MongoRepository<User, String> {

  @Aggregation(pipeline = {
        """
                              {
                                $match: {
                                  is_deleted: false,
                                }
                              }
                            
              """
  }
  )
  List<UserResponse> findAllUsers();

  @Aggregation({
        """
              {
                   $match: {
                         name: { $regex: ?0, $options: "i" },
                         is_deleted: false
                       }
              }
              """
  })
  List<UserResponse> search(String keyword, Pageable pageable);

  @Aggregation({
        """
        {
            $match: {
                _id: ?0,
                isDeleted: false
            }
        }
        """
  })
  UserResponse findUserById(String id);



  @Query("{'id': ?0}")
  @Update("{'$set': {'is_deleted': true}}")
  void deleteUser(String id);





//  @Aggregation({
//        """
//              {
//                  $match: {
//                    _id: ?0
//                  }
//                }
//                {
//                  $set: {
//                    username: ?1,
//                    password: ?2,
//                    name: ?3,
//                    email: ?4
//                  }
//                }
//                {
//                  $project: {
//                    password: 0,
//                    name: 0
//                  }
//                }
//              """
//  })
//  UserResponse update(String id, String username, String password, String name, String email);
}
