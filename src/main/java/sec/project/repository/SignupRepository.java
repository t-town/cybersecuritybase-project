package sec.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sec.project.domain.Signup;

public interface SignupRepository extends JpaRepository<Signup, Long> {
    
   // @Query("SELECT name where name = firstname AND password = password")
   // Boolean login(String firstname, String password);
}
