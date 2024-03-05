package edu.jcourse.node.integration.repository;

import edu.jcourse.node.entity.RawData;
import edu.jcourse.node.integration.IntegrationTestBase;
import edu.jcourse.node.repository.RawDataRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor
class RawDataRepositoryIT extends IntegrationTestBase {
    private final RawDataRepository rawDataRepository;

    @Test
    void saveRawData() {
        RawData rawData = RawData.builder()
                .event(new Update())
                .build();
        RawData expectedResult = rawDataRepository.save(rawData);

        assertThat(expectedResult.getId()).isNotNull().isEqualTo(3L);
        assertThat(rawDataRepository.findAll()).hasSize(3);
    }
}