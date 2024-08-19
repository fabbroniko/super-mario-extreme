package com.fabbroniko.audio;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResetClipLineListenerTest {

    @Mock
    private LineEvent lineEvent;
    @Mock
    private Clip clip;
    @InjectMocks
    private ResetClipLineListener resetClipLineListener;

    @Test
    void shouldGetType() {
        resetClipLineListener.update(lineEvent);

        verify(lineEvent).getType();
    }

    @Test
    void shouldGetSourceWhenLineEventIsStop() {
        when(lineEvent.getType()).thenReturn(LineEvent.Type.STOP);
        when(lineEvent.getSource()).thenReturn(clip);

        resetClipLineListener.update(lineEvent);

        verify(lineEvent).getSource();
    }

    @Test
    void shouldResetFrameOnStop() {
        when(lineEvent.getType()).thenReturn(LineEvent.Type.STOP);
        when(lineEvent.getSource()).thenReturn(clip);

        resetClipLineListener.update(lineEvent);

        verify(clip).setFramePosition(0);
    }
}