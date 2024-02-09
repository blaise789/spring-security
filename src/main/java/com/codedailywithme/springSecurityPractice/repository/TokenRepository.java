package com.codedailywithme.springSecurityPractice.repository;

import com.codedailywithme.springSecurityPractice.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token,Integer> {
@Query(value = """
select  t from Token t inner join User u\s on t.user.id=u.id\s where
 u.id =:id and (t.expired=false  or t.revoked=false )\s""")
    List<Token> findALlValidTokenByUser(Integer id);
   Optional<Token> findByToken(String token);
    String findByUserId(Integer id);
}