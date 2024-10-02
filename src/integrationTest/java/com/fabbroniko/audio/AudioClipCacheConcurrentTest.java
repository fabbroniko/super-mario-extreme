package com.fabbroniko.audio;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.sound.sampled.Clip;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AudioClipCacheConcurrentTest {

    private static final int NUM_CONCURRENT_INSERTS = 10;

    @Spy
    private HashMap<String, Object> cache;
    @Mock
    private Clip clip;
    @InjectMocks
    private AudioClipCache audioClipCache;

    @RepeatedTest(10)
    void shouldOnlyInsertDuplicateEntryOnce() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(NUM_CONCURRENT_INSERTS);
        final List<Callable<Object>> tasks = new ArrayList<>();
        for(int i = 0; i < NUM_CONCURRENT_INSERTS; i++) {
            tasks.add(Executors.callable(() -> audioClipCache.insert("some", clip)));
        }
        executorService.invokeAll(tasks);

        verify(cache, times(1)).put(anyString(), any());
    }
}
