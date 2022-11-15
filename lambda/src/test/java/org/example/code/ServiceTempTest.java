package org.example.code;

import static org.mockito.Mockito.when;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ServiceTempTest {

    ServiceTemp serviceTemp;

    @Mock
    RepositoryTemp repositoryTemp;

    @BeforeEach
    void setUp() {
        serviceTemp = new ServiceTemp(repositoryTemp);
    }

    @DisplayName("id로 찾기")
    @Test
    void findId() {
        // given
        String itemId = "1";
        when(repositoryTemp.findById(itemId)).thenReturn("ok");

        //when
        String findId = serviceTemp.findId(itemId);

        // then
        Assertions.assertThat(findId).isEqualTo("ok");
    }
}
