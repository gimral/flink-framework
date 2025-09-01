package com.gimral.streaming.join;

import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;

public class BuiltinJoinFuncs {

  /** Inner join two KeyedStream using OneToOneJoin. */
  public static <KEY, L, R> DataStream<Tuple2<KEY, Tuple2<L, R>>> oneToOneJoin(
      KeyedStream<L, KEY> leftStream,
      KeyedStream<R, KEY> rightStream,
      long leftStateDuration,
      long rightStateDuration) {
    return oneToOneJoin(
        leftStream, rightStream, leftStateDuration, rightStateDuration, JoinType.INNER);
  }

  public static <KEY, L, R> DataStream<Tuple2<KEY, Tuple2<L, R>>> oneToOneJoin(
      KeyedStream<L, KEY> leftStream,
      KeyedStream<R, KEY> rightStream,
      long leftStateDuration,
      long rightStateDuration,
      JoinType joinType) {
    return leftStream
        .connect(rightStream)
        .process(new OneToOneJoin<>(joinType, leftStateDuration, rightStateDuration));
  }

  /**
   * Inner join two DataStreams using OneToOneJoin. The two streams will be keyed by KeySelector
   * respectively.
   */
  public static <KEY, L, R> DataStream<Tuple2<KEY, Tuple2<L, R>>> oneToOneJoin(
      DataStream<L> leftStream,
      KeySelector<L, KEY> leftKeySelector,
      DataStream<R> rightStream,
      KeySelector<R, KEY> rightKeySelector,
      long leftStateDuration,
      long rightStateDuration) {
    return oneToOneJoin(
        leftStream,
        leftKeySelector,
        rightStream,
        rightKeySelector,
        leftStateDuration,
        rightStateDuration,
        JoinType.INNER);
  }

  public static <KEY, L, R> DataStream<Tuple2<KEY, Tuple2<L, R>>> oneToOneJoin(
      DataStream<L> leftStream,
      KeySelector<L, KEY> leftKeySelector,
      DataStream<R> rightStream,
      KeySelector<R, KEY> rightKeySelector,
      long leftStateDuration,
      long rightStateDuration,
      JoinType joinType) {
    return oneToOneJoin(
        leftStream.keyBy(leftKeySelector),
        rightStream.keyBy(rightKeySelector),
        leftStateDuration,
        rightStateDuration,
        joinType);
  }

  // OneToManyJoin helpers
  public static <KEY, L, R> DataStream<Tuple2<KEY, Tuple2<L, R>>> oneToManyJoin(
      KeyedStream<L, KEY> leftStream,
      KeyedStream<R, KEY> rightStream,
      long leftStateDuration,
      long rightStateDuration) {
    return oneToManyJoin(
        leftStream, rightStream, leftStateDuration, rightStateDuration, JoinType.INNER);
  }

  public static <KEY, L, R> DataStream<Tuple2<KEY, Tuple2<L, R>>> oneToManyJoin(
      KeyedStream<L, KEY> leftStream,
      KeyedStream<R, KEY> rightStream,
      long leftStateDuration,
      long rightStateDuration,
      JoinType joinType) {
    return leftStream
        .connect(rightStream)
        .process(new OneToManyJoin<>(joinType, leftStateDuration, rightStateDuration));
  }

  public static <KEY, L, R> DataStream<Tuple2<KEY, Tuple2<L, R>>> oneToManyJoin(
      DataStream<L> leftStream,
      KeySelector<L, KEY> leftKeySelector,
      DataStream<R> rightStream,
      KeySelector<R, KEY> rightKeySelector,
      long leftStateDuration,
      long rightStateDuration) {
    return oneToManyJoin(
        leftStream,
        leftKeySelector,
        rightStream,
        rightKeySelector,
        leftStateDuration,
        rightStateDuration,
        JoinType.INNER);
  }

  public static <KEY, L, R> DataStream<Tuple2<KEY, Tuple2<L, R>>> oneToManyJoin(
      DataStream<L> leftStream,
      KeySelector<L, KEY> leftKeySelector,
      DataStream<R> rightStream,
      KeySelector<R, KEY> rightKeySelector,
      long leftStateDuration,
      long rightStateDuration,
      JoinType joinType) {
    return oneToManyJoin(
        leftStream.keyBy(leftKeySelector),
        rightStream.keyBy(rightKeySelector),
        leftStateDuration,
        rightStateDuration,
        joinType);
  }
}
