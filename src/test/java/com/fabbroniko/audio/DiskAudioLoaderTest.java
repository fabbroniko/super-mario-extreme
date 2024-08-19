package com.fabbroniko.audio;

import com.fabbroniko.error.ResourceLoadingException;
import com.fabbroniko.resource.ResourceLoader;
import com.fabbroniko.resource.ResourceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.Clip;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DiskAudioLoaderTest {

    private static final String RESOURCE_NAME = "test-resource-name";
    private static final String RESOURCE_PATH = "test-resource-path";

    @Mock
    private ResourceLoader resourceLoader;
    @Mock
    private ResourceLocator resourceLocator;
    @Mock
    private AudioFactory audioFactory;
    @Mock
    private Clip clip;
    @Mock
    private InputStream inputStream;
    @Mock
    private AudioInputStream audioInputStream;
    @InjectMocks
    private DiskAudioLoader diskAudioLoader;

    @BeforeEach
    void setUp() {
        when(audioFactory.createClip()).thenReturn(clip);
        when(resourceLocator.findByName(anyString())).thenReturn(RESOURCE_PATH);
        when(resourceLoader.load(anyString())).thenReturn(inputStream);
        when(audioFactory.createAudioInputStream(any())).thenReturn(audioInputStream);
    }

    @Test
    void shouldResolvePathWithLocator() {
        diskAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(resourceLocator).findByName(RESOURCE_NAME);
    }

    @Test
    void shouldLoadResourceAsInputStream() {
        diskAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(resourceLoader).load(RESOURCE_PATH);
    }

    @Test
    void shouldCreateClip() {
        diskAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(audioFactory).createClip();
    }

    @Test
    void shouldCreateAudioInputStream() {
        diskAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(audioFactory).createAudioInputStream(inputStream);
    }

    @Test
    void shouldOpenClipWithAudioInputStream() throws Exception {
        diskAudioLoader.loadClipByName(RESOURCE_NAME);

        verify(clip).open(audioInputStream);
    }

    @Test
    void shouldReturnClip() {
        assertThat(diskAudioLoader.loadClipByName(RESOURCE_NAME))
                .isEqualTo(clip);
    }

    @Test
    void shouldThrowResourceLoadingExceptionWhenCreateClipThrows() {
        final Throwable exception = new RuntimeException();
        reset(audioFactory);
        when(audioFactory.createClip()).thenThrow(exception);

        assertThatThrownBy(() -> diskAudioLoader.loadClipByName(RESOURCE_NAME))
                .isInstanceOf(ResourceLoadingException.class)
                .hasCause(exception);
    }

    @Test
    void shouldThrowExceptionWhenCreateAudioInputStreamFails() {
        final Throwable exception = new RuntimeException();
        when(audioFactory.createAudioInputStream(any())).thenThrow(exception);

        assertThatThrownBy(() -> diskAudioLoader.loadClipByName(RESOURCE_NAME))
                .isInstanceOf(ResourceLoadingException.class)
                .hasCause(exception);
    }
}