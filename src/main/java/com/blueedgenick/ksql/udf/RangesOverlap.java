

package com.blueedgenick.ksql.udf;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.confluent.ksql.function.udf.Udf;
import io.confluent.ksql.function.udf.UdfDescription;
import io.confluent.ksql.function.udf.UdfParameter;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

@UdfDescription(
    name = "ranges_overlap",
    description = "Given an array of strings, each of which is a representation of a JSON object"
        + " with attributes describing some numeric range, tests whether any of those ranges"
        + " overlap and returns TRUE or FALSE accordingly. Returns NULL if the input array is null,"
        + " or the start and end attributes of the range cannot be found in an entry.")
public class RangesOverlap {

  static ObjectMapper mapper = new ObjectMapper();

  @Udf
  public Boolean overlaps(@UdfParameter(
      description = "Array of JSON values", value = "data") final List<String> data,
      @UdfParameter(
          description = "Name of start field", value = "startField") final String startName,
      @UdfParameter(
          description = "Name of end field", value = "endField") final String endName) {

    if (data == null || startName == null || endName == null)
      return null;

    List<Range> ranges = new LinkedList<Range>();
    for (String rawEntry : data) {
      JsonNode jsonEntry;
      try {
        jsonEntry = mapper.readTree(rawEntry);
        long start = jsonEntry.get(startName).asLong();
        long end = jsonEntry.get(endName).asLong();
        ranges.add(new Range(start, end));
      } catch (JsonProcessingException e) {
        return null;
      }
    }

    ListIterator<Range> rangeIt = ranges.listIterator();
    while (rangeIt.hasNext()) {
      Range thisRange = rangeIt.next();
      List<Range> otherRanges = ranges.subList(rangeIt.nextIndex(), ranges.size());
      for (Range otherRange : otherRanges) {
        if (thisRange.overlaps(otherRange)) {
          return true;
        }
      }
    }
    return false;
  }



  private class Range {
    long start;
    long end;

    Range(long start, long end) {
      this.start = start;
      this.end = end;
    }

    boolean overlaps(Range other) {
      return (this.end > other.start && this.start < other.end);
    }
  }

}
