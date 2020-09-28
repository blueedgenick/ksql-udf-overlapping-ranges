package com.blueedgenick.ksql.udf;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.Matchers.contains;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;



public class RangesOverlapTest {
  private final RangesOverlap udf = new RangesOverlap();

    @Test
    public void shouldDetectSimpleOverlap() {
      final String input1 = "{\"startDt\":100, \"endDt\":200}";
      final String input2 = "{\"startDt\":150, \"endDt\":250}";
      final List<String> input = Arrays.asList(input1, input2);
      final Boolean result = udf.overlaps(input, "startDt", "endDt");
      assertThat(result, is(true));
    }

    @Test
    public void shouldDetectContained() {
      final String input1 = "{\"startDt\":100, \"endDt\":200}";
      final String input2 = "{\"startDt\":120, \"endDt\":150}";
      final List<String> input = Arrays.asList(input1, input2);
      final Boolean result = udf.overlaps(input, "startDt", "endDt");
      assertThat(result, is(true));
    }

    @Test
    public void shouldDetectNoOverlap() {
      final String input1 = "{\"startDt\":100, \"endDt\":200}";
      final String input2 = "{\"startDt\":300, \"endDt\":400}";
      final List<String> input = Arrays.asList(input1, input2);
      final Boolean result = udf.overlaps(input, "startDt", "endDt");
      assertThat(result, is(false));
    }

    @Test
    public void shouldDetectWithMultipleRanges() {
      final String input1 = "{\"startDt\":100, \"endDt\":200}";
      final String input2 = "{\"startDt\":220, \"endDt\":250}";
      final String input3 = "{\"startDt\":150, \"endDt\":250}";
      final String input4 = "{\"startDt\":320, \"endDt\":350}";
      final List<String> input = Arrays.asList(input1, input2, input3, input4);
      final Boolean result = udf.overlaps(input, "startDt", "endDt");
      assertThat(result, is(true));
    }
    @Test
    public void sample() {
      final String input1 =
          "{\"startDt\": 1504742400000, \"endDt\": 7983792000000, \"updated\": \"2020-09-21 22:00:01.390440\"}";
      final String input2 =
          "{\"startDt\": 1483228800000, \"endDt\":1504224000000, \"updated\": \"2020-09-21 22:00:03.390440\"}";
      final String input3 = "{\"startDt\":150, \"endDt\":250}";
      final String input4 = "{\"startDt\":320, \"endDt\":350}";
      final List<String> input = Arrays.asList(input1, input2, input3, input4);
      final Boolean result = udf.overlaps(input, "startDt", "endDt");
      assertThat(result, is(false));
    }

    @Test
    public void shouldReturnNullForNullDataInput() {
      final Boolean result = udf.overlaps(null, "startDt", "endDt");
      assertThat(result, is(nullValue()));
    }


    @Test
    public void shouldReturnNullForNullStartInput() {
      final String input1 = "{\"startDt\":100, \"endDt\":200}";
      final String input2 = "{\"startDt\":120, \"endDt\":150}";
      final List<String> input = Arrays.asList(input1, input2);
      final Boolean result = udf.overlaps(input, null, "endDt");
      assertThat(result, is(nullValue()));
    }

    @Test
    public void shouldReturnNullForNullEndInput() {
      final String input1 = "{\"startDt\":100, \"endDt\":200}";
      final String input2 = "{\"startDt\":120, \"endDt\":150}";
      final List<String> input = Arrays.asList(input1, input2);
      final Boolean result = udf.overlaps(input, "startDt", null);
      assertThat(result, is(nullValue()));
    }

}
