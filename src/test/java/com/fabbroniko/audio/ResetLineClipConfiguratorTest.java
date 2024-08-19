package com.fabbroniko.audio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.Clip;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ResetLineClipConfiguratorTest {

    @Mock
    private Clip clip;
    @InjectMocks
    private ResetLineClipConfigurator resetLineClipConfigurator;

    @Test
    void shouldConfigureListener() {
        resetLineClipConfigurator.configureLineListener(clip);

        verify(clip).addLineListener(any(ResetClipLineListener.class));
    }

    @Test
    void shouldReturnSameInstance() {
        assertThat(resetLineClipConfigurator.configureLineListener(clip))
                .isEqualTo(clip);
    }
}