package com.fabbroniko.environment;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;

public class BoundingBoxWithinTest {

    private final BoundingBox outer = new BoundingBox(new ImmutablePosition(0, 0), new ImmutableDimension2D(100, 100));
    private final Random random = new Random();

    @RepeatedTest(1000)
    void shouldReturnTrueWhenFullyWithinBounds() {
        final int randomX = random.nextInt(0, 91);
        final int randomY = random.nextInt(0, 91);

        final Position subjectPosition = new ImmutablePosition(randomX, randomY);
        final Dimension2D subjectDimension = new ImmutableDimension2D(10, 10);
        final BoundingBox testSubject = new BoundingBox(subjectPosition, subjectDimension);

        assertThat(testSubject.within(outer))
            .isTrue();
    }

    @Test
    void shouldReturnFalseWhenFullyOutsideBoundsLeft() {
        final Position subjectPosition = new ImmutablePosition(-20, 10);
        final Dimension2D subjectDimension = new ImmutableDimension2D(10, 10);
        final BoundingBox testSubject = new BoundingBox(subjectPosition, subjectDimension);

        assertThat(testSubject.within(outer))
            .isFalse();
    }

    @Test
    void shouldReturnFalseWhenFullyOutsideBoundsRight() {
        final Position subjectPosition = new ImmutablePosition(120, 10);
        final Dimension2D subjectDimension = new ImmutableDimension2D(10, 10);
        final BoundingBox testSubject = new BoundingBox(subjectPosition, subjectDimension);

        assertThat(testSubject.within(outer))
            .isFalse();
    }

    @Test
    void shouldReturnFalseWhenFullyOutsideBoundsTop() {
        final Position subjectPosition = new ImmutablePosition(10, -20);
        final Dimension2D subjectDimension = new ImmutableDimension2D(10, 10);
        final BoundingBox testSubject = new BoundingBox(subjectPosition, subjectDimension);

        assertThat(testSubject.within(outer))
            .isFalse();
    }

    @Test
    void shouldReturnFalseWhenFullyOutsideBoundsBottom() {
        final Position subjectPosition = new ImmutablePosition(10, 101);
        final Dimension2D subjectDimension = new ImmutableDimension2D(10, 10);
        final BoundingBox testSubject = new BoundingBox(subjectPosition, subjectDimension);

        assertThat(testSubject.within(outer))
            .isFalse();
    }

    @Test
    void shouldReturnTrueWhenIntersectingOnTheLeft() {
        final Position subjectPosition = new ImmutablePosition(-9, 10);
        final Dimension2D subjectDimension = new ImmutableDimension2D(10, 10);
        final BoundingBox testSubject = new BoundingBox(subjectPosition, subjectDimension);

        assertThat(testSubject.within(outer))
            .isTrue();
    }

    @Test
    void shouldReturnTrueWhenIntersectingOnTheRight() {
        final Position subjectPosition = new ImmutablePosition(95, 10);
        final Dimension2D subjectDimension = new ImmutableDimension2D(10, 10);
        final BoundingBox testSubject = new BoundingBox(subjectPosition, subjectDimension);

        assertThat(testSubject.within(outer))
            .isTrue();
    }

    @Test
    void shouldReturnTrueWhenIntersectingOnTheTop() {
        final Position subjectPosition = new ImmutablePosition(10, -9);
        final Dimension2D subjectDimension = new ImmutableDimension2D(10, 10);
        final BoundingBox testSubject = new BoundingBox(subjectPosition, subjectDimension);

        assertThat(testSubject.within(outer))
            .isTrue();
    }

    @Test
    void shouldReturnTrueWhenIntersectingOnTheBottom() {
        final Position subjectPosition = new ImmutablePosition(10, 95);
        final Dimension2D subjectDimension = new ImmutableDimension2D(10, 10);
        final BoundingBox testSubject = new BoundingBox(subjectPosition, subjectDimension);

        assertThat(testSubject.within(outer))
            .isTrue();
    }
}
