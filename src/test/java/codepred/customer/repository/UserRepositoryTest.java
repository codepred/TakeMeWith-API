package codepred.customer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import codepred.configuration.RepoTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@RepoTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repo;

    @Nested
    class test {

        @Test
        void shouldReturnSchadeninformationDto() {
            assertThat(5).isEqualTo(5);
        }


    }

}